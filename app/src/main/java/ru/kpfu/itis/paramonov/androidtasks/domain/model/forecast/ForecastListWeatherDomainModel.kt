package ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast

import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDataDomainModel

class ForecastListWeatherDomainModel(
    val time: String,
    val weatherData: WeatherDataDomainModel,
    val temperature: Double
)