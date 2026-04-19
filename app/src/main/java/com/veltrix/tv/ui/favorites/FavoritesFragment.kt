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
import com.veltrix.tv.data.local.AppDatabase
import com.veltrix.tv.data.local.FavoriteEntity
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
                    rvFavorites.adapter = FavoriteListAdapter(favorites) { fav ->
                        openFavorite(fav)
                    }
                }
            }
        }
    }

    private fun openFavorite(fav: FavoriteEntity) {
        val prefs = MainActivity.prefsInstance

        when (fav.type) {
            "live" -> {
                val streamUrl = "${prefs.getBaseUrl()}/live/${prefs.username}/${prefs.password}/${fav.streamId}.ts"
                val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(PlayerActivity.EXTRA_STREAM_URL, streamUrl)
                    putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, fav.name)
                    putExtra(PlayerActivity.EXTRA_CATEGORY_NAME, "Favorites")
                    putExtra(PlayerActivity.EXTRA_STREAM_TYPE, "live")
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
        private val items: List<FavoriteEntity>,
        private val onClick: (FavoriteEntity) -> Unit
    ) : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivIcon: ImageView = itemView.findViewById(R.id.ivFavIcon)
            val tvName: TextView = itemView.findViewById(R.id.tvFavName)
            val tvType: TextView = itemView.findViewById(R.id.tvFavType)

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
                .inflate(R.layout.item_favorite, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name
            holder.tvType.text = item.type.replaceFirstChar { it.uppercase() }
            holder.ivIcon.loadImage(item.icon)
        }

        override fun getItemCount(): Int = items.size
    }
}
