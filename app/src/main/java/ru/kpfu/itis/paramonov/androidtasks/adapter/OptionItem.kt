package ru.kpfu.itis.paramonov.androidtasks.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemOptionBinding
import ru.kpfu.itis.paramonov.androidtasks.model.Option

class OptionItem(
    private val binding: ItemOptionBinding,
    private val onItemChecked: (Int) -> Unit,
    private val onRootClicked: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        with(binding) {
            rbOption.setOnClickListener {
                onItemChecked.invoke(adapterPosition)
            }
            root.setOnClickListener {
                onRootClicked.invoke(adapterPosition)
            }
        }
    }

    fun onBind(option: Option) {
        with(binding) {
            tvOption.text = option.option.toString()
            rbOption.isChecked = option.checked
            rbOption.isEnabled = !option.checked
        }
    }
}