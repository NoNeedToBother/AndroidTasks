package ru.kpfu.itis.paramonov.androidtasks.utils

import android.content.Context
import ru.kpfu.itis.paramonov.androidtasks.App
import ru.kpfu.itis.paramonov.androidtasks.di.AppComponent

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }