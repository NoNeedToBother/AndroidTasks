package ru.kpfu.itis.paramonov.androidtasks.model

import android.app.Notification
import androidx.core.app.NotificationCompat

enum class Visibility(private val visibility: String, private val code: Int) {
    Public("Public", NotificationCompat.VISIBILITY_PUBLIC),
    Secret("Secret", NotificationCompat.VISIBILITY_SECRET),
    Private("Private", NotificationCompat.VISIBILITY_PRIVATE);

    override fun toString(): String {
        return visibility
    }

    fun getCode(): Int {
        return code
    }
}