package com.veltrix.tv.ui.vod

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
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.FavoriteEntity
import com.veltrix.tv.data.models.VodStream
import com.veltrix.tv.ui.live.CategoryAdapter
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.visible
import com.veltrix.tv.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VodFragment : Fragment(), MainActivity.DpadNavigable {

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvMovies: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var vodAdapter: VodAdapter

    override fun canGoLeft(): Boolean {
        val focused = activity?.currentFocus ?: return false
        return rvMovies.isAncestorOf(focused)
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
        return inflater.inflate(R.layout.fragment_vod, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCategories = view.findViewById(R.id.rvCategories)
        rvMovies = view.findViewById(R.id.rvMovies)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        setupAdapters()
        loadCategories()
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter { category ->
            loadMovies(category.categoryId)
        }
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = categoryAdapter

        vodAdapter = VodAdapter(
            onMovieClick = { movie ->
                try {
                    val prefs = MainActivity.prefsInstance
                    val ext = movie.containerExtension ?: "mp4"
                    val streamUrl = "${prefs.getBaseUrl()}/movie/${prefs.username}/${prefs.password}/${movie.streamId}.$ext"

                    val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                        putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                        putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, movie.name)
                        putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Movies")
                        putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "vod")
                        putExtra(PlayerActivity.EXTRA_STREAM_ICON, movie.streamIcon)
                        putExtra(PlayerActivity.EXTRA_CONTAINER_EXT, movie.containerExtension)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    android.util.Log.e("VeltrixTV", "openMovie error", e)
                }
            },
            onMovieLongClick = { movie ->
                toggleFavorite(movie)
            }
        )
        val columns = calculateGridColumns()
        rvMovies.layoutManager = GridLayoutManager(requireContext(), columns)
        rvMovies.adapter = vodAdapter
        rvMovies.setHasFixedSize(true)
        rvMovies.setItemViewCacheSize(20)
    }

    private fun calculateGridColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val availableWidth = screenWidthDp - 260 - 200 - 24
        return (availableWidth / 162).toInt().coerceIn(3, 7) // 150dp poster + 12dp margin
    }

    private fun toggleFavorite(movie: VodStream) {
        val dao = AppDatabase.getInstance(requireContext()).favoriteDao()
        viewLifecycleOwner.lifecycleScope.launch {
            val isFav = withContext(Dispatchers.IO) {
                dao.isFavorite(movie.streamId, "vod")
            }
            withContext(Dispatchers.IO) {
                if (isFav) {
                    dao.delete(movie.streamId, "vod")
                } else {
                    dao.insert(
                        FavoriteEntity(
                            streamId = movie.streamId,
                            name = movie.name,
                            icon = movie.streamIcon,
                            type = "vod",
                            categoryId = movie.categoryId,
                            containerExtension = movie.containerExtension
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
                    MainActivity.apiService.getVodCategories(prefs.username, prefs.password)
                }

                val allCategory = Category("0", getString(R.string.all_categories), 0)
                val fullList = listOf(allCategory) + categories
                categoryAdapter.submitList(fullList)

                loadMovies("0")
            } catch (e: Exception) {
                progressBar.gone()
                tvEmpty.text = getString(R.string.error_loading)
                tvEmpty.visible()
            }
        }
    }

    private fun loadMovies(categoryId: String) {
        val prefs = MainActivity.prefsInstance
        progressBar.visible()
        tvEmpty.gone()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val streams = withContext(Dispatchers.IO) {
                    if (categoryId == "0") {
                        MainActivity.apiService.getVodStreams(prefs.username, prefs.password)
                    } else {
                        MainActivity.apiService.getVodStreams(
                            prefs.username, prefs.password,
                            categoryId = categoryId
                        )
                    }
                }

                vodAdapter.submitList(streams)
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
}
