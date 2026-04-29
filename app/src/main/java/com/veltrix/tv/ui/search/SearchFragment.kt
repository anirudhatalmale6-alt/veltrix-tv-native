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
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.SearchIndexDao
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
    private var loadJob: Job? = null
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
        startDataLoad()
    }

    private fun startDataLoad() {
        if (SearchDataCache.isLoaded) {
            progressBar.gone()
            updateStatusText()
            return
        }

        if (SearchDataCache.isLoading) {
            progressBar.visible()
            tvEmpty.text = "Building search index..."
            tvEmpty.visible()
            loadJob = viewLifecycleOwner.lifecycleScope.launch {
                while (SearchDataCache.isLoading && !SearchDataCache.isLoaded) {
                    delay(1000)
                    if (!isAdded) return@launch
                    val dao = AppDatabase.getInstance(requireContext()).searchIndexDao()
                    val c = withContext(Dispatchers.IO) { dao.countSync() }
                    if (c > 0) {
                        tvEmpty.text = "Indexed $c items so far... You can search now"
                        SearchDataCache.isLoaded = true
                    }
                }
                if (!isAdded) return@launch
                progressBar.gone()
                updateStatusText()
            }
            return
        }

        progressBar.visible()
        tvEmpty.text = "Building search index..."
        tvEmpty.visible()

        val activity = requireActivity() as? MainActivity ?: return
        val dao = AppDatabase.getInstance(requireContext()).searchIndexDao()
        val client = MainActivity.createHttpClient(30, 300)
        val baseUrl = MainActivity.prefsInstance.getBaseUrl().trimEnd('/')

        SearchDataCache.isLoading = true

        loadJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                dao.deleteAllSync()

                SearchDataCache.liveCount = activity.streamParseAndInsert(
                    client, baseUrl, "get_live_streams", "live", dao
                )
                withContext(Dispatchers.Main) {
                    if (!isAdded) return@withContext
                    SearchDataCache.isLoaded = true
                    tvEmpty.text = "Indexed ${SearchDataCache.liveCount} channels. Loading movies..."
                }

                SearchDataCache.vodCount = activity.streamParseAndInsert(
                    client, baseUrl, "get_vod_streams", "vod", dao
                )
                withContext(Dispatchers.Main) {
                    if (!isAdded) return@withContext
                    tvEmpty.text = "Indexed ${SearchDataCache.liveCount + SearchDataCache.vodCount} items. Loading series..."
                }

                SearchDataCache.seriesCount = activity.streamParseAndInsert(
                    client, baseUrl, "get_series", "series", dao
                )

                SearchDataCache.isLoading = false
                withContext(Dispatchers.Main) {
                    if (!isAdded) return@withContext
                    progressBar.gone()
                    updateStatusText()
                }
            } catch (e: Exception) {
                SearchDataCache.isLoading = false
                val c = try { dao.countSync() } catch (_: Exception) { 0 }
                if (c > 0) SearchDataCache.isLoaded = true
                withContext(Dispatchers.Main) {
                    if (!isAdded) return@withContext
                    progressBar.gone()
                    if (c > 0) {
                        updateStatusText()
                    } else {
                        tvEmpty.text = "Error loading data. Try again later."
                        tvEmpty.visible()
                    }
                }
            }
        }
    }

    private fun updateStatusText() {
        if (!isAdded) return
        val live = SearchDataCache.liveCount
        val vod = SearchDataCache.vodCount
        val series = SearchDataCache.seriesCount
        val total = live + vod + series
        if (total == 0) {
            tvEmpty.text = "Building search index..."
        } else {
            tvEmpty.text = "Search $live channels, $vod movies, $series series"
        }
        tvEmpty.visible()
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
            updateStatusText()
            return
        }

        if (!SearchDataCache.isLoaded) {
            tvEmpty.text = "Building search index, please wait..."
            tvEmpty.visible()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val dao = AppDatabase.getInstance(requireContext()).searchIndexDao()
            val q = query.lowercase()

            val dbResults = withContext(Dispatchers.IO) {
                try {
                    if (currentFilter == "all") {
                        dao.searchAll(q, 150)
                    } else {
                        dao.searchByType(currentFilter, q, 50)
                    }
                } catch (_: Exception) { emptyList() }
            }

            if (!isAdded) return@launch

            val results = dbResults.map { entity ->
                SearchResult(
                    name = entity.name,
                    icon = entity.icon,
                    typeLabel = when (entity.type) {
                        "live" -> "Live"
                        "vod" -> "Movie"
                        "series" -> "Series"
                        else -> entity.type
                    },
                    streamId = entity.streamId,
                    seriesId = if (entity.type == "series") entity.seriesId else null,
                    type = entity.type,
                    containerExtension = entity.containerExtension
                )
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
    }

    private fun openResult(result: SearchResult) {
        val prefs = MainActivity.prefsInstance
        when (result.type) {
            "live" -> {
                val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${result.streamId}.m3u8"
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
