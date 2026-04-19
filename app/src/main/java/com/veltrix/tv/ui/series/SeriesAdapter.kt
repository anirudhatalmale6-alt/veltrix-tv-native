package com.veltrix.tv.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.models.SeriesItem
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.loadImage

class SeriesAdapter(
    private val onSeriesClick: (SeriesItem) -> Unit,
    private val onSeriesLongClick: ((SeriesItem) -> Unit)? = null
) : RecyclerView.Adapter<SeriesAdapter.ViewHolder>() {

    private val items = mutableListOf<SeriesItem>()

    fun submitList(series: List<SeriesItem>) {
        items.clear()
        items.addAll(series)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        init {
            FocusHighlightHelper.setupFocusHighlight(itemView)

            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onSeriesClick(items[pos])
                }
            }

            itemView.setOnLongClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onSeriesLongClick?.invoke(items[pos])
                }
                true
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
        holder.ivPoster.loadImage(item.cover, 8f)
    }

    override fun getItemCount(): Int = items.size
}
