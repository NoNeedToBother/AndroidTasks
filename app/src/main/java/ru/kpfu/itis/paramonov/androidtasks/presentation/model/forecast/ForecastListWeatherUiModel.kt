package ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast

import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherDataUiModel
import java.util.Calendar

data class ForecastListWeatherUiModel(
    val time: Calendar,
    val weatherData: WeatherDataUiModel,
    val temperature: Double
)