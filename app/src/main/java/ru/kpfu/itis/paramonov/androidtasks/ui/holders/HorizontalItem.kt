package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemHorizontalBinding
import ru.kpfu.itis.paramonov.androidtasks.model.SquareItem

class HorizontalItem(private val binding: ItemHorizontalBinding):RecyclerView.ViewHolder(binding.root) {
    private var item: SquareItem? = null

    fun onBind() {
        this.item = item
    }
}