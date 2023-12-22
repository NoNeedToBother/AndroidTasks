package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemSquareBinding
import ru.kpfu.itis.paramonov.androidtasks.model.SquareItem

class SquareItem(private val binding: ItemSquareBinding):RecyclerView.ViewHolder(binding.root) {
    private var item: SquareItem? = null

    fun onBind() {
        this.item = item
    }
}