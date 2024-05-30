package ru.kpfu.itis.paramonov.androidtasks.data.model.weather

import com.google.gson.annotations.SerializedName

class ResponseCoordData(
    @SerializedName("lon") val longitude: Double? = null,
    @SerializedName("lat") val latitude: Double? = null
)