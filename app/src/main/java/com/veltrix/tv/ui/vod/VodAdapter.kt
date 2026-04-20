package com.veltrix.tv.ui.vod

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.models.VodStream
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.loadImage

class VodAdapter(
    private val onMovieClick: (VodStream) -> Unit,
    private val onMovieLongClick: ((VodStream) -> Unit)? = null,
    private val onMovieFocus: ((VodStream) -> Unit)? = null
) : RecyclerView.Adapter<VodAdapter.ViewHolder>() {

    private val items = mutableListOf<VodStream>()

    fun submitList(streams: List<VodStream>) {
        items.clear()
        items.addAll(streams)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)

        init {
            FocusHighlightHelper.setupFocusHighlight(itemView)

            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onMovieClick(items[pos])
                }
            }

            itemView.setOnLongClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onMovieLongClick?.invoke(items[pos])
                }
                true
            }

            itemView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    val pos = bindingAdapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onMovieFocus?.invoke(items[pos])
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_poster, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.name
        holder.ivPoster.loadImage(item.streamIcon, 8f)

        // Show rating badge
        val rating = item.rating
        if (!rating.isNullOrBlank() && rating != "0" && rating != "0.0") {
            holder.tvRating.text = rating
            holder.tvRating.visibility = View.VISIBLE
        } else if (item.rating5Based != null && item.rating5Based > 0) {
            // Convert 5-based to 10-based
            val r10 = item.rating5Based * 2
            holder.tvRating.text = String.format("%.1f", r10)
            holder.tvRating.visibility = View.VISIBLE
        } else {
            holder.tvRating.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size
}
