package ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast

import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDataDomainModel
import java.util.Calendar

class ForecastListWeatherDomainModel(
    val time: Calendar,
    val weatherData: WeatherDataDomainModel,
    val temperature: Double
)