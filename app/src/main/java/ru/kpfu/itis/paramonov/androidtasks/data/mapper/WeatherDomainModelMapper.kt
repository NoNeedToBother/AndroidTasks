package ru.kpfu.itis.paramonov.androidtasks.data.mapper

import ru.kpfu.itis.paramonov.androidtasks.data.model.weather.WeatherResponse
import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherCoordDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDataDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Params
import javax.inject.Inject

class WeatherDomainModelMapper @Inject constructor() {
    fun mapResponseToDomainModel(input: WeatherResponse?): WeatherDomainModel? {
        return input?.let {
            WeatherDomainModel(
                weatherData = WeatherDataDomainModel(
                    main = it.weatherData[0].main ?: Params.WEATHER_EMPTY_DATA,
                    description = it.weatherData[0].description ?: Params.WEATHER_EMPTY_DATA,
                    icon = it.weatherData[0].icon ?: Params.WEATHER_EMPTY_DATA
                ),
                temperature = it.temperatureData.temp ?: Params.NO_TEMPERATURE_DATA,
                city = it.city ?: Params.CITY_EMPTY_DATA,
                coordinates = WeatherCoordDomainModel(
                    longitude = it.coordinates.longitude ?: Params.NO_COORD_DATA,
                    latitude = it.coordinates.latitude ?: Params.NO_COORD_DATA
                )
            )
        }
    }
}