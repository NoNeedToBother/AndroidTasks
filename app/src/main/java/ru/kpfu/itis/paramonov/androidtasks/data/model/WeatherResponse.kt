package ru.kpfu.itis.paramonov.androidtasks.data.model

import com.google.gson.annotations.SerializedName

class WeatherResponse(
    @SerializedName("weather") val weatherData: List<ResponseWeatherData>,
    @SerializedName("main") val temperatureData: ResponseTemperatureData
)