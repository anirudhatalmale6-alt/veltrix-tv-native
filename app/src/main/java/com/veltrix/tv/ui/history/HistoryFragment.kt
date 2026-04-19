package com.veltrix.tv.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.WatchHistoryEntity
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.ui.series.SeriesDetailActivity
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.loadImage
import com.veltrix.tv.util.visible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class HistoryFragment : Fragment() {

    private lateinit var rvHistory: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = view.findViewById(R.id.rvHistory)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        rvHistory.layoutManager = LinearLayoutManager(requireContext())

        val dao = AppDatabase.getInstance(requireContext()).watchHistoryDao()

        viewLifecycleOwner.lifecycleScope.launch {
            dao.getRecentHistory().collectLatest { history ->
                if (history.isEmpty()) {
                    tvEmpty.visible()
                    rvHistory.gone()
                } else {
                    tvEmpty.gone()
                    rvHistory.visible()
                    rvHistory.adapter = HistoryAdapter(history) { item ->
                        openHistoryItem(item)
                    }
                }
            }
        }
    }

    private fun openHistoryItem(item: WatchHistoryEntity) {
        val prefs = MainActivity.prefsInstance

        when (item.type) {
            "live" -> {
                val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${item.streamId}.ts"
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, item.name)
                    putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "History")
                    putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "live")
                }
                startActivity(intent)
            }
            "vod" -> {
                val ext = item.containerExtension ?: "mp4"
                val streamUrl = "${prefs.getBaseUrl()}/movie/${prefs.username}/${prefs.password}/${item.streamId}.$ext"
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, item.name)
                    putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "History")
                    putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "vod")
                    if (item.positionMs > 0) {
                        putExtra(PlayerActivity.EXTRA_RESUME_POSITION, item.positionMs)
                    }
                }
                startActivity(intent)
            }
            "series" -> {
                if (item.seriesId != null) {
                    val intent = Intent(requireContext(), SeriesDetailActivity::class.java).apply {
                        putExtra(SeriesDetailActivity.EXTRA_SERIES_ID, item.seriesId)
                        putExtra(SeriesDetailActivity.EXTRA_SERIES_NAME, item.name)
                        putExtra(SeriesDetailActivity.EXTRA_SERIES_COVER, item.icon)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    inner class HistoryAdapter(
        private val items: List<WatchHistoryEntity>,
        private val onClick: (WatchHistoryEntity) -> Unit
    ) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
            val tvTime: TextView = itemView.findViewById(R.id.tvTime)
            val progressWatch: ProgressBar = itemView.findViewById(R.id.progressWatch)

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
                .inflate(R.layout.item_history, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name
            holder.ivIcon.loadImage(item.icon)

            // Build details text
            val details = buildString {
                append(item.type.replaceFirstChar { it.uppercase() })
                if (item.type == "series" && item.episodeTitle != null) {
                    append(" - S${item.seasonNumber}E${item.episodeNumber}: ${item.episodeTitle}")
                }
            }
            holder.tvDetails.text = details

            // Time ago
            holder.tvTime.text = getTimeAgo(item.watchedAt)

            // Progress bar for VOD/series
            if (item.durationMs > 0 && item.positionMs > 0 && item.type != "live") {
                val percent = ((item.positionMs.toFloat() / item.durationMs) * 100).toInt()
                holder.progressWatch.progress = percent
                holder.progressWatch.visible()
            } else {
                holder.progressWatch.gone()
            }
        }

        override fun getItemCount(): Int = items.size

        private fun getTimeAgo(timestamp: Long): String {
            val diff = System.currentTimeMillis() - timestamp
            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m ago"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h ago"
                diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}d ago"
                else -> {
                    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
                    sdf.format(Date(timestamp))
                }
            }
        }
    }
}
