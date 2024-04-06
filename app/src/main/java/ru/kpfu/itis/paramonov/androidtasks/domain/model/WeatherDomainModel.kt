package ru.kpfu.itis.paramonov.androidtasks.domain.model

import ru.kpfu.itis.paramonov.androidtasks.utils.Params

data class WeatherDomainModel(
    val weatherData: WeatherDataDomainModel,
    val temperatureData: WeatherMainDomainModel,
    val city: String
) {
    fun isEmptyResponse(): Boolean {
        val isMainDataEmpty = temperatureData.temp == 0.0
        val isWeatherDataEmpty = with(this.weatherData) {
            val weatherEmptyData = Params.WEATHER_EMPTY_DATA
            icon == weatherEmptyData && description == weatherEmptyData && main == weatherEmptyData
        }
        return isMainDataEmpty && isWeatherDataEmpty
    }
}