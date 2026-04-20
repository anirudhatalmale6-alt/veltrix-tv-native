package com.veltrix.tv.ui.vod

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
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
import com.veltrix.tv.data.models.VodInfo
import com.veltrix.tv.data.models.VodStream
import com.veltrix.tv.ui.live.CategoryAdapter
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.visible
import com.veltrix.tv.util.toast
import com.veltrix.tv.util.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VodFragment : Fragment(), MainActivity.DpadNavigable {

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvMovies: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var detailPanel: FrameLayout
    private lateinit var ivBackdrop: ImageView
    private lateinit var tvMovieTitle: TextView
    private lateinit var tvRatingBadge: TextView
    private lateinit var tvMetaInfo: TextView
    private lateinit var tvCast: TextView
    private lateinit var tvDirector: TextView
    private lateinit var tvPlot: TextView
    private lateinit var tvCategoryHeader: TextView

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var vodAdapter: VodAdapter

    private var detailLoadJob: Job? = null
    private var currentSelectedCategory: String = ""

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
        detailPanel = view.findViewById(R.id.detailPanel)
        ivBackdrop = view.findViewById(R.id.ivBackdrop)
        tvMovieTitle = view.findViewById(R.id.tvMovieTitle)
        tvRatingBadge = view.findViewById(R.id.tvRatingBadge)
        tvMetaInfo = view.findViewById(R.id.tvMetaInfo)
        tvCast = view.findViewById(R.id.tvCast)
        tvDirector = view.findViewById(R.id.tvDirector)
        tvPlot = view.findViewById(R.id.tvPlot)
        tvCategoryHeader = view.findViewById(R.id.tvCategoryHeader)

        setupAdapters()
        loadCategories()
    }

    private fun setupAdapters() {
        categoryAdapter = CategoryAdapter { category ->
            currentSelectedCategory = category.categoryName
            tvCategoryHeader.text = category.categoryName
            loadMovies(category.categoryId)
        }
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = categoryAdapter

        vodAdapter = VodAdapter(
            onMovieClick = { movie ->
                openMovieDetail(movie)
            },
            onMovieLongClick = { movie ->
                toggleFavorite(movie)
            },
            onMovieFocus = { movie ->
                loadMovieDetail(movie)
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
        return (availableWidth / 162).toInt().coerceIn(3, 7)
    }

    private fun openMovieDetail(movie: VodStream) {
        try {
            val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                putExtra("stream_id", movie.streamId)
                putExtra("name", movie.name)
                putExtra("icon", movie.streamIcon)
                putExtra("rating", movie.rating)
                putExtra("container_ext", movie.containerExtension)
                putExtra("category_id", movie.categoryId)
            }
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback: play directly
            playMovie(movie)
        }
    }

    private fun playMovie(movie: VodStream) {
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
    }

    private fun loadMovieDetail(movie: VodStream) {
        // Show basic info immediately
        tvMovieTitle.text = movie.name
        detailPanel.visible()

        // Show poster as backdrop initially
        if (!movie.streamIcon.isNullOrBlank()) {
            ivBackdrop.loadImage(movie.streamIcon, 0f)
        }

        // Show rating if available
        val rating = movie.rating
        if (!rating.isNullOrBlank() && rating != "0" && rating != "0.0") {
            tvRatingBadge.text = rating
            tvRatingBadge.visible()
        } else {
            tvRatingBadge.gone()
        }

        // Clear previous details
        tvMetaInfo.text = ""
        tvCast.gone()
        tvDirector.gone()
        tvPlot.gone()

        // Debounce: cancel previous detail load
        detailLoadJob?.cancel()
        detailLoadJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(300) // wait 300ms before fetching (user might still be scrolling)
            try {
                val prefs = MainActivity.prefsInstance
                val info = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getVodInfo(prefs.username, prefs.password, vodId = movie.streamId)
                }
                if (!isAdded) return@launch
                displayMovieInfo(info)
            } catch (e: Exception) {
                android.util.Log.d("VeltrixTV", "VodInfo load error: ${e.message}")
            }
        }
    }

    private fun displayMovieInfo(info: VodInfo) {
        val movieInfo = info.info ?: return

        // Update title with original name if available
        val title = movieInfo.name ?: movieInfo.originalName
        if (!title.isNullOrBlank()) {
            tvMovieTitle.text = title
        }

        // Backdrop image
        val backdrop = movieInfo.backdropPath?.firstOrNull()
        if (!backdrop.isNullOrBlank()) {
            ivBackdrop.loadImage(backdrop, 0f)
        } else if (!movieInfo.movieImage.isNullOrBlank()) {
            ivBackdrop.loadImage(movieInfo.movieImage, 0f)
        }

        // Rating badge
        val rating = movieInfo.rating
        if (!rating.isNullOrBlank() && rating != "0" && rating != "0.0") {
            tvRatingBadge.text = rating
            tvRatingBadge.visible()
        }

        // Meta info (year, duration, genre)
        val metaParts = mutableListOf<String>()
        val releaseDate = movieInfo.releaseDate ?: movieInfo.releaseDateAlt
        if (!releaseDate.isNullOrBlank()) {
            metaParts.add(releaseDate.take(4)) // year
        }
        val duration = movieInfo.duration
        if (!duration.isNullOrBlank()) {
            metaParts.add(duration)
        } else if (movieInfo.durationSecs != null && movieInfo.durationSecs > 0) {
            val mins = movieInfo.durationSecs / 60
            val h = mins / 60
            val m = mins % 60
            metaParts.add(if (h > 0) "${h}h ${m}m" else "${m}m")
        }
        val genre = movieInfo.genre
        if (!genre.isNullOrBlank()) {
            metaParts.add(genre)
        }
        if (metaParts.isNotEmpty()) {
            tvMetaInfo.text = metaParts.joinToString("  \u2022  ")
        }

        // Cast
        val cast = movieInfo.cast
        if (!cast.isNullOrBlank()) {
            tvCast.text = "Cast:  $cast"
            tvCast.visible()
        }

        // Director
        val director = movieInfo.director
        if (!director.isNullOrBlank()) {
            tvDirector.text = "Director:  $director"
            tvDirector.visible()
        }

        // Plot
        val plot = movieInfo.plot
        if (!plot.isNullOrBlank()) {
            tvPlot.text = plot
            tvPlot.visible()
        }
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

                val firstCategoryId = if (categories.isNotEmpty()) categories[0].categoryId else "0"
                currentSelectedCategory = if (categories.isNotEmpty()) categories[0].categoryName else "All"
                tvCategoryHeader.text = currentSelectedCategory
                categoryAdapter.setSelected(if (categories.isNotEmpty()) 1 else 0)
                loadMovies(firstCategoryId)
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "VodFragment load error", e)
                progressBar.gone()
                tvEmpty.text = "Error: ${e.message}"
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

                if (streams.isEmpty() && categoryId != "0") {
                    android.util.Log.d("VeltrixTV", "Category $categoryId empty, loading all VOD")
                    val allStreams = withContext(Dispatchers.IO) {
                        MainActivity.apiService.getVodStreams(prefs.username, prefs.password)
                    }
                    val limited = if (allStreams.size > 200) allStreams.take(200) else allStreams
                    vodAdapter.submitList(limited)
                    progressBar.gone()
                    if (limited.isEmpty()) {
                        tvEmpty.text = "No movies found"
                        tvEmpty.visible()
                        detailPanel.gone()
                    } else {
                        tvEmpty.gone()
                        categoryAdapter.setSelected(0)
                    }
                } else {
                    vodAdapter.submitList(streams)
                    progressBar.gone()
                    if (streams.isEmpty()) {
                        tvEmpty.text = "No movies found"
                        tvEmpty.visible()
                        detailPanel.gone()
                    } else {
                        tvEmpty.gone()
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "VodFragment load error", e)
                progressBar.gone()
                tvEmpty.text = "Error: ${e.message}"
                tvEmpty.visible()
            }
        }
    }
}
