package ru.kpfu.itis.paramonov.androidtasks.model

import android.app.Notification
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationConfig {
    companion object {
        var importance = Importance.High

        var visibility = Visibility.Public

        var hasLongText = false

        var hasOptions = false

    }
}