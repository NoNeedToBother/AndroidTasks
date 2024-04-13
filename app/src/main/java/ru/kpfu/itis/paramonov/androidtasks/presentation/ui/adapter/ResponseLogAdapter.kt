package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemResponseBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.diffutil.ResponseDiffUtilCallback
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder.ResponseViewHolder
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager

class ResponseLogAdapter(
    private val onResponseClicked: (Int) -> Unit,
    private val resourceManager: ResourceManager
): ListAdapter<ResponseUiModel, ResponseViewHolder>(ResponseDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        return ResponseViewHolder(
            binding = ItemResponseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onResponseClicked = onResponseClicked,
            resourceManager = resourceManager
        )
    }
    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}