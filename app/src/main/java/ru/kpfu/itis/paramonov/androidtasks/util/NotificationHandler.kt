package ru.kpfu.itis.paramonov.androidtasks.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.model.Importance
import ru.kpfu.itis.paramonov.androidtasks.model.NotificationConfig

class NotificationHandler(private val context: Context) {

    private fun getNotifChannelId(importance: Importance): String {
        return context.getString(R.string.default_notification_channel_id) + "_" + importance.toString().lowercase()
    }

    private fun createNotificationChannel(manager: NotificationManager, importance: Importance) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getNotifChannelId(importance),
                importance.toString(),
                importance.getCode()
            )
            manager.createNotificationChannel(channel)
        }
    }

    fun createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (importance in Importance.values()) {
                createNotificationChannel(
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
                    importance
                )
            }
        }
    }

    fun createNotification(title: String, content: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NotificationCompat.Builder(
            context, getNotifChannelId(NotificationConfig.importance)
        )
            .setSmallIcon(R.drawable.active_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setVisibility(NotificationConfig.visibility.getCode())
            .build()
            .also {
                notificationManager.notify(0, it)
            }
    }

}