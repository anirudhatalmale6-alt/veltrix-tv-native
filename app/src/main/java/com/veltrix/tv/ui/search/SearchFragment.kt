package com.veltrix.tv.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.SearchDataCache
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.data.models.VodStream
import com.veltrix.tv.data.models.SeriesItem
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.ui.series.SeriesDetailActivity
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.loadImage
import com.veltrix.tv.util.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class SearchFragment : Fragment() {

    private lateinit var etSearch: EditText
    private lateinit var rvResults: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var tabAll: TextView
    private lateinit var tabLive: TextView
    private lateinit var tabMovies: TextView
    private lateinit var tabSeries: TextView

    private var searchJob: Job? = null
    private var loadJob: Job? = null
    private var currentFilter = "all"

    // Cached data for searching
    private var cachedLive = listOf<LiveStream>()
    private var cachedVod = listOf<VodStream>()
    private var cachedSeries = listOf<SeriesItem>()
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearch = view.findViewById(R.id.etSearch)
        rvResults = view.findViewById(R.id.rvResults)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        tabAll = view.findViewById(R.id.tabAll)
        tabLive = view.findViewById(R.id.tabLive)
        tabMovies = view.findViewById(R.id.tabMovies)
        tabSeries = view.findViewById(R.id.tabSeries)

        rvResults.layoutManager = LinearLayoutManager(requireContext())

        setupTabs()
        setupSearch()
        preloadData()
    }

    private fun preloadData() {
        // Use global cache that was preloaded at app startup
        if (SearchDataCache.isLoaded) {
            cachedLive = SearchDataCache.liveStreams
            cachedVod = SearchDataCache.vodStreams
            cachedSeries = SearchDataCache.seriesItems

            // If cache has gaps (vod or series empty), reload those in background
            if (cachedVod.isEmpty() || cachedSeries.isEmpty()) {
                isDataLoaded = true
                progressBar.gone()
                tvEmpty.text = "Search ${cachedLive.size} channels, loading movies & series..."
                tvEmpty.visible()
                loadMissingData()
                return
            }

            isDataLoaded = true
            progressBar.gone()
            tvEmpty.text = "Search ${cachedLive.size} channels, ${cachedVod.size} movies, ${cachedSeries.size} series"
            tvEmpty.visible()
            return
        }

        // Cache not ready yet - wait for it
        progressBar.visible()
        tvEmpty.gone()

        loadJob = viewLifecycleOwner.lifecycleScope.launch {
            // Poll until global cache is loaded (checks every 500ms, max 60s)
            var waited = 0
            while (!SearchDataCache.isLoaded && waited < 60_000) {
                delay(500)
                waited += 500
            }

            if (SearchDataCache.isLoaded) {
                cachedLive = SearchDataCache.liveStreams
                cachedVod = SearchDataCache.vodStreams
                cachedSeries = SearchDataCache.seriesItems
                isDataLoaded = true
            } else {
                // Fallback: load directly if global cache failed
                val prefs = MainActivity.prefsInstance
                try {
                    val liveJob = async(Dispatchers.IO) {
                        withTimeoutOrNull(30_000) {
                            try { MainActivity.apiService.getLiveStreams(prefs.username, prefs.password) }
                            catch (_: Exception) { null }
                        } ?: emptyList()
                    }
                    val vodJob = async(Dispatchers.IO) {
                        withTimeoutOrNull(30_000) {
                            try { MainActivity.apiService.getVodStreams(prefs.username, prefs.password) }
                            catch (_: Exception) { null }
                        } ?: emptyList()
                    }
                    val seriesJob = async(Dispatchers.IO) {
                        withTimeoutOrNull(30_000) {
                            try { MainActivity.apiService.getSeries(prefs.username, prefs.password) }
                            catch (_: Exception) { null }
                        } ?: emptyList()
                    }
                    cachedLive = liveJob.await()
                    cachedVod = vodJob.await()
                    cachedSeries = seriesJob.await()
                    isDataLoaded = true
                } catch (_: Exception) {
                    isDataLoaded = true
                }
            }

            if (!isAdded) return@launch
            progressBar.gone()
            if (cachedLive.isEmpty() && cachedVod.isEmpty() && cachedSeries.isEmpty()) {
                tvEmpty.text = "Error loading data. Try again later."
            } else {
                tvEmpty.text = "Search ${cachedLive.size} channels, ${cachedVod.size} movies, ${cachedSeries.size} series"
            }
            tvEmpty.visible()
        }
    }

    private fun loadMissingData() {
        val prefs = MainActivity.prefsInstance
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (cachedVod.isEmpty()) {
                    val vod = async(Dispatchers.IO) {
                        withTimeoutOrNull(90_000) {
                            try { MainActivity.apiService.getVodStreams(prefs.username, prefs.password) }
                            catch (e: Exception) {
                                android.util.Log.e("VeltrixTV", "Search VOD reload error", e)
                                null
                            }
                        } ?: emptyList()
                    }
                    cachedVod = vod.await()
                    SearchDataCache.vodStreams = cachedVod
                    android.util.Log.d("VeltrixTV", "Search VOD reloaded: ${cachedVod.size}")
                }
                if (cachedSeries.isEmpty()) {
                    val series = async(Dispatchers.IO) {
                        withTimeoutOrNull(90_000) {
                            try { MainActivity.apiService.getSeries(prefs.username, prefs.password) }
                            catch (e: Exception) {
                                android.util.Log.e("VeltrixTV", "Search series reload error", e)
                                null
                            }
                        } ?: emptyList()
                    }
                    cachedSeries = series.await()
                    SearchDataCache.seriesItems = cachedSeries
                    android.util.Log.d("VeltrixTV", "Search series reloaded: ${cachedSeries.size}")
                }
            } catch (_: Exception) {}

            if (!isAdded) return@launch
            tvEmpty.text = "Search ${cachedLive.size} channels, ${cachedVod.size} movies, ${cachedSeries.size} series"
            tvEmpty.visible()
        }
    }

    private fun setupTabs() {
        val tabs = listOf(tabAll, tabLive, tabMovies, tabSeries)
        val filters = listOf("all", "live", "vod", "series")

        tabs.forEachIndexed { index, tab ->
            tab.setOnClickListener {
                currentFilter = filters[index]
                updateTabUI(tabs, index)
                val query = etSearch.text.toString()
                if (query.length >= 2) {
                    performSearch(query)
                }
            }
            tab.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    (v as TextView).setTextColor(resources.getColor(R.color.white, null))
                } else if (filters[index] != currentFilter) {
                    (v as TextView).setTextColor(resources.getColor(R.color.text_secondary, null))
                }
            }
        }
    }

    private fun updateTabUI(tabs: List<TextView>, selectedIndex: Int) {
        tabs.forEachIndexed { index, tab ->
            if (index == selectedIndex) {
                tab.setTextColor(resources.getColor(R.color.red, null))
                tab.setBackgroundResource(R.drawable.bg_tab_selected)
            } else {
                tab.setTextColor(resources.getColor(R.color.text_secondary, null))
                tab.setBackgroundResource(R.drawable.bg_tab_normal)
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    performSearch(s?.toString() ?: "")
                }
            }
        })
    }

    private fun performSearch(query: String) {
        if (query.length < 2) {
            rvResults.adapter = null
            if (isDataLoaded) {
                tvEmpty.text = "Search ${cachedLive.size} channels, ${cachedVod.size} movies, ${cachedSeries.size} series"
            } else {
                tvEmpty.text = "Loading data..."
            }
            tvEmpty.visible()
            return
        }

        if (!isDataLoaded) {
            tvEmpty.text = "Still loading data, please wait..."
            tvEmpty.visible()
            return
        }

        val q = query.lowercase()
        val results = mutableListOf<SearchResult>()

        // Search live channels
        if (currentFilter == "all" || currentFilter == "live") {
            cachedLive.filter { it.name.lowercase().contains(q) }
                .take(50)
                .mapTo(results) {
                    SearchResult(it.name, it.streamIcon, "Live", streamId = it.streamId, type = "live")
                }
        }

        // Search movies
        if (currentFilter == "all" || currentFilter == "vod") {
            cachedVod.filter { it.name.lowercase().contains(q) }
                .take(50)
                .mapTo(results) {
                    SearchResult(it.name, it.streamIcon, "Movie", streamId = it.streamId, type = "vod",
                        containerExtension = it.containerExtension)
                }
        }

        // Search series
        if (currentFilter == "all" || currentFilter == "series") {
            cachedSeries.filter { it.name.lowercase().contains(q) }
                .take(50)
                .mapTo(results) {
                    SearchResult(it.name, it.cover, "Series", seriesId = it.seriesId, type = "series")
                }
        }

        if (results.isEmpty()) {
            tvEmpty.text = "No results for \"$query\""
            tvEmpty.visible()
            rvResults.adapter = null
        } else {
            tvEmpty.gone()
            rvResults.adapter = SearchResultAdapter(results) { result ->
                openResult(result)
            }
        }
    }

    private fun openResult(result: SearchResult) {
        val prefs = MainActivity.prefsInstance
        when (result.type) {
            "live" -> {
                val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${result.streamId}.ts"
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, result.name)
                    putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Search")
                    putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "live")
                }
                startActivity(intent)
            }
            "vod" -> {
                val ext = result.containerExtension ?: "mp4"
                val streamUrl = "${prefs.getBaseUrl()}/movie/${prefs.username}/${prefs.password}/${result.streamId}.$ext"
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, result.name)
                    putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Search")
                    putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "vod")
                }
                startActivity(intent)
            }
            "series" -> {
                val intent = Intent(requireContext(), SeriesDetailActivity::class.java).apply {
                    putExtra(SeriesDetailActivity.EXTRA_SERIES_ID, result.seriesId ?: 0)
                    putExtra(SeriesDetailActivity.EXTRA_SERIES_NAME, result.name)
                    putExtra(SeriesDetailActivity.EXTRA_SERIES_COVER, result.icon)
                }
                startActivity(intent)
            }
        }
    }

    data class SearchResult(
        val name: String,
        val icon: String?,
        val typeLabel: String,
        val streamId: Int = 0,
        val seriesId: Int? = null,
        val type: String,
        val containerExtension: String? = null
    )

    inner class SearchResultAdapter(
        private val items: List<SearchResult>,
        private val onClick: (SearchResult) -> Unit
    ) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvType: TextView = itemView.findViewById(R.id.tvType)

            init {
                FocusHighlightHelper.setupFocusHighlight(itemView)
                itemView.setOnClickListener {
                    val pos = bindingAdapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onClick(items[pos])
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name
            holder.tvType.text = item.typeLabel
            holder.ivIcon.loadImage(item.icon)
        }

        override fun getItemCount(): Int = items.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadJob?.cancel()
        searchJob?.cancel()
    }
}
