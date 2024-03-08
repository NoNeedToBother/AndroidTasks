package ru.kpfu.itis.paramonov.androidtasks

import android.app.Application
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //ServiceLocator.initDataDependencies(ctx = this)
        //ServiceLocator.initDomainDependencies()
    }
}