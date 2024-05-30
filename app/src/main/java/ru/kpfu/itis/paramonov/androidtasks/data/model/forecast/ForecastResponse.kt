package ru.kpfu.itis.paramonov.androidtasks.data.model.forecast

import com.google.gson.annotations.SerializedName

class ForecastResponse(
    @SerializedName("list") val forecastList: List<ForecastListWeather>?,
    val city: CityDataResponse?
)