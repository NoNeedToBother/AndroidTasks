package ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast

import ru.kpfu.itis.paramonov.androidtasks.utils.Params

data class ForecastDomainModel(
    val forecastList: List<ForecastListWeatherDomainModel>,
    val city: String
) {
    fun isEmpty(): Boolean {
        return (forecastList.isEmpty() && city == Params.CITY_EMPTY_DATA)
    }
}