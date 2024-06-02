package ru.kpfu.itis.paramonov.androidtasks.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(private val spacing: Int, private vararg val sides: Side): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        s: RecyclerView.State
    ) {
        parent.adapter?.let { adapter ->
            sides.forEach {
                when (it) {
                    Side.LEFT -> rect.left = getSpacing(parent, view, adapter)
                    Side.RIGHT -> rect.right = getSpacing(parent, view, adapter)
                    Side.BOTTOM -> rect.bottom = getSpacing(parent, view, adapter)
                    Side.TOP -> rect.top = getSpacing(parent, view, adapter)
                }
            }
        }
    }

    private fun getSpacing(parent: RecyclerView, view: View, adapter: RecyclerView.Adapter<*>): Int {
        return when (parent.getChildAdapterPosition(view)) {
            RecyclerView.NO_POSITION,
            adapter.itemCount - 1 -> 0
            else -> spacing
        }
    }

    enum class Side {
        LEFT, RIGHT, TOP, BOTTOM
    }
}