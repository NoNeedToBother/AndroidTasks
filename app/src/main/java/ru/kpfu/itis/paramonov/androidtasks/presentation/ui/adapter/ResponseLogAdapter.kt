package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemResponseBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder.ResponseViewHolder

class ResponseLogAdapter(
    private val onResponseClicked: ((ResponseUiModel) -> Unit)
): RecyclerView.Adapter<ResponseViewHolder>() {

    private val responses: MutableList<ResponseUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        return ResponseViewHolder(
            binding = ItemResponseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = responses.size

    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        holder.onBind(responses[position])
    }
}