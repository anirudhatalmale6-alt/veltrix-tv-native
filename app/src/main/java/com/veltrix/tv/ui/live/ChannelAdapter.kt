package com.veltrix.tv.ui.live

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.util.FocusHighlightHelper
import com.veltrix.tv.util.loadImage

class ChannelAdapter(
    private val onChannelClick: (LiveStream, Int) -> Unit,
    private val onChannelLongClick: ((LiveStream) -> Unit)? = null
) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    private val items = mutableListOf<LiveStream>()

    fun submitList(streams: List<LiveStream>) {
        items.clear()
        items.addAll(streams)
        notifyDataSetChanged()
    }

    fun getItems(): List<LiveStream> = items.toList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivChannelIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvChannelName)

        init {
            FocusHighlightHelper.setupFocusHighlight(itemView)

            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onChannelClick(items[pos], pos)
                }
            }

            itemView.setOnLongClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onChannelLongClick?.invoke(items[pos])
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.ivIcon.loadImage(item.streamIcon)
    }

    override fun getItemCount(): Int = items.size
}
