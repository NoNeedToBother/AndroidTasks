package ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather

data class WeatherUiModel(
    val weatherData: WeatherDataUiModel,
    val temperature: Double,
    val city: String,
    val coordinates: WeatherCoordUiModel
) {
}