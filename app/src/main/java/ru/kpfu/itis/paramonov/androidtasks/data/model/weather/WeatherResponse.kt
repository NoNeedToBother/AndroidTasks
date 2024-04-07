package ru.kpfu.itis.paramonov.androidtasks.data.model.weather

import com.google.gson.annotations.SerializedName

class WeatherResponse(
    @SerializedName("weather") val weatherData: List<ResponseWeatherData>,
    @SerializedName("main") val temperatureData: ResponseTemperatureData,
    @SerializedName("name") val city: String?,
    @SerializedName("coord") val coordinates: ResponseCoordData
)