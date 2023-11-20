package ru.kpfu.itis.paramonov.androidtasks

import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ActivityMainBinding
import ru.kpfu.itis.paramonov.androidtasks.util.NotificationHandler

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHandler(this).createNotificationChannels()

        findViewById<BottomNavigationView>(R.id.bnv_main).apply {
            val controller = (supportFragmentManager.findFragmentById(R.id.main_activity_container)
                    as NavHostFragment).navController
            setupWithNavController(controller)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}