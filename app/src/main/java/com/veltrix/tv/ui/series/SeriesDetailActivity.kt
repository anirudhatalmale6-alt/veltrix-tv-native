package com.veltrix.tv.ui.series

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.models.Episode
import com.veltrix.tv.data.models.SeriesInfo
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.WatchHistoryEntity
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.loadImage
import com.veltrix.tv.util.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar as AndroidProgressBar

class SeriesDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SERIES_ID = "series_id"
        const val EXTRA_SERIES_NAME = "series_name"
        const val EXTRA_SERIES_COVER = "series_cover"
    }

    private lateinit var ivCover: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvPlot: TextView
    private lateinit var tvGenre: TextView
    private lateinit var rvSeasons: RecyclerView
    private lateinit var rvEpisodes: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView

    private var seriesInfo: SeriesInfo? = null
    private var currentSeasonKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_detail)

        ivCover = findViewById(R.id.ivCover)
        tvTitle = findViewById(R.id.tvSeriesTitle)
        tvPlot = findViewById(R.id.tvPlot)
        tvGenre = findViewById(R.id.tvGenre)
        rvSeasons = findViewById(R.id.rvSeasons)
        rvEpisodes = findViewById(R.id.rvEpisodes)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)

        val seriesId = intent.getIntExtra(EXTRA_SERIES_ID, 0)
        val seriesName = intent.getStringExtra(EXTRA_SERIES_NAME) ?: ""
        val seriesCover = intent.getStringExtra(EXTRA_SERIES_COVER)

        tvTitle.text = seriesName
        ivCover.loadImage(seriesCover, 8f)

        rvSeasons.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvEpisodes.layoutManager = LinearLayoutManager(this)

        loadSeriesInfo(seriesId)
    }

    private fun loadSeriesInfo(seriesId: Int) {
        val prefs = MainActivity.prefsInstance
        progressBar.visible()

        lifecycleScope.launch {
            try {
                val info = withContext(Dispatchers.IO) {
                    MainActivity.apiService.getSeriesInfo(
                        prefs.username, prefs.password, seriesId = seriesId
                    )
                }

                seriesInfo = info
                progressBar.gone()

                info.info?.let { detail ->
                    if (!detail.cover.isNullOrEmpty()) {
                        ivCover.loadImage(detail.cover, 8f)
                    }
                    tvTitle.text = detail.name ?: tvTitle.text
                    tvPlot.text = detail.plot ?: ""
                    tvGenre.text = detail.genre ?: ""
                }

                val seasonKeys = info.episodes?.keys?.sorted() ?: emptyList()
                if (seasonKeys.isNotEmpty()) {
                    rvSeasons.adapter = SeasonTabAdapter(seasonKeys) { key ->
                        currentSeasonKey = key
                        showEpisodes(key)
                    }
                    currentSeasonKey = seasonKeys.first()
                    showEpisodes(seasonKeys.first())
                }

            } catch (e: Exception) {
                progressBar.gone()
                tvError.visible()
            }
        }
    }

    private fun showEpisodes(seasonKey: String) {
        val episodes = seriesInfo?.episodes?.get(seasonKey) ?: emptyList()
        val seriesId = intent.getIntExtra(EXTRA_SERIES_ID, 0)
        val dao = AppDatabase.getInstance(this).watchHistoryDao()

        lifecycleScope.launch {
            val historyList = withContext(Dispatchers.IO) {
                dao.getSeriesHistory(seriesId)
            }
            rvEpisodes.adapter = EpisodeAdapter(episodes, historyList, seasonKey) { episode ->
                playEpisode(episode)
            }
        }
    }

    private fun playEpisode(episode: Episode) {
        val prefs = MainActivity.prefsInstance
        val ext = episode.containerExtension ?: "mp4"
        val streamUrl = "${prefs.getBaseUrl()}/series/${prefs.username}/${prefs.password}/${episode.id}.$ext"
        val seriesId = intent.getIntExtra(EXTRA_SERIES_ID, 0)

        val playerIntent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
            putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, episode.title ?: "Episode ${episode.episodeNum}")
            putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, tvTitle.text.toString())
            putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "series")
            putExtra(PlayerActivity.EXTRA_SERIES_ID, seriesId)
            putExtra(PlayerActivity.EXTRA_SEASON_NUMBER, currentSeasonKey)
            putExtra(PlayerActivity.EXTRA_EPISODE_NUMBER, episode.episodeNum ?: 0)
            putExtra(PlayerActivity.EXTRA_EPISODE_TITLE, episode.title)
            putExtra(PlayerActivity.EXTRA_CONTAINER_EXT, ext)
            putExtra(PlayerActivity.EXTRA_STREAM_ICON, intent.getStringExtra(EXTRA_SERIES_COVER))
        }
        startActivity(playerIntent)
    }

    // Inner adapters for seasons and episodes
    inner class SeasonTabAdapter(
        private val seasons: List<String>,
        private val onSeasonSelected: (String) -> Unit
    ) : RecyclerView.Adapter<SeasonTabAdapter.ViewHolder>() {

        private var selectedPos = 0

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvSeason: TextView = itemView.findViewById(R.id.tvSeason)

            init {
                itemView.isFocusable = true
                itemView.isFocusableInTouchMode = true

                itemView.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        tvSeason.setTextColor(ContextCompat.getColor(v.context, R.color.white))
                        v.isSelected = true
                    } else {
                        val isSel = adapterPosition == selectedPos
                        tvSeason.setTextColor(
                            ContextCompat.getColor(
                                v.context,
                                if (isSel) R.color.text_primary else R.color.text_secondary
                            )
                        )
                        v.isSelected = isSel
                    }
                }

                itemView.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION && pos != selectedPos) {
                        val old = selectedPos
                        selectedPos = pos
                        notifyItemChanged(old)
                        notifyItemChanged(pos)
                        onSeasonSelected(seasons[pos])
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_season, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvSeason.text = getString(R.string.season_label, seasons[position].toIntOrNull() ?: (position + 1))
            val isSel = position == selectedPos
            holder.tvSeason.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    if (isSel) R.color.text_primary else R.color.text_secondary
                )
            )
            holder.itemView.isSelected = isSel
        }

        override fun getItemCount(): Int = seasons.size
    }

    inner class EpisodeAdapter(
        private val episodes: List<Episode>,
        private val historyList: List<WatchHistoryEntity>,
        private val seasonKey: String,
        private val onEpisodeClick: (Episode) -> Unit
    ) : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvEpisodeTitle)
            val tvPlot: TextView = itemView.findViewById(R.id.tvEpisodePlot)
            val tvDuration: TextView = itemView.findViewById(R.id.tvEpisodeDuration)
            val progressEpisode: AndroidProgressBar = itemView.findViewById(R.id.progressEpisode)

            init {
                FocusHighlightHelper.setupFocusHighlight(itemView)

                itemView.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onEpisodeClick(episodes[pos])
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_episode, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val ep = episodes[position]
            val epNum = ep.episodeNum ?: (position + 1)
            val epTitle = ep.title ?: "Episode $epNum"
            holder.tvTitle.text = getString(R.string.episode_label, epNum, epTitle)
            holder.tvPlot.text = ep.info?.plot ?: ""
            holder.tvDuration.text = ep.info?.duration ?: ""

            if (holder.tvPlot.text.isBlank()) holder.tvPlot.gone()
            else holder.tvPlot.visible()

            if (holder.tvDuration.text.isBlank()) holder.tvDuration.gone()
            else holder.tvDuration.visible()

            // Show watch progress
            val history = historyList.find { h ->
                h.seasonNumber == seasonKey && h.episodeNumber == epNum
            }
            if (history != null && history.durationMs > 0 && history.positionMs > 0) {
                val percent = ((history.positionMs.toFloat() / history.durationMs) * 100).toInt()
                holder.progressEpisode.progress = percent
                holder.progressEpisode.visible()
            } else {
                holder.progressEpisode.gone()
            }
        }

        override fun getItemCount(): Int = episodes.size
    }
}
