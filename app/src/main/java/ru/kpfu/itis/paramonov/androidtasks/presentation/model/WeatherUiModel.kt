package ru.kpfu.itis.paramonov.androidtasks.presentation.model

data class WeatherUiModel(
    val weatherData: WeatherDataUiModel,
    val temperatureData: WeatherMainUiModel,
    val city: String
) {
}