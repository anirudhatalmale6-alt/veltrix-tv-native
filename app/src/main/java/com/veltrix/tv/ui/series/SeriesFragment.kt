package com.veltrix.tv.ui.series

import android.content.Intent
import android.os.Bundle
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

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvSeries: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var seriesAdapter: SeriesAdapter

    override fun canGoLeft(): Boolean {
        val focused = activity?.currentFocus ?: return false
        return rvSeries.isAncestorOf(focused)
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

        rvCategories = view.findViewById(R.id.rvCategories)
        rvSeries = view.findViewById(R.id.rvSeries)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        setupAdapters()
        loadCategories()
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
    }

    private fun calculateGridColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val availableWidth = screenWidthDp - 260 - 200 - 24
        return (availableWidth / 162).toInt().coerceIn(3, 7)
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

                loadSeries("0")
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = getString(R.string.error_loading)
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

                seriesAdapter.submitList(series)
                progressBar.gone()

                if (series.isEmpty()) {
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
}
