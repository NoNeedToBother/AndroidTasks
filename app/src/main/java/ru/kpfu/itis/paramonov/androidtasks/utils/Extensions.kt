package ru.kpfu.itis.paramonov.androidtasks.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.kpfu.itis.paramonov.androidtasks.App
import ru.kpfu.itis.paramonov.androidtasks.di.AppComponent
import androidx.fragment.app.viewModels

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: (stateHandle: SavedStateHandle) -> T
): Lazy<T> {
    return viewModels<T> {
        AssistedViewModelFactory(this, create)
    }
}