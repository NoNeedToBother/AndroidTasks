package ru.kpfu.itis.paramonov.androidtasks.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class DateTimeParser @Inject constructor() {

    fun parseTime(time: String): Calendar {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.UK)
        simpleDateFormat.parse(time)?.let {
            calendar.time = it
        }
        return calendar
    }

    fun parseTime(time: String, dateFormat: String, locale: Locale): Calendar {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(dateFormat, locale)
        simpleDateFormat.parse(time)?.let {
            calendar.time = it
        }
        return calendar
    }

    companion object {
        const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss"
    }
}