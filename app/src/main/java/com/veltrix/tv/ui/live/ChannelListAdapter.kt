package com.veltrix.tv.ui.live

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.models.LiveStream
import com.veltrix.tv.util.loadImage

class ChannelListAdapter(
    private val onChannelClick: (LiveStream, Int) -> Unit,
    private val onChannelFocus: ((LiveStream, Int) -> Unit)? = null,
    private val onChannelLongClick: ((LiveStream) -> Unit)? = null
) : RecyclerView.Adapter<ChannelListAdapter.ViewHolder>() {

    private val items = mutableListOf<LiveStream>()
    private var playingStreamId: Int = -1

    fun submitList(streams: List<LiveStream>) {
        items.clear()
        items.addAll(streams)
        notifyDataSetChanged()
    }

    fun setPlaying(streamId: Int) {
        val oldId = playingStreamId
        playingStreamId = streamId
        // Refresh old and new playing items
        items.forEachIndexed { index, stream ->
            if (stream.streamId == oldId || stream.streamId == streamId) {
                notifyItemChanged(index)
            }
        }
    }

    fun getItems(): List<LiveStream> = items.toList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.findViewById(R.id.tvChannelNumber)
        val ivIcon: ImageView = itemView.findViewById(R.id.ivChannelIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvChannelName)
        val tvEpg: TextView = itemView.findViewById(R.id.tvEpgNow)
        val ivPlaying: ImageView = itemView.findViewById(R.id.ivPlaying)

        init {
            itemView.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onChannelClick(items[pos], pos)
                }
            }

            itemView.setOnLongClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onChannelLongClick?.invoke(items[pos])
                }
                true
            }

            itemView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    val pos = bindingAdapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        onChannelFocus?.invoke(items[pos], pos)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvNumber.text = (position + 1).toString()
        holder.tvName.text = item.name
        holder.ivIcon.loadImage(item.streamIcon)

        // Show playing indicator
        if (item.streamId == playingStreamId) {
            holder.ivPlaying.visibility = View.VISIBLE
            holder.tvName.setTextColor(holder.itemView.context.getColor(R.color.red))
        } else {
            holder.ivPlaying.visibility = View.GONE
            holder.tvName.setTextColor(holder.itemView.context.getColor(R.color.text_primary))
        }
    }

    override fun getItemCount(): Int = items.size
}
