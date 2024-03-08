package ru.kpfu.itis.paramonov.androidtasks.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.WeatherFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ServiceLocator.initDataDependencies(ctx = this)
        ServiceLocator.initDomainDependencies()

        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_container, WeatherFragment())
            .commit()
    }
}