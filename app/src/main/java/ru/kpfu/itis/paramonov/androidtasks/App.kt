package ru.kpfu.itis.paramonov.androidtasks

import android.app.Application
import ru.kpfu.itis.paramonov.androidtasks.di.AppComponent
import ru.kpfu.itis.paramonov.androidtasks.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(ctx = this)
            .build()
    }
}