package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemResponseBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseStatusUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager

class ResponseViewHolder(
    private val binding: ItemResponseBinding,
    private val onResponseClicked: (Int) -> Unit,
    private val resourceManager: ResourceManager
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            item?.let {
                onResponseClicked(adapterPosition)
            }
        }
    }

    private var item: ResponseUiModel? = null

    fun onBind(item: ResponseUiModel) {
        this.item = item

        with(binding) {
            item.let {
                when (it.status) {
                    ResponseStatusUiModel.SUCCESS -> root.strokeColor = resourceManager.getColor(R.color.green)
                    ResponseStatusUiModel.NEUTRAL -> root.strokeColor = resourceManager.getColor(R.color.black)
                    ResponseStatusUiModel.ERROR -> root.strokeColor = resourceManager.getColor(R.color.red)
                }
                tvMethod.text = it.method
                tvCode.text = it.code.toString()
            }
        }
    }
}