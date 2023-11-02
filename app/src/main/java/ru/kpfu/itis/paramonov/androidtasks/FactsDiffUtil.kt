package ru.kpfu.itis.paramonov.androidtasks

import androidx.recyclerview.widget.DiffUtil

class FactsDiffUtil(
    private val oldItemsList: List<CityFact>,
    private val newItemsList: List<CityFact>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return (oldItem.title == newItem.title) &&
                (oldItem.content == newItem.content)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]

        return if (oldItem.isLiked != newItem.isLiked) {
            newItem.isLiked
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}