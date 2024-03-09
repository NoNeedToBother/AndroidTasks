package ru.kpfu.itis.paramonov.androidtasks.domain.mapper

import ru.kpfu.itis.paramonov.androidtasks.domain.model.WeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherDataUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherMainUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel
import javax.inject.Inject

class WeatherUiModelMapper @Inject constructor() {
    fun mapDomainToUiModel(input: WeatherDomainModel): WeatherUiModel {
        with(input) {
            return WeatherUiModel(
                weatherData = WeatherDataUiModel(
                    main = weatherData.main,
                    description = weatherData.description,
                    icon = weatherData.icon
                ),
                temperatureData = WeatherMainUiModel(
                    temp = temperatureData.temp,
                    feelsLike = temperatureData.feelsLike
                )
            )
        }
    }
}