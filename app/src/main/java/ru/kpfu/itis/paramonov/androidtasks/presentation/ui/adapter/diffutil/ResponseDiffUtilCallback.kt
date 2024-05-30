package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel

class ResponseDiffUtilCallback : DiffUtil.ItemCallback<ResponseUiModel>() {
    override fun areItemsTheSame(oldItem: ResponseUiModel, newItem: ResponseUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ResponseUiModel, newItem: ResponseUiModel): Boolean {
        return oldItem.let { o ->
            newItem.let { n ->
                o.code == n.code && o.responseBody == n.responseBody && o.url == n.url &&
                        areHeadersEqual(o.headers, n.headers) && o.requestBody == n.requestBody
                        && o.status == n.status
            }
        }
    }

    private fun areHeadersEqual(map1: Map<String, String>, map2: Map<String, String>): Boolean {
        if (map1.keys != map2.keys) return false
        for (key in map1.keys) {
            if (map2[key] != map1[key]) return false
        }
        return true
    }
}