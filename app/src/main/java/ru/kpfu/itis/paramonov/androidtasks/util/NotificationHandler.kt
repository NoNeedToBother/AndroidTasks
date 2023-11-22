package ru.kpfu.itis.paramonov.androidtasks.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.kpfu.itis.paramonov.androidtasks.MainActivity
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.model.Importance
import ru.kpfu.itis.paramonov.androidtasks.model.NotificationConfig
import ru.kpfu.itis.paramonov.androidtasks.model.Visibility

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

    fun createNotification(title: String, content: String, default: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val importance = if (default) Importance.getDefaultValue()
        else NotificationConfig.importance

        val visibility = if (default) Visibility.getDefaultValue()
        else NotificationConfig.visibility

        NotificationCompat.Builder(
            context, getNotifChannelId(importance)
        )
            .setSmallIcon(R.drawable.active_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setVisibility(visibility.getCode())
            .setDefaultIntent()
            .apply {
                if (!default) {
                    if (NotificationConfig.hasLongText) setStyle(
                        NotificationCompat.BigTextStyle().bigText(content)
                    )
                    if (NotificationConfig.hasOptions) setOptions()
                }
            }
            .build().also {
                notificationManager.notify(0, it)
            }
    }

    private fun NotificationCompat.Builder.setDefaultIntent(): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pIntent = createPendingIntent(NOTIFICATION_INTENT_DEFAULT_REQ_CODE, intent)
        return this.setContentIntent(pIntent)
    }

    private fun NotificationCompat.Builder.setOptions(): NotificationCompat.Builder {
        val settingsIntent = Intent(context, MainActivity::class.java).apply{
            putExtra(NOTIFICATION_INTENT_ACTION, NOTIFICATION_INTENT_ACTION_SETTINGS)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val settingsPendingIntent =
            createPendingIntent(NOTIFICATION_INTENT_SETTINGS_REQ_CODE, settingsIntent)

        addAction(
            R.drawable.icon_settings,
            context.getString(R.string.settings),
            settingsPendingIntent)

        val toastIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(NOTIFICATION_INTENT_ACTION, NOTIFICATION_INTENT_ACTION_TOAST)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val toastPendingIntent =
            createPendingIntent(NOTIFICATION_INTENT_TOAST_REQ_CODE, toastIntent)

        addAction(
            R.drawable.icon_message,
            context.getString(R.string.open_with_surprise),
            toastPendingIntent
        )
        return this
    }

    private fun createPendingIntent(requestCode: Int, intent: Intent) =
        PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    companion object {
        const val NOTIFICATION_INTENT_DEFAULT_REQ_CODE = 0
        const val NOTIFICATION_INTENT_SETTINGS_REQ_CODE = 1
        const val NOTIFICATION_INTENT_TOAST_REQ_CODE = 2

        const val NOTIFICATION_INTENT_ACTION = "action"
        const val NOTIFICATION_INTENT_ACTION_TOAST = "action_show_toast"
        const val NOTIFICATION_INTENT_ACTION_SETTINGS = "action_go_to_settings"
    }

}