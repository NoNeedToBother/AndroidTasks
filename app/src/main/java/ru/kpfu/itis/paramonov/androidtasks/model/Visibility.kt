package ru.kpfu.itis.paramonov.androidtasks.model

import android.app.Notification
import androidx.core.app.NotificationCompat

enum class Visibility(private val appName: String, private val code: Int) {
    Public("Public", NotificationCompat.VISIBILITY_PUBLIC),
    Secret("Secret", NotificationCompat.VISIBILITY_SECRET),
    Private("Private", NotificationCompat.VISIBILITY_PRIVATE);

    override fun toString(): String {
        return appName
    }

    fun getCode(): Int {
        return code
    }
}