package ru.kpfu.itis.paramonov.androidtasks.model

import androidx.core.app.NotificationManagerCompat

enum class Importance(private val appName: String, private val code: Int) {
    Medium("Medium", NotificationManagerCompat.IMPORTANCE_LOW),
    High("High", NotificationManagerCompat.IMPORTANCE_DEFAULT),
    Urgent("Urgent", NotificationManagerCompat.IMPORTANCE_HIGH);

    override fun toString(): String {
        return appName
    }

    fun getCode(): Int {
        return code
    }
}