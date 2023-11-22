package ru.kpfu.itis.paramonov.androidtasks.model

import androidx.core.app.NotificationManagerCompat

enum class Importance(private val importance: String, private val code: Int) {
    Medium("Medium", NotificationManagerCompat.IMPORTANCE_LOW),
    High("High", NotificationManagerCompat.IMPORTANCE_DEFAULT),
    Urgent("Urgent", NotificationManagerCompat.IMPORTANCE_HIGH);

    override fun toString(): String {
        return importance
    }

    fun getCode(): Int {
        return code
    }

    companion object {
        fun getDefaultValue(): Importance {
            return High
        }
    }
}