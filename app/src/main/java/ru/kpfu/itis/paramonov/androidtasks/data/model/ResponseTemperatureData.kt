package ru.kpfu.itis.paramonov.androidtasks.data.model

import com.google.gson.annotations.SerializedName

class ResponseTemperatureData(
    val temp: Double? = null,
    @SerializedName("feels_like") val feelsLike: Double? = null
)