package ru.kpfu.itis.paramonov.androidtasks.data.model.forecast

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.paramonov.androidtasks.data.model.weather.ResponseTemperatureData
import ru.kpfu.itis.paramonov.androidtasks.data.model.weather.ResponseWeatherData

class ForecastListWeather(
    @SerializedName("dt_txt") val time: String?,
    @SerializedName("weather") val weatherData: List<ResponseWeatherData>?,
    @SerializedName("main") val temperatureData: ResponseTemperatureData?
)