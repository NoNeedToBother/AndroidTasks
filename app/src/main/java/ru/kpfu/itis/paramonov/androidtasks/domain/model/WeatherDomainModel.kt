package ru.kpfu.itis.paramonov.androidtasks.domain.model

import ru.kpfu.itis.paramonov.androidtasks.utils.Constants

data class WeatherDomainModel(
    val weatherData: WeatherDataDomainModel,
    val temperatureData: WeatherMainDomainModel
) {
    fun isEmptyResponse(): Boolean {
        val isMainDataEmpty = with(this.temperatureData) {
            temp == 0.0 && feelsLike == 0.0
        }
        val isWeatherDataEmpty = with(this.weatherData) {
            val weatherEmptyData = Constants.WEATHER_EMPTY_DATA
            icon == weatherEmptyData && description == weatherEmptyData && main == weatherEmptyData
        }
        return isMainDataEmpty && isWeatherDataEmpty
    }
}