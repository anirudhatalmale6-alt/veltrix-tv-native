package com.veltrix.tv.ui.live

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.ChannelListHolder
import com.veltrix.tv.data.models.Category
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.FavoriteEntity
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.visible
import com.veltrix.tv.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LiveFragment : Fragment(), MainActivity.DpadNavigable {

    private lateinit var categoryContainer: LinearLayout
    private lateinit var rvCategories: RecyclerView
    private lateinit var rvChannels: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    // Preview player views
    private lateinit var previewPlayerView: PlayerView
    private lateinit var previewLoading: ProgressBar
    private lateinit var tvPreviewChannelName: TextView
    private lateinit var tvPreviewEpgNow: TextView
    private lateinit var tvPreviewEpgNext: TextView
    private lateinit var tvPreviewResolution: TextView
    private lateinit var tvPreviewAudio: TextView

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var channelListAdapter: ChannelListAdapter

    private var allStreams = listOf<LiveStream>()
    private var previewPlayer: ExoPlayer? = null
    private var currentPreviewStreamId: Int = -1
    private var selectedStreamId: Int = -1

    private lateinit var etSearchBar: EditText

    private var isCategoryVisible = true
    private var categoryWidth = 0
    private var isFavoritesMode = false
    private var allLiveCache = listOf<LiveStream>()
    private var searchWatcherAdded = false

    override fun getMainContentView(): View = rvChannels

    override fun canGoLeft(): Boolean {
        val focused = activity?.currentFocus ?: return false
        if (rvChannels.isAncestorOf(focused)) {
            if (!isCategoryVisible) {
                expandCategories()
                rvCategories.post { rvCategories.getChildAt(0)?.requestFocus() }
                return false
            }
            return true
        }
        return false
    }

    private fun RecyclerView.isAncestorOf(view: View): Boolean {
        var current: android.view.ViewParent? = view.parent
        while (current != null) {
            if (current == this) return true
            current = current.parent
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryContainer = view.findViewById(R.id.categoryContainer)
        rvCategories = view.findViewById(R.id.rvCategories)
        rvChannels = view.findViewById(R.id.rvChannels)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        // Preview player
        previewPlayerView = view.findViewById(R.id.previewPlayerView)
        previewLoading = view.findViewById(R.id.previewLoading)
        tvPreviewChannelName = view.findViewById(R.id.tvPreviewChannelName)
        tvPreviewEpgNow = view.findViewById(R.id.tvPreviewEpgNow)
        tvPreviewEpgNext = view.findViewById(R.id.tvPreviewEpgNext)
        tvPreviewResolution = view.findViewById(R.id.tvPreviewResolution)
        tvPreviewAudio = view.findViewById(R.id.tvPreviewAudio)
        etSearchBar = view.findViewById(R.id.etSearchBar)

        setupAdapters()
        setupCategoryAutoHide()
        loadCategories()
    }

    private fun isPhoneScreen(): Boolean {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return screenWidthDp < 600
    }

    private fun setupCategoryAutoHide() {
        categoryContainer.post {
            categoryWidth = categoryContainer.width
            if (isPhoneScreen()) {
                collapseCategories()
            }
        }

        rvChannels.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && isCategoryVisible) collapseCategories()
        }
        rvChannels.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && isCategoryVisible) collapseCategories()
                }
            }
            override fun onChildViewDetachedFromWindow(view: View) {}
        })

        rvCategories.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !isCategoryVisible) expandCategories()
        }
        rvCategories.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && !isCategoryVisible) expandCategories()
                }
            }
            override fun onChildViewDetachedFromWindow(view: View) {}
        })
    }

    private fun collapseCategories() {
        if (!isCategoryVisible) return
        isCategoryVisible = false
        val animator = ValueAnimator.ofInt(categoryWidth, 0)
        animator.duration = 200
        animator.addUpdateListener { anim ->
            val params = categoryContainer.layoutParams
            params.width = anim.animatedValue as Int
            categoryContainer.layoutParams = params
        }
        animator.start()
    }

    private fun expandCategories() {
        if (isCategoryVisible) return
        isCategoryVisible = true
        val animator = ValueAnimator.ofInt(0, categoryWidth)
        animator.duration = 200
        animator.addUpdateListener { anim ->
            val params = categoryContainer.layoutParams
            params.width = anim.animatedValue as Int
            categoryContainer.layoutParams = params
        }
        animator.start()
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter { category ->
            when (category.categoryId) {
                "favorites" -> {
                    isFavoritesMode = true
                    hideSearchBar()
                    loadFavorites()
                }
                "search" -> {
                    isFavoritesMode = false
                    showSearchMode()
                }
                else -> {
                    isFavoritesMode = false
                    hideSearchBar()
                    loadStreams(category.categoryId)
                }
            }
        }
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = categoryAdapter

        channelListAdapter = ChannelListAdapter(
            onChannelClick = { stream, position ->
                if (stream.streamId == selectedStreamId) {
                    openPlayer(stream, position)
                } else {
                    selectedStreamId = stream.streamId
                    startPreview(stream)
                    channelListAdapter.setPlaying(stream.streamId)
                }
            },
            onChannelFocus = { stream, _ ->
                startPreview(stream)
            },
            onChannelLongClick = { stream ->
                showLongPressMenu(stream)
            }
        )
        rvChannels.layoutManager = LinearLayoutManager(requireContext())
        rvChannels.adapter = channelListAdapter
        rvChannels.setHasFixedSize(true)
        rvChannels.setItemViewCacheSize(20)
    }

    private fun showLongPressMenu(stream: LiveStream) {
        val options = arrayOf("Add/Remove Favorite", "Play in Mini Player")
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(stream.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> toggleFavorite(stream)
                    1 -> {
                        val prefs = MainActivity.prefsInstance
                        val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${stream.streamId}.ts"
                        (activity as? MainActivity)?.startMiniPlayer(streamUrl, stream.name)
                    }
                }
            }
            .show()
    }

    private fun toggleFavorite(stream: LiveStream) {
        val dao = AppDatabase.getInstance(requireContext()).favoriteDao()
        viewLifecycleOwner.lifecycleScope.launch {
            val isFav = withContext(Dispatchers.IO) {
                dao.isFavorite(stream.streamId, "live")
            }
            withContext(Dispatchers.IO) {
                if (isFav) {
                    dao.delete(stream.streamId, "live")
                } else {
                    dao.insert(
                        FavoriteEntity(
                            streamId = stream.streamId,
                            name = stream.name,
                            icon = stream.streamIcon,
                            type = "live",
                            categoryId = stream.categoryId,
                            containerExtension = null
                        )
                    )
                }
            }
            requireContext().toast(
                getString(if (isFav) R.string.fav_removed else R.string.fav_added)
            )
            // Refresh if in favorites mode
            if (isFavoritesMode) loadFavorites()
        }
    }

    private fun loadFavorites() {
        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val dao = AppDatabase.getInstance(requireContext()).favoriteDao()
                val favs = withContext(Dispatchers.IO) { dao.getByType("live") }

                if (favs.isEmpty()) {
                    progressBar.gone()
                    tvEmpty.text = "No favorites yet. Long-press a channel to add it."
                    tvEmpty.visible()
                    channelListAdapter.submitList(emptyList())
                    return@launch
                }

                // Load all live streams and filter to favorites
                val allLive = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getLiveStreams(prefs.username, prefs.password)
                }
                val favIds = favs.map { it.streamId }.toSet()
                val favStreams = allLive.filter { it.streamId in favIds }

                allStreams = favStreams
                channelListAdapter.submitList(favStreams)
                progressBar.gone()

                if (favStreams.isEmpty()) {
                    tvEmpty.text = "No favorites found"
                    tvEmpty.visible()
                } else {
                    tvEmpty.gone()
                    startPreview(favStreams[0])
                }
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = "Error: ${e.message}"
                tvEmpty.visible()
            }
        }
    }

    private fun showSearchMode() {
        etSearchBar.visible()
        etSearchBar.setText("")
        etSearchBar.requestFocus()

        // Load all channels for searching
        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (allLiveCache.isEmpty()) {
                    allLiveCache = withContext(Dispatchers.IO) {
                        MainActivity.apiService.getLiveStreams(prefs.username, prefs.password)
                    }
                }
                progressBar.gone()
                allStreams = allLiveCache
                channelListAdapter.submitList(allLiveCache)
                tvEmpty.gone()
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = "Error loading channels"
                tvEmpty.visible()
            }
        }

        if (!searchWatcherAdded) {
            searchWatcherAdded = true
            etSearchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val query = s?.toString()?.lowercase() ?: ""
                    if (query.length < 2) {
                        channelListAdapter.submitList(allLiveCache)
                        allStreams = allLiveCache
                    } else {
                        val filtered = allLiveCache.filter { it.name.lowercase().contains(query) }
                        channelListAdapter.submitList(filtered)
                        allStreams = filtered
                        if (filtered.isEmpty()) {
                            tvEmpty.text = "No channels found for \"$query\""
                            tvEmpty.visible()
                        } else {
                            tvEmpty.gone()
                        }
                    }
                }
            })
        }
    }

    private fun hideSearchBar() {
        etSearchBar.gone()
        etSearchBar.setText("")
    }

    private fun startPreview(stream: LiveStream) {
        if (stream.streamId == currentPreviewStreamId) return
        currentPreviewStreamId = stream.streamId

        tvPreviewChannelName.text = stream.name
        tvPreviewEpgNow.text = ""
        tvPreviewEpgNext.text = ""
        tvPreviewResolution.text = ""
        tvPreviewAudio.text = ""

        val prefs = MainActivity.prefsInstance
        val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${stream.streamId}.ts"

        previewPlayer?.release()
        previewLoading.visible()

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(10_000, 30_000, 2_000, 5_000)
            .build()

        val defaultHeaders = mapOf(
            "Connection" to "keep-alive",
            "Accept" to "*/*"
        )
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setConnectTimeoutMs(15_000)
            .setReadTimeoutMs(30_000)
            .setAllowCrossProtocolRedirects(true)
            .setUserAgent("Lavf/60.3.100")
            .setDefaultRequestProperties(defaultHeaders)

        val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory)

        previewPlayer = ExoPlayer.Builder(requireContext())
            .setLoadControl(loadControl)
            .setMediaSourceFactory(mediaSourceFactory)
            .build().also { player ->
                previewPlayerView.player = player
                player.volume = 1f
                player.setWakeMode(C.WAKE_MODE_NETWORK)

                // Force max quality for preview too
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .setMaxVideoSize(Int.MAX_VALUE, Int.MAX_VALUE)
                    .setForceHighestSupportedBitrate(true)
                    .build()

                player.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_READY) {
                            previewLoading.gone()
                            updateStreamInfo(player)
                        } else if (state == Player.STATE_ENDED) {
                            player.clearMediaItems()
                            player.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
                            player.prepare()
                            player.playWhenReady = true
                        }
                    }
                    override fun onPlayerError(error: PlaybackException) {
                        previewLoading.gone()
                        android.util.Log.e("VeltrixTV", "Preview error", error)
                    }
                })

                player.setMediaItem(MediaItem.fromUri(Uri.parse(streamUrl)))
                player.prepare()
                player.playWhenReady = true
            }

        loadEpg(stream)
    }

    private fun updateStreamInfo(player: ExoPlayer) {
        try {
            val videoFormat = player.videoFormat
            val audioFormat = player.audioFormat

            if (videoFormat != null) {
                val w = videoFormat.width
                val h = videoFormat.height
                val label = when {
                    h >= 2160 -> "4K  ${w}×${h}"
                    h >= 1080 -> "FHD  ${w}×${h}"
                    h >= 720 -> "HD  ${w}×${h}"
                    h >= 480 -> "SD  ${w}×${h}"
                    w > 0 && h > 0 -> "${w}×${h}"
                    else -> null
                }
                if (label != null) {
                    tvPreviewResolution.text = label
                    tvPreviewResolution.visible()
                }
            }

            if (audioFormat != null) {
                val lang = audioFormat.language?.uppercase() ?: ""
                val channels = when (audioFormat.channelCount) {
                    1 -> "Mono"
                    2 -> "Stereo"
                    6 -> "5.1"
                    8 -> "7.1"
                    else -> "${audioFormat.channelCount}ch"
                }
                tvPreviewAudio.text = if (lang.isNotEmpty()) "$lang $channels" else channels
                tvPreviewAudio.visible()
            }
        } catch (_: Exception) {}
    }

    private fun loadEpg(stream: LiveStream) {
        val prefs = MainActivity.prefsInstance

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val epgData = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getShortEpg(
                        prefs.username, prefs.password,
                        streamId = stream.streamId
                    )
                }
                val listings = epgData.epgListings
                if (listings.isNotEmpty()) {
                    val now = listings.getOrNull(0)
                    val next = listings.getOrNull(1)
                    if (now != null) {
                        tvPreviewEpgNow.text = "Now: ${now.title}"
                        tvPreviewEpgNow.visible()
                    }
                    if (next != null) {
                        tvPreviewEpgNext.text = "Next: ${next.title}"
                        tvPreviewEpgNext.visible()
                    }
                }
            } catch (e: Exception) {
                android.util.Log.d("VeltrixTV", "EPG not available for ${stream.name}: ${e.message}")
            }
        }
    }

    private fun loadCategories() {
        val prefs = MainActivity.prefsInstance
        progressBar.visible()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val categories = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getLiveCategories(prefs.username, prefs.password)
                }

                // Build category list: Favorites, Search, All, then server categories
                val favCategory = Category("favorites", "Favorites", 0)
                val searchCategory = Category("search", "Search", 0)
                val allCategory = Category("0", getString(R.string.all_categories), 0)
                val fullList = listOf(favCategory, searchCategory, allCategory) + categories
                categoryAdapter.submitList(fullList)

                // Select first real category (skip Favorites, Search and All)
                val firstCategoryId = if (categories.isNotEmpty()) categories[0].categoryId else "0"
                categoryAdapter.setSelected(if (categories.isNotEmpty()) 3 else 2)
                loadStreams(firstCategoryId)
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "loadCategories error", e)
                progressBar.gone()
                tvEmpty.text = "Error: ${e.message}"
                tvEmpty.visible()
            }
        }
    }

    private fun loadStreams(categoryId: String) {
        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val streams = withContext(Dispatchers.IO) {
                    if (categoryId == "0") {
                        MainActivity.apiService.getLiveStreams(prefs.username, prefs.password)
                    } else {
                        MainActivity.apiService.getLiveStreams(
                            prefs.username, prefs.password,
                            categoryId = categoryId
                        )
                    }
                }

                if (streams.isEmpty() && categoryId != "0") {
                    android.util.Log.d("VeltrixTV", "Category $categoryId empty, loading all live")
                    val allLive = withContext(Dispatchers.IO) {
                        MainActivity.apiService.getLiveStreams(prefs.username, prefs.password)
                    }
                    val limited = if (allLive.size > 500) allLive.take(500) else allLive
                    allStreams = limited
                    channelListAdapter.submitList(limited)
                    progressBar.gone()
                    if (limited.isEmpty()) {
                        tvEmpty.text = "No channels found"
                        tvEmpty.visible()
                    } else {
                        tvEmpty.gone()
                        categoryAdapter.setSelected(1) // select "All"
                    }
                    return@launch
                }

                allStreams = streams
                channelListAdapter.submitList(streams)
                progressBar.gone()

                if (streams.isEmpty()) {
                    tvEmpty.text = "No channels found"
                    tvEmpty.visible()
                } else {
                    tvEmpty.gone()
                    if (streams.isNotEmpty()) {
                        startPreview(streams[0])
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "loadStreams error cat=$categoryId", e)
                progressBar.gone()
                tvEmpty.text = "Error: ${e.message}"
                tvEmpty.visible()
            }
        }
    }

    private fun openPlayer(stream: LiveStream, position: Int) {
        try {
            val prefs = MainActivity.prefsInstance
            val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${stream.streamId}.ts"

            ChannelListHolder.set(allStreams)

            previewPlayer?.release()
            previewPlayer = null
            (activity as? MainActivity)?.closeMiniPlayer()

            val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, stream.name)
                putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Live TV")
                putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "live")
                putExtra(PlayerActivity.EXTRA_CURRENT_INDEX, position)
                putExtra(PlayerActivity.EXTRA_STREAM_ICON, stream.streamIcon)
            }
            startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("VeltrixTV", "openPlayer error", e)
            requireContext().toast("Error opening player: ${e.message}")
        }
    }

    override fun onPause() {
        super.onPause()
        previewPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        previewPlayer?.play()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        previewPlayer?.release()
        previewPlayer = null
        allLiveCache = emptyList()
    }
}
