package com.veltrix.tv.ui.series

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var isCategoryVisible = true
    private var categoryWidth = 0

    override fun canGoLeft(): Boolean {
        val focused = activity?.currentFocus ?: return false
        if (rvSeries.isAncestorOf(focused)) {
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
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryContainer = view.findViewById(R.id.categoryContainer)
        rvCategories = view.findViewById(R.id.rvCategories)
        rvSeries = view.findViewById(R.id.rvSeries)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)

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
            loadSeries(category.categoryId)
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

                val allCategory = Category("0", getString(R.string.all_categories), 0)
                val fullList = listOf(allCategory) + categories
                categoryAdapter.submitList(fullList)

                // Load first real category instead of "All" to avoid loading thousands of series
                val firstCategoryId = if (categories.isNotEmpty()) categories[0].categoryId else "0"
                categoryAdapter.setSelected(if (categories.isNotEmpty()) 1 else 0)
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
                    // First category was empty - try loading all series
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
}
