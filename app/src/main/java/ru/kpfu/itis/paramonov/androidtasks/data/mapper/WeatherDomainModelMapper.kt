package ru.kpfu.itis.paramonov.androidtasks.data.mapper

import ru.kpfu.itis.paramonov.androidtasks.data.model.WeatherResponse
import ru.kpfu.itis.paramonov.androidtasks.domain.model.WeatherDataDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.WeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.WeatherMainDomainModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Constants

class WeatherDomainModelMapper {
    fun mapResponseToDomainModel(input: WeatherResponse?): WeatherDomainModel? {
        return input?.let {
            WeatherDomainModel(
                weatherData = WeatherDataDomainModel(
                    main = it.weatherData[0].main ?: Constants.WEATHER_EMPTY_DATA,
                    description = it.weatherData[0].description ?: Constants.WEATHER_EMPTY_DATA,
                    icon = it.weatherData[0].icon ?: Constants.WEATHER_EMPTY_DATA
                ),
                temperatureData = WeatherMainDomainModel(
                    temp = (it.temperatureData.temp ?: 0) as Double,
                    feelsLike = (it.temperatureData.temp ?: 0) as Double
                )
            )
        }
    }
}