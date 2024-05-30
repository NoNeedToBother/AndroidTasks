package ru.kpfu.itis.paramonov.androidtasks.domain.mapper

import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherCoordUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherDataUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import javax.inject.Inject

class WeatherUiModelMapper @Inject constructor() {
    fun mapDomainToUiModel(input: WeatherDomainModel): WeatherUiModel {
        return with(input) {
            WeatherUiModel(
                weatherData = WeatherDataUiModel(
                    main = weatherData.main,
                    description = weatherData.description,
                    icon = weatherData.icon
                ),
                temperature = temperature,
                city = input.city,
                coordinates = WeatherCoordUiModel(
                    longitude = input.coordinates.longitude,
                    latitude = input.coordinates.latitude
                )
            )
        }
    }
}