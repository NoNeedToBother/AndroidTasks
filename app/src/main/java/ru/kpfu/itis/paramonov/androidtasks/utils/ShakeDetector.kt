package ru.kpfu.itis.paramonov.androidtasks.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import javax.inject.Inject
import kotlin.math.sqrt

class ShakeDetector @Inject constructor(): SensorEventListener {

    private var lastShaked = 0L

    private var onShakeDetected: (() -> Unit)? = null

    fun addOnShakeDetectedListener(onShakeDetected: () -> Unit) {
        this.onShakeDetected = onShakeDetected
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.run {
            val x = values[0]
            val y = values[1]
            val z = values[2]
            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if(acceleration > ACCELERATION_THRESHOLD) {
                val now = System.currentTimeMillis()
                if (now - lastShaked > MIN_TIME_BETWEEN_SENSOR_CHANGES_TO_REGISTER_SHAKE) {
                    lastShaked = now
                    onShakeDetected?.invoke()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    companion object {
        private var acceleration = 10f
        private var currentAcceleration = SensorManager.GRAVITY_EARTH
        private var lastAcceleration = SensorManager.GRAVITY_EARTH

        private const val ACCELERATION_THRESHOLD = 12f

        private const val MIN_TIME_BETWEEN_SENSOR_CHANGES_TO_REGISTER_SHAKE = 500L
    }
}