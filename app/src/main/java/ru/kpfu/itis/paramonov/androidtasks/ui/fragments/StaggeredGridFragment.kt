package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.kpfu.itis.paramonov.androidtasks.adapter.ItemAdapter
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentStaggeredGridBinding
import ru.kpfu.itis.paramonov.androidtasks.model.HorizontalItem
import ru.kpfu.itis.paramonov.androidtasks.model.Item
import ru.kpfu.itis.paramonov.androidtasks.model.RectangleItem
import ru.kpfu.itis.paramonov.androidtasks.model.SquareItem

class StaggeredGridFragment: Fragment() {
    private var _binding: FragmentStaggeredGridBinding? = null
    private val binding: FragmentStaggeredGridBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaggeredGridBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val itemAmount = arguments?.getInt("item_amount") ?: 0
        val itemList = ArrayList<Item>()
        val repeat = itemAmount / 6
        for (i in 0 until repeat) {
            itemList.add(HorizontalItem())
            itemList.add(SquareItem())
            itemList.add(RectangleItem())
            itemList.add(RectangleItem())
            itemList.add(SquareItem())
            itemList.add(HorizontalItem())
        }
        val rest = itemAmount % 6
        when(rest) {
            5 -> {
                itemList.add(HorizontalItem())
                itemList.add(SquareItem())
                itemList.add(RectangleItem())
                itemList.add(RectangleItem())
                itemList.add(SquareItem())
            }
            3, 4 -> {
                itemList.add(HorizontalItem())
                itemList.add(RectangleItem())
                itemList.add(RectangleItem())
                if (rest == 4) itemList.add(HorizontalItem())
            }
            1, 2 -> {
                itemList.add(HorizontalItem())
                if (rest == 2) itemList.add(HorizontalItem())
            }
        }
        with(binding) {
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
            )

            val adapter = ItemAdapter(staggeredGridLayoutManager)
            adapter.setItems(itemList)

            rvStaggeredGrid.layoutManager = staggeredGridLayoutManager
            rvStaggeredGrid.adapter = adapter
        }
    }
}