package ru.kpfu.itis.paramonov.androidtasks.data.model.forecast

import com.google.gson.annotations.SerializedName

class CityDataResponse(
    @SerializedName("name") val city: String
)