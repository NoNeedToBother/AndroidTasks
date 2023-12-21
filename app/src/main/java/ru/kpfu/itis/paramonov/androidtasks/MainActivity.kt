package ru.kpfu.itis.paramonov.androidtasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kpfu.itis.paramonov.androidtasks.databinding.ActivityMainBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initDb()
    }

    private fun initDb() {
        ServiceLocator.initData(ctx = this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}