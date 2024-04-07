package ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast

import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherDataUiModel

data class ForecastListWeatherUiModel(
    val time: String,
    val weatherData: WeatherDataUiModel,
    val temperature: Double
)