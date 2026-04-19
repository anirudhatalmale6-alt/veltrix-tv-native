package com.veltrix.tv.ui.live

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R
import com.veltrix.tv.data.models.Category

class CategoryAdapter(
    private val onCategorySelected: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val items = mutableListOf<Category>()
    var selectedPosition: Int = 0
        private set

    fun submitList(categories: List<Category>) {
        items.clear()
        items.addAll(categories)
        selectedPosition = 0
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView as TextView

        init {
            itemView.isFocusable = true
            itemView.isFocusableInTouchMode = true

            itemView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    tvName.setTextColor(ContextCompat.getColor(v.context, R.color.white))
                    v.isSelected = true
                } else {
                    val isSelected = adapterPosition == selectedPosition
                    tvName.setTextColor(
                        ContextCompat.getColor(
                            v.context,
                            if (isSelected) R.color.text_primary else R.color.text_secondary
                        )
                    )
                    v.isSelected = isSelected
                }
            }

            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION && pos != selectedPosition) {
                    val old = selectedPosition
                    selectedPosition = pos
                    notifyItemChanged(old)
                    notifyItemChanged(pos)
                    onCategorySelected(items[pos])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.categoryName

        val isSelected = position == selectedPosition
        holder.tvName.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (isSelected) R.color.text_primary else R.color.text_secondary
            )
        )
        holder.itemView.isSelected = isSelected
    }

    override fun getItemCount(): Int = items.size
}
