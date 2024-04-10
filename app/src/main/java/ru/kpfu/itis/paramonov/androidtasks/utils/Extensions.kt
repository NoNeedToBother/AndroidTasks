package ru.kpfu.itis.paramonov.androidtasks.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.kpfu.itis.paramonov.androidtasks.App
import ru.kpfu.itis.paramonov.androidtasks.di.AppComponent
import androidx.fragment.app.viewModels
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import okio.BufferedSource

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

val Response.bodyCopy: ResponseBody?
    get() = body?.let {body ->
        val source: BufferedSource = body.source()
        val bufferedCopy: Buffer = source.buffer.clone()
        bufferedCopy.asResponseBody(body.contentType(), body.contentLength())
    }

inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: (stateHandle: SavedStateHandle) -> T
): Lazy<T> {
    return viewModels<T> {
        AssistedViewModelFactory(this, create)
    }
}