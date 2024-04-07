package ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast

data class ForecastUiModel(
    val forecasts: List<ForecastListWeatherUiModel>,
    val city: String
)