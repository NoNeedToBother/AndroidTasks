package ru.kpfu.itis.paramonov.androidtasks.domain.model

data class WeatherDataDomainModel(
    val main: String,
    val description: String,
    val icon: String
)