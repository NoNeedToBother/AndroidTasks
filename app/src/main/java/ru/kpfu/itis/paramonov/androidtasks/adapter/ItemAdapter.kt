package ru.kpfu.itis.paramonov.androidtasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemHorizontalBinding
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemSquareBinding
import ru.kpfu.itis.paramonov.androidtasks.model.HorizontalItem
import ru.kpfu.itis.paramonov.androidtasks.model.Item
import ru.kpfu.itis.paramonov.androidtasks.model.RectangleItem
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.SquareItem
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.VerticalItem

class ItemAdapter(private val staggeredGridLayoutManager: StaggeredGridLayoutManager)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = mutableListOf<Item>()

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is ru.kpfu.itis.paramonov.androidtasks.model.SquareItem -> R.layout.item_square
            is HorizontalItem -> R.layout.item_horizontal
            is RectangleItem -> R.layout.item_vertical
            else -> throw RuntimeException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_square -> SquareItem(
                binding = ItemSquareBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            R.layout.item_horizontal -> ru.kpfu.itis.paramonov.androidtasks.ui.holders.HorizontalItem(
                binding = ItemHorizontalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            R.layout.item_vertical -> ru.kpfu.itis.paramonov.androidtasks.ui.holders.HorizontalItem(
                binding = ItemHorizontalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            else -> throw RuntimeException()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SquareItem -> holder.onBind()
            is ru.kpfu.itis.paramonov.androidtasks.ui.holders.HorizontalItem -> holder.onBind()
            is VerticalItem -> holder.onBind()
        }
    }

    fun setItems(items: List<Item>) {
        this.items.clear()
        this.items.addAll(items)
    }
}