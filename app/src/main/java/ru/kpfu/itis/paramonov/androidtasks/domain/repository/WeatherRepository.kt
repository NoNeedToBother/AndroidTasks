package ru.kpfu.itis.paramonov.androidtasks.domain.repository

import ru.kpfu.itis.paramonov.androidtasks.domain.model.WeatherDomainModel

interface WeatherRepository {
    suspend fun getCurrentWeatherByCity(city: String): WeatherDomainModel
}