package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemResponseBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel

class ResponseViewHolder(
    private val binding: ItemResponseBinding
): RecyclerView.ViewHolder(binding.root) {

    init {

    }

    private var item: ResponseUiModel? = null

    fun onBind(item: ResponseUiModel) {
        this.item = item
    }
}