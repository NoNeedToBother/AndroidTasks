package ru.kpfu.itis.paramonov.androidtasks.ui.utils

import android.content.Context
import android.util.TypedValue
import android.view.View

fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}

fun Context.spToPx(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics)
}
fun View.setAndUpdate(action: () -> Unit) {
    action.invoke()
    invalidate()
}