package ru.kpfu.itis.paramonov.androidtasks.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ActivityMainBinding
import ru.kpfu.itis.paramonov.androidtasks.ui.fragments.MainFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_container, MainFragment())
            .commit()
    }
}