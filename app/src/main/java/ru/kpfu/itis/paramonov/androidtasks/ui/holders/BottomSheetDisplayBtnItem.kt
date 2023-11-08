package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemBsdBtnBinding

class BottomSheetDisplayBtnItem(
    private val binding : ItemBsdBtnBinding,
    onBtnClicked : () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.btnBsd.setOnClickListener {
            onBtnClicked()
        }
    }

    fun bindItem() {}
}