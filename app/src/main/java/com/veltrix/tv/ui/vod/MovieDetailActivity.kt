package com.veltrix.tv.ui.vod

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.veltrix.tv.R
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.FavoriteEntity
import com.veltrix.tv.data.models.VodInfo
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.visible
import com.veltrix.tv.util.toast
import com.veltrix.tv.util.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailActivity : AppCompatActivity() {

    private var streamId: Int = 0
    private var movieName: String = ""
    private var movieIcon: String? = null
    private var movieRating: String? = null
    private var containerExt: String? = null
    private var categoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        streamId = intent.getIntExtra("stream_id", 0)
        movieName = intent.getStringExtra("name") ?: ""
        movieIcon = intent.getStringExtra("icon")
        movieRating = intent.getStringExtra("rating")
        containerExt = intent.getStringExtra("container_ext")
        categoryId = intent.getStringExtra("category_id")

        val ivBackdrop = findViewById<ImageView>(R.id.ivBackdrop)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvRating = findViewById<TextView>(R.id.tvRating)
        val tvMeta = findViewById<TextView>(R.id.tvMeta)
        val tvCast = findViewById<TextView>(R.id.tvCast)
        val tvDirector = findViewById<TextView>(R.id.tvDirector)
        val tvPlot = findViewById<TextView>(R.id.tvPlot)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnAddList = findViewById<Button>(R.id.btnAddList)
        val btnRemoveHistory = findViewById<Button>(R.id.btnRemoveHistory)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Show basic info immediately
        tvTitle.text = movieName
        if (!movieIcon.isNullOrBlank()) {
            ivBackdrop.loadImage(movieIcon, 0f)
        }
        if (!movieRating.isNullOrBlank() && movieRating != "0" && movieRating != "0.0") {
            tvRating.text = movieRating
            tvRating.visible()
        }

        // Play button
        btnPlay.setOnClickListener { playMovie() }
        btnPlay.requestFocus()

        // Add to My list (favorites)
        btnAddList.setOnClickListener { toggleFavorite() }

        // Remove from History
        btnRemoveHistory.setOnClickListener { removeFromHistory() }

        // Focused button text color change
        listOf(btnPlay, btnAddList, btnRemoveHistory).forEach { btn ->
            btn.setOnFocusChangeListener { _, hasFocus ->
                btn.setTextColor(if (hasFocus) 0xFF111111.toInt() else 0xFFFFFFFF.toInt())
            }
        }

        // Load full details
        progressBar.visible()
        lifecycleScope.launch {
            try {
                val prefs = MainActivity.prefsInstance
                val info = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getVodInfo(prefs.username, prefs.password, vodId = streamId)
                }
                progressBar.gone()
                displayInfo(info, ivBackdrop, tvTitle, tvRating, tvMeta, tvCast, tvDirector, tvPlot)
            } catch (e: Exception) {
                android.util.Log.e("VeltrixTV", "MovieDetail load error", e)
                progressBar.gone()
            }
        }
    }

    private fun displayInfo(
        info: VodInfo,
        ivBackdrop: ImageView,
        tvTitle: TextView,
        tvRating: TextView,
        tvMeta: TextView,
        tvCast: TextView,
        tvDirector: TextView,
        tvPlot: TextView
    ) {
        val movieInfo = info.info ?: return

        // Title
        val title = movieInfo.name ?: movieInfo.originalName
        if (!title.isNullOrBlank()) {
            tvTitle.text = title
            movieName = title
        }

        // Backdrop
        val backdrop = movieInfo.backdropPath?.firstOrNull()
        if (!backdrop.isNullOrBlank()) {
            ivBackdrop.loadImage(backdrop, 0f)
        } else if (!movieInfo.movieImage.isNullOrBlank()) {
            ivBackdrop.loadImage(movieInfo.movieImage, 0f)
        }

        // Container ext from API
        val ext = info.movieData?.containerExtension
        if (!ext.isNullOrBlank()) {
            containerExt = ext
        }

        // Rating
        val rating = movieInfo.rating
        if (!rating.isNullOrBlank() && rating != "0" && rating != "0.0") {
            tvRating.text = rating
            tvRating.visible()
        }

        // Meta
        val metaParts = mutableListOf<String>()
        val releaseDate = movieInfo.releaseDate ?: movieInfo.releaseDateAlt
        if (!releaseDate.isNullOrBlank()) {
            metaParts.add(releaseDate.take(4))
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
            tvMeta.text = metaParts.joinToString("  \u2022  ")
        }

        // Cast
        val cast = movieInfo.cast
        if (!cast.isNullOrBlank()) {
            tvCast.text = "Cast:     $cast"
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

    private fun playMovie() {
        val prefs = MainActivity.prefsInstance
        val ext = containerExt ?: "mp4"
        val streamUrl = "${prefs.getBaseUrl()}/movie/${prefs.username}/${prefs.password}/$streamId.$ext"

        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
            putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, movieName)
            putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Movies")
            putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "vod")
            putExtra(PlayerActivity.EXTRA_STREAM_ICON, movieIcon)
            putExtra(PlayerActivity.EXTRA_CONTAINER_EXT, containerExt)
        }
        startActivity(intent)
    }

    private fun toggleFavorite() {
        val dao = AppDatabase.getInstance(this).favoriteDao()
        lifecycleScope.launch {
            val isFav = withContext(Dispatchers.IO) {
                dao.isFavorite(streamId, "vod")
            }
            withContext(Dispatchers.IO) {
                if (isFav) {
                    dao.delete(streamId, "vod")
                } else {
                    dao.insert(
                        FavoriteEntity(
                            streamId = streamId,
                            name = movieName,
                            icon = movieIcon,
                            type = "vod",
                            categoryId = categoryId,
                            containerExtension = containerExt
                        )
                    )
                }
            }
            toast(getString(if (isFav) R.string.fav_removed else R.string.fav_added))
            val btn = findViewById<Button>(R.id.btnAddList)
            btn.text = if (!isFav) "\u2605  In My list" else "\u2606  Add to My list"
        }
    }

    private fun removeFromHistory() {
        val dao = AppDatabase.getInstance(this).watchHistoryDao()
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteByStreamId(streamId)
            }
            toast("Removed from history")
        }
    }
}
