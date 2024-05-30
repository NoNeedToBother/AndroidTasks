package ru.kpfu.itis.paramonov.androidtasks.domain.repository

import ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast.ForecastDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDomainModel

interface WeatherRepository {
    suspend fun getCurrentWeatherByCity(city: String): WeatherDomainModel

    suspend fun getForecastByCoordinates(longitude: Double, latitude: Double): ForecastDomainModel
}