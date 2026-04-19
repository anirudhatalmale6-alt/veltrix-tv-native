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
                    delay(500) // debounce
                    performSearch(s?.toString() ?: "")
                }
            }
        })
    }

    private fun performSearch(query: String) {
        if (query.length < 2) {
            rvResults.adapter = null
            tvEmpty.text = "Type to search..."
            tvEmpty.visible()
            progressBar.gone()
            return
        }

        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            try {
                val results = mutableListOf<SearchResult>()
                val q = query.lowercase()

                // Search live channels (load one category at a time, search by name)
                if (currentFilter == "all" || currentFilter == "live") {
                    try {
                        val categories = withContext(Dispatchers.IO) {
                            MainActivity.apiService.getLiveCategories(prefs.username, prefs.password)
                        }
                        // Search through first 10 categories max to keep it fast
                        val categoriesToSearch = categories.take(10)
                        for (cat in categoriesToSearch) {
                            if (results.size >= 30) break
                            try {
                                val streams = withContext(Dispatchers.IO) {
                                    MainActivity.apiService.getLiveStreams(
                                        prefs.username, prefs.password,
                                        categoryId = cat.categoryId
                                    )
                                }
                                streams.filter { it.name.lowercase().contains(q) }
                                    .take(10)
                                    .mapTo(results) {
                                        SearchResult(it.name, it.streamIcon, "Live", streamId = it.streamId, type = "live")
                                    }
                            } catch (_: Exception) {}
                        }
                    } catch (_: Exception) {}
                }

                // Search movies
                if (currentFilter == "all" || currentFilter == "vod") {
                    try {
                        val categories = withContext(Dispatchers.IO) {
                            MainActivity.apiService.getVodCategories(prefs.username, prefs.password)
                        }
                        val categoriesToSearch = categories.take(10)
                        for (cat in categoriesToSearch) {
                            if (results.size >= 50) break
                            try {
                                val streams = withContext(Dispatchers.IO) {
                                    MainActivity.apiService.getVodStreams(
                                        prefs.username, prefs.password,
                                        categoryId = cat.categoryId
                                    )
                                }
                                streams.filter { it.name.lowercase().contains(q) }
                                    .take(10)
                                    .mapTo(results) {
                                        SearchResult(it.name, it.streamIcon, "Movie", streamId = it.streamId, type = "vod",
                                            containerExtension = it.containerExtension)
                                    }
                            } catch (_: Exception) {}
                        }
                    } catch (_: Exception) {}
                }

                // Search series
                if (currentFilter == "all" || currentFilter == "series") {
                    try {
                        val categories = withContext(Dispatchers.IO) {
                            MainActivity.apiService.getSeriesCategories(prefs.username, prefs.password)
                        }
                        val categoriesToSearch = categories.take(10)
                        for (cat in categoriesToSearch) {
                            if (results.size >= 70) break
                            try {
                                val series = withContext(Dispatchers.IO) {
                                    MainActivity.apiService.getSeries(
                                        prefs.username, prefs.password,
                                        categoryId = cat.categoryId
                                    )
                                }
                                series.filter { it.name.lowercase().contains(q) }
                                    .take(10)
                                    .mapTo(results) {
                                        SearchResult(it.name, it.cover, "Series", seriesId = it.seriesId, type = "series")
                                    }
                            } catch (_: Exception) {}
                        }
                    } catch (_: Exception) {}
                }

                progressBar.gone()

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
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = "Search error"
                tvEmpty.visible()
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
