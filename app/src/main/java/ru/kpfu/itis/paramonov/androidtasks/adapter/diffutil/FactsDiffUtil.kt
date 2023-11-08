package ru.kpfu.itis.paramonov.androidtasks.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.androidtasks.model.CityFact
import ru.kpfu.itis.paramonov.androidtasks.model.Model

class FactsDiffUtil(
    private val oldItemsList: List<Model>,
    private val newItemsList: List<Model>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        return if (oldItem is CityFact && newItem is CityFact) oldItem.id == newItem.id
        else false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return if (oldItem is CityFact && newItem is CityFact) (oldItem.title == newItem.title &&
                oldItem.content == newItem.content)
        else false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        if (oldItem is CityFact && newItem is CityFact) {
            return if (oldItem.isLiked != newItem.isLiked) {
                newItem.isLiked
            } else {
                super.getChangePayload(oldItemPosition, newItemPosition)
            }
        }

        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}