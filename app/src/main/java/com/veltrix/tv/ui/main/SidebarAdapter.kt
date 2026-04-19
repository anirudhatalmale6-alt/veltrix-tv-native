package com.veltrix.tv.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.util.FocusHighlightHelper

data class SidebarItem(
    val title: String,
    val id: Int
)

class SidebarAdapter(
    private val items: List<SidebarItem>,
    private val onItemSelected: (SidebarItem, Int) -> Unit
) : RecyclerView.Adapter<SidebarAdapter.ViewHolder>() {

    var selectedPosition: Int = 0
        set(value) {
            val old = field
            field = value
            notifyItemChanged(old)
            notifyItemChanged(value)
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvSidebarItem)
        val indicator: View = itemView.findViewById(R.id.indicator)

        init {
            itemView.isFocusable = true
            itemView.isFocusableInTouchMode = true

            itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v.scaleX = 1.03f
                    v.scaleY = 1.03f
                    v.isSelected = true
                    tvTitle.setTextColor(ContextCompat.getColor(v.context, R.color.white))
                } else {
                    v.scaleX = 1.0f
                    v.scaleY = 1.0f
                    v.isSelected = false
                    val isSelected = adapterPosition == selectedPosition
                    tvTitle.setTextColor(
                        ContextCompat.getColor(
                            v.context,
                            if (isSelected) R.color.text_primary else R.color.text_secondary
                        )
                    )
                }
            }

            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    selectedPosition = pos
                    onItemSelected(items[pos], pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sidebar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title

        val isSelected = position == selectedPosition
        holder.indicator.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (isSelected) R.color.red else android.R.color.transparent
            )
        )
        holder.tvTitle.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (isSelected) R.color.text_primary else R.color.text_secondary
            )
        )
        holder.itemView.isSelected = isSelected
    }

    override fun getItemCount(): Int = items.size
}
