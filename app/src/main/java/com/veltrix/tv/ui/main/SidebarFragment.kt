package com.veltrix.tv.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veltrix.tv.R

class SidebarFragment : Fragment() {

    private lateinit var rvSidebar: RecyclerView
    lateinit var adapter: SidebarAdapter

    var onSectionSelected: ((SidebarItem, Int) -> Unit)? = null

    companion object {
        const val ID_LIVE = 0
        const val ID_MOVIES = 1
        const val ID_SERIES = 2
        const val ID_FAVORITES = 3
        const val ID_SETTINGS = 4
    }

    private val sidebarItems = listOf(
        SidebarItem("Live TV", ID_LIVE),
        SidebarItem("Movies", ID_MOVIES),
        SidebarItem("Series", ID_SERIES),
        SidebarItem("Favorites", ID_FAVORITES),
        SidebarItem("Settings", ID_SETTINGS)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sidebar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSidebar = view.findViewById(R.id.rvSidebar)

        adapter = SidebarAdapter(sidebarItems) { item, position ->
            onSectionSelected?.invoke(item, position)
        }

        rvSidebar.layoutManager = LinearLayoutManager(requireContext())
        rvSidebar.adapter = adapter
    }

    fun focusCurrentItem() {
        rvSidebar.post {
            val holder = rvSidebar.findViewHolderForAdapterPosition(adapter.selectedPosition)
            holder?.itemView?.requestFocus()
        }
    }
}
