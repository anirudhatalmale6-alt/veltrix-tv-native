package com.veltrix.tv.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.ChannelListHolder
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.FavoriteEntity
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.ui.main.MainActivity
import com.veltrix.tv.ui.player.PlayerActivity
import com.veltrix.tv.ui.series.SeriesDetailActivity
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.gone
import com.veltrix.tv.util.loadImage
import com.veltrix.tv.util.visible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorites = view.findViewById(R.id.rvFavorites)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        rvFavorites.layoutManager = LinearLayoutManager(requireContext())

        val dao = AppDatabase.getInstance(requireContext()).favoriteDao()

        viewLifecycleOwner.lifecycleScope.launch {
            dao.getAllFavorites().collectLatest { favorites ->
                if (favorites.isEmpty()) {
                    tvEmpty.visible()
                    rvFavorites.gone()
                } else {
                    tvEmpty.gone()
                    rvFavorites.visible()
                    val grouped = buildGroupedList(favorites)
                    rvFavorites.adapter = FavoriteListAdapter(grouped) { fav ->
                        openFavorite(fav, favorites.filter { it.type == fav.type })
                    }
                }
            }
        }
    }

    private fun buildGroupedList(favorites: List<FavoriteEntity>): List<Any> {
        val result = mutableListOf<Any>()
        val grouped = favorites.groupBy { it.type }
        val order = listOf("live" to "Live Channels", "vod" to "Movies", "series" to "Series")
        for ((type, label) in order) {
            val items = grouped[type] ?: continue
            result.add(label)
            result.addAll(items)
        }
        return result
    }

    private fun openFavorite(fav: FavoriteEntity, sameFavorites: List<FavoriteEntity>) {
        val prefs = MainActivity.prefsInstance

        when (fav.type) {
            "live" -> {
                val liveStreams = sameFavorites.map {
                    LiveStream(null, it.name, null, it.streamId, it.icon, null, null, null, null, null, null, null)
                }
                ChannelListHolder.set(liveStreams)
                val index = sameFavorites.indexOfFirst { it.streamId == fav.streamId }.coerceAtLeast(0)

                val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${fav.streamId}.m3u8"
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, fav.name)
                    putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Favorites")
                    putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "live")
                    putExtra(PlayerActivity.EXTRA_CURRENT_INDEX, index)
                }
                startActivity(intent)
            }
            "vod" -> {
                val ext = fav.containerExtension ?: "mp4"
                val streamUrl = "${prefs.getBaseUrl()}/movie/${prefs.username}/${prefs.password}/${fav.streamId}.$ext"
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, fav.name)
                    putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Favorites")
                    putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "vod")
                }
                startActivity(intent)
            }
            "series" -> {
                val intent = Intent(requireContext(), SeriesDetailActivity::class.java).apply {
                    putExtra(SeriesDetailActivity.EXTRA_SERIES_ID, fav.seriesId ?: fav.streamId)
                    putExtra(SeriesDetailActivity.EXTRA_SERIES_NAME, fav.name)
                    putExtra(SeriesDetailActivity.EXTRA_SERIES_COVER, fav.icon)
                }
                startActivity(intent)
            }
        }
    }

    inner class FavoriteListAdapter(
        private val items: List<Any>,
        private val onClick: (FavoriteEntity) -> Unit
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val TYPE_HEADER = 0
        private val TYPE_ITEM = 1

        inner class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvHeader: TextView = itemView as TextView
        }

        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivIcon: ImageView = itemView.findViewById(R.id.ivFavIcon)
            val tvName: TextView = itemView.findViewById(R.id.tvFavName)
            val tvType: TextView = itemView.findViewById(R.id.tvFavType)

            init {
                FocusHighlightHelper.setupFocusHighlight(itemView)
                itemView.setOnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        val item = items[pos]
                        if (item is FavoriteEntity) onClick(item)
                    }
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (items[position] is String) TYPE_HEADER else TYPE_ITEM
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == TYPE_HEADER) {
                val tv = TextView(parent.context).apply {
                    setTextColor(resources.getColor(R.color.cyan, null))
                    textSize = 16f
                    setPadding(0, 32, 0, 12)
                    setTypeface(null, android.graphics.Typeface.BOLD)
                }
                HeaderHolder(tv)
            } else {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_favorite, parent, false)
                ItemHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is HeaderHolder -> holder.tvHeader.text = items[position] as String
                is ItemHolder -> {
                    val item = items[position] as FavoriteEntity
                    holder.tvName.text = item.name
                    holder.tvType.text = item.type.replaceFirstChar { it.uppercase() }
                    holder.ivIcon.loadImage(item.icon)
                }
            }
        }

        override fun getItemCount(): Int = items.size
    }
}
