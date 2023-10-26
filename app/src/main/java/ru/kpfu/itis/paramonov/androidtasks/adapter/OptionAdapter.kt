package ru.kpfu.itis.paramonov.androidtasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemOptionBinding
import ru.kpfu.itis.paramonov.androidtasks.model.Option

class OptionAdapter(
    val options : List<Option>,
    val onItemChecked: (Int) -> Unit,
    val onRootClicked: (Int) -> Unit
    )  : RecyclerView.Adapter<OptionItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionItem {
        return OptionItem(
            binding = ItemOptionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onItemChecked = onItemChecked,
            onRootClicked = onRootClicked,
        )
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: OptionItem, position: Int) {
        holder.onBind(options[position])
    }

}