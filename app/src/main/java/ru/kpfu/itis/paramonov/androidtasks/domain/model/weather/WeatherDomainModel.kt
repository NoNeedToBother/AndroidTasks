package ru.kpfu.itis.paramonov.androidtasks.domain.model.weather

import ru.kpfu.itis.paramonov.androidtasks.utils.Params

data class WeatherDomainModel(
    val weatherData: WeatherDataDomainModel,
    val temperature: Double,
    val city: String,
    val coordinates: WeatherCoordDomainModel
) {
    fun isEmpty(): Boolean {
        val isMainDataEmpty = temperature == Params.NO_TEMPERATURE_DATA
        val isWeatherDataEmpty = with(this.weatherData) {
            val weatherEmptyData = Params.WEATHER_EMPTY_DATA
            icon == weatherEmptyData && description == weatherEmptyData && main == weatherEmptyData
        }
        val isCityEmpty = city == Params.CITY_EMPTY_DATA
        val areCoordEmpty = with(coordinates) {
            val noCoordData = Params.NO_COORD_DATA
            longitude.equals(noCoordData) && latitude.equals(noCoordData)
        }
        return isMainDataEmpty && isWeatherDataEmpty && isCityEmpty && areCoordEmpty
    }
}