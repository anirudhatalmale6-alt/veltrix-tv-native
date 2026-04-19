package com.veltrix.tv.ui.live

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
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

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvChannels: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var channelAdapter: ChannelAdapter

    private var allStreams = listOf<LiveStream>()

    override fun canGoLeft(): Boolean {
        // If focus is in the channels grid, LEFT should go to categories first
        val focused = activity?.currentFocus ?: return false
        return rvChannels.isAncestorOf(focused)
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

        rvCategories = view.findViewById(R.id.rvCategories)
        rvChannels = view.findViewById(R.id.rvChannels)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        setupAdapters()
        loadCategories()
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter { category ->
            loadStreams(category.categoryId)
        }
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = categoryAdapter

        channelAdapter = ChannelAdapter(
            onChannelClick = { stream, position ->
                openPlayer(stream, position)
            },
            onChannelLongClick = { stream ->
                toggleFavorite(stream)
            }
        )
        val columns = calculateGridColumns()
        rvChannels.layoutManager = GridLayoutManager(requireContext(), columns)
        rvChannels.adapter = channelAdapter
    }

    private fun calculateGridColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        // Subtract sidebar (260dp) and category (200dp) widths, then fit cards (180dp each)
        val availableWidth = screenWidthDp - 260 - 200 - 24 // padding
        return (availableWidth / 192).toInt().coerceIn(3, 7) // 180dp card + 12dp margin
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

                val allCategory = Category("0", getString(R.string.all_categories), 0)
                val fullList = listOf(allCategory) + categories
                categoryAdapter.submitList(fullList)

                loadStreams("0")
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = getString(R.string.error_loading)
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

                allStreams = streams
                channelAdapter.submitList(streams)
                progressBar.gone()

                if (streams.isEmpty()) {
                    tvEmpty.visible()
                } else {
                    tvEmpty.gone()
                }
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = getString(R.string.error_loading)
                tvEmpty.visible()
            }
        }
    }

    private fun openPlayer(stream: LiveStream, position: Int) {
        val prefs = MainActivity.prefsInstance
        val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${stream.streamId}.ts"

        val streamIds = allStreams.map { it.streamId }.toIntArray()
        val streamNames = allStreams.map { it.name }.toTypedArray()

        val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
            putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, stream.name)
            putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Live TV")
            putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "live")
            putExtra(PlayerActivity.EXTRA_STREAM_IDS, streamIds)
            putExtra(PlayerActivity.EXTRA_STREAM_NAMES, streamNames)
            putExtra(PlayerActivity.EXTRA_CURRENT_INDEX, position)
        }
        startActivity(intent)
    }
}
