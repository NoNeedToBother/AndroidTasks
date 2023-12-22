package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemSquareBinding
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemVerticalBinding
import ru.kpfu.itis.paramonov.androidtasks.model.RectangleItem
import ru.kpfu.itis.paramonov.androidtasks.model.SquareItem

class VerticalItem(private val binding: ItemVerticalBinding):RecyclerView.ViewHolder(binding.root) {
    private var item: RectangleItem? = null

    fun onBind() {
    }
}