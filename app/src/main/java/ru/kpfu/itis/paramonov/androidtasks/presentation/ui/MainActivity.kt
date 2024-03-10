package ru.kpfu.itis.paramonov.androidtasks.presentation.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.DebugViewPagerFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.WeatherFragment
import ru.kpfu.itis.paramonov.androidtasks.utils.ShakeDetector
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var shakeDetector: ShakeDetector

    private var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initShakeDetector()

        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_container, WeatherFragment())
            .commit()
    }

    private fun initShakeDetector() {
        if (BuildConfig.DEBUG) {
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            shakeDetector.addOnShakeDetectedListener {
                if (supportFragmentManager.
                    findFragmentByTag(DebugViewPagerFragment.DEBUG_VIEW_PAGER_FRAGMENT_TAG) == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_activity_container,
                            DebugViewPagerFragment(),
                            DebugViewPagerFragment.DEBUG_VIEW_PAGER_FRAGMENT_TAG
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }
            registerShakeDetector()
        }
    }

    override fun onResume() {
        super.onResume()
        registerShakeDetector()
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(shakeDetector)
    }

    private fun registerShakeDetector() {
        sensorManager?.run {
            registerListener(
                shakeDetector,
                getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
}