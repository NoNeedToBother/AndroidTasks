package ru.kpfu.itis.paramonov.androidtasks.domain.model.weather

data class WeatherDataDomainModel(
    val main: String,
    val description: String,
    val icon: String
)