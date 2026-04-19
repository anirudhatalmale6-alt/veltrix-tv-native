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
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.data.models.SeriesItem
import com.veltrix.tv.data.models.VodStream
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.ui.series.SeriesDetailActivity
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.loadImage
import com.veltrix.tv.util.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private lateinit var etSearch: EditText
    private lateinit var rvResults: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var tabAll: TextView
    private lateinit var tabLive: TextView
    private lateinit var tabMovies: TextView
    private lateinit var tabSeries: TextView

    private var allLive = listOf<LiveStream>()
    private var allVod = listOf<VodStream>()
    private var allSeries = listOf<SeriesItem>()
    private var dataLoaded = false
    private var searchJob: Job? = null
    private var currentFilter = "all"

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
        loadAllData()
    }

    private fun setupTabs() {
        val tabs = listOf(tabAll, tabLive, tabMovies, tabSeries)
        val filters = listOf("all", "live", "vod", "series")

        tabs.forEachIndexed { index, tab ->
            tab.setOnClickListener {
                currentFilter = filters[index]
                updateTabUI(tabs, index)
                performSearch(etSearch.text.toString())
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
        val filters = listOf("all", "live", "vod", "series")
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
                    delay(300) // debounce
                    performSearch(s?.toString() ?: "")
                }
            }
        })
    }

    private fun loadAllData() {
        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val live = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getLiveStreams(prefs.username, prefs.password)
                }
                val vod = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getVodStreams(prefs.username, prefs.password)
                }
                val series = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getSeries(prefs.username, prefs.password)
                }

                allLive = live
                allVod = vod
                allSeries = series
                dataLoaded = true
                progressBar.gone()
                tvEmpty.text = "Type to search ${live.size + vod.size + series.size} items..."
                tvEmpty.visible()
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = "Error loading data"
                tvEmpty.visible()
            }
        }
    }

    private fun performSearch(query: String) {
        if (!dataLoaded) return
        if (query.length < 2) {
            rvResults.adapter = null
            tvEmpty.text = "Type to search..."
            tvEmpty.visible()
            return
        }

        val q = query.lowercase()
        val results = mutableListOf<SearchResult>()

        if (currentFilter == "all" || currentFilter == "live") {
            allLive.filter { it.name.lowercase().contains(q) }
                .take(50)
                .mapTo(results) {
                    SearchResult(it.name, it.streamIcon, "Live", streamId = it.streamId, type = "live")
                }
        }

        if (currentFilter == "all" || currentFilter == "vod") {
            allVod.filter { it.name.lowercase().contains(q) }
                .take(50)
                .mapTo(results) {
                    SearchResult(it.name, it.streamIcon, "Movie", streamId = it.streamId, type = "vod",
                        containerExtension = it.containerExtension)
                }
        }

        if (currentFilter == "all" || currentFilter == "series") {
            allSeries.filter { it.name.lowercase().contains(q) }
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
                    val pos = adapterPosition
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
}
