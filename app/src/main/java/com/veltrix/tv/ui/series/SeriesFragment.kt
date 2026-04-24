package com.veltrix.tv.ui.series

import android.animation.ValueAnimator
import android.content.Intent
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.models.Category
import com.veltrix.tv.data.models.SeriesItem
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.FavoriteEntity
import com.veltrix.tv.ui.live.CategoryAdapter
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.visible
import com.veltrix.tv.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeriesFragment : Fragment(), MainActivity.DpadNavigable {

    private lateinit var categoryContainer: LinearLayout
    private lateinit var rvCategories: RecyclerView
    private lateinit var rvSeries: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var etSearchBar: EditText
    private var isCategoryVisible = true
    private var categoryWidth = 0
    private var allSeriesCache = listOf<SeriesItem>()
    private var searchWatcherAdded = false

    override fun getMainContentView(): View = rvSeries

    override fun canGoLeft(): Boolean {
        val focused = activity?.currentFocus ?: return false
        if (!rvSeries.isAncestorOf(focused)) return false

        val vh = rvSeries.findContainingViewHolder(focused)
        if (vh != null) {
            val pos = vh.adapterPosition
            val lm = rvSeries.layoutManager as? GridLayoutManager
            if (lm != null && pos >= 0) {
                val column = pos % lm.spanCount
                if (column > 0) {
                    lm.findViewByPosition(pos - 1)?.requestFocus()
                    return true
                }
            }
        }

        if (!isCategoryVisible) expandCategories()
        rvCategories.post {
            val sel = rvCategories.findViewHolderForAdapterPosition(categoryAdapter.selectedPosition)
            (sel?.itemView ?: rvCategories.getChildAt(0))?.requestFocus()
        }
        return true
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
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryContainer = view.findViewById(R.id.categoryContainer)
        rvCategories = view.findViewById(R.id.rvCategories)
        rvSeries = view.findViewById(R.id.rvSeries)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
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

        rvSeries.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && isCategoryVisible) collapseCategories()
        }
        rvSeries.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
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
                val adapterListener = view.onFocusChangeListener
                view.setOnFocusChangeListener { v, hasFocus ->
                    adapterListener?.onFocusChange(v, hasFocus)
                    if (hasFocus && !isCategoryVisible) expandCategories()
                }
                view.setOnKeyListener { _, keyCode, event ->
                    if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT && event.action == android.view.KeyEvent.ACTION_DOWN) {
                        focusPosterGrid()
                        true
                    } else false
                }
            }
            override fun onChildViewDetachedFromWindow(view: View) {}
        })
    }

    private fun focusPosterGrid() {
        val lm = rvSeries.layoutManager as? GridLayoutManager
        val firstVisible = lm?.findFirstVisibleItemPosition() ?: 0
        val vh = rvSeries.findViewHolderForAdapterPosition(firstVisible)
        vh?.itemView?.requestFocus() ?: rvSeries.getChildAt(0)?.requestFocus()
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
                    hideSearchBar()
                    loadSeriesFavorites()
                }
                "search" -> {
                    showSearchMode()
                }
                else -> {
                    hideSearchBar()
                    loadSeries(category.categoryId)
                }
            }
        }
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = categoryAdapter

        seriesAdapter = SeriesAdapter(
            onSeriesClick = { series ->
                try {
                    val intent = Intent(requireContext(), SeriesDetailActivity::class.java).apply {
                        putExtra(SeriesDetailActivity.EXTRA_SERIES_ID, series.seriesId)
                        putExtra(SeriesDetailActivity.EXTRA_SERIES_NAME, series.name)
                        putExtra(SeriesDetailActivity.EXTRA_SERIES_COVER, series.cover)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    android.util.Log.e("VeltrixTV", "openSeries error", e)
                }
            },
            onSeriesLongClick = { series ->
                toggleFavorite(series)
            }
        )
        val columns = calculateGridColumns()
        rvSeries.layoutManager = GridLayoutManager(requireContext(), columns)
        rvSeries.adapter = seriesAdapter
        rvSeries.setHasFixedSize(true)
        rvSeries.setItemViewCacheSize(20)
    }

    private fun calculateGridColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val availableWidth = screenWidthDp - 260 - 24
        return (availableWidth / 162).toInt().coerceIn(3, 8)
    }

    private fun toggleFavorite(series: SeriesItem) {
        val dao = AppDatabase.getInstance(requireContext()).favoriteDao()
        viewLifecycleOwner.lifecycleScope.launch {
            val isFav = withContext(Dispatchers.IO) {
                dao.isFavorite(series.seriesId, "series")
            }
            withContext(Dispatchers.IO) {
                if (isFav) {
                    dao.delete(series.seriesId, "series")
                } else {
                    dao.insert(
                        FavoriteEntity(
                            streamId = series.seriesId,
                            name = series.name,
                            icon = series.cover,
                            type = "series",
                            categoryId = series.categoryId,
                            containerExtension = null,
                            seriesId = series.seriesId
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
                    MainActivity.apiService.getSeriesCategories(prefs.username, prefs.password)
                }

                val favCategory = Category("favorites", "Favorites", 0)
                val searchCategory = Category("search", "Search", 0)
                val allCategory = Category("0", getString(R.string.all_categories), 0)
                val fullList = listOf(favCategory, searchCategory, allCategory) + categories
                categoryAdapter.submitList(fullList)

                // Load first real category (skip Favorites, Search, All)
                val firstCategoryId = if (categories.isNotEmpty()) categories[0].categoryId else "0"
                categoryAdapter.setSelected(if (categories.isNotEmpty()) 3 else 2)
                loadSeries(firstCategoryId)
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "SeriesFragment load error", e)
                progressBar.gone()
                tvEmpty.text = "Error: ${e.message}"
                tvEmpty.visible()
            }
        }
    }

    private fun loadSeries(categoryId: String) {
        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val series = withContext(Dispatchers.IO) {
                    if (categoryId == "0") {
                        MainActivity.apiService.getSeries(prefs.username, prefs.password)
                    } else {
                        MainActivity.apiService.getSeries(
                            prefs.username, prefs.password,
                            categoryId = categoryId
                        )
                    }
                }

                if (series.isEmpty() && categoryId != "0") {
                    android.util.Log.d("VeltrixTV", "Category $categoryId empty, loading all series")
                    val allSeries = withContext(Dispatchers.IO) {
                        MainActivity.apiService.getSeries(prefs.username, prefs.password)
                    }
                    val limited = if (allSeries.size > 200) allSeries.take(200) else allSeries
                    seriesAdapter.submitList(limited)
                    progressBar.gone()
                    if (limited.isEmpty()) {
                        tvEmpty.text = "No series found"
                        tvEmpty.visible()
                    } else {
                        tvEmpty.gone()
                        categoryAdapter.setSelected(0)
                    }
                } else {
                    seriesAdapter.submitList(series)
                    progressBar.gone()
                    if (series.isEmpty()) {
                        tvEmpty.text = "No series found"
                        tvEmpty.visible()
                    } else {
                        tvEmpty.gone()
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "SeriesFragment load error", e)
                progressBar.gone()
                tvEmpty.text = "Error: ${e.message}"
                tvEmpty.visible()
            }
        }
    }

    private fun loadSeriesFavorites() {
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val dao = AppDatabase.getInstance(requireContext()).favoriteDao()
                val favs = withContext(Dispatchers.IO) { dao.getByType("series") }

                if (favs.isEmpty()) {
                    progressBar.gone()
                    tvEmpty.text = "No favorites yet. Long-press a series to add it."
                    tvEmpty.visible()
                    seriesAdapter.submitList(emptyList())
                    return@launch
                }

                val prefs = MainActivity.prefsInstance
                val allSeries = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getSeries(prefs.username, prefs.password)
                }
                val favIds = favs.map { it.streamId }.toSet()
                val favSeries = allSeries.filter { it.seriesId in favIds }

                seriesAdapter.submitList(favSeries)
                progressBar.gone()
                if (favSeries.isEmpty()) {
                    tvEmpty.text = "No favorites found"
                    tvEmpty.visible()
                } else {
                    tvEmpty.gone()
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

        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (allSeriesCache.isEmpty()) {
                    allSeriesCache = withContext(Dispatchers.IO) {
                        MainActivity.apiService.getSeries(prefs.username, prefs.password)
                    }
                }
                progressBar.gone()
                seriesAdapter.submitList(allSeriesCache)
                tvEmpty.gone()
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = "Error loading series"
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
                        seriesAdapter.submitList(allSeriesCache)
                    } else {
                        val filtered = allSeriesCache.filter { it.name.lowercase().contains(query) }
                        seriesAdapter.submitList(filtered)
                        if (filtered.isEmpty()) {
                            tvEmpty.text = "No series found for \"$query\""
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

    override fun onDestroyView() {
        super.onDestroyView()
        allSeriesCache = emptyList()
    }
}
