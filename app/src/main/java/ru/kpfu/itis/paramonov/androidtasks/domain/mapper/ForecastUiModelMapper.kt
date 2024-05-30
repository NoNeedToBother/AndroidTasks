package ru.kpfu.itis.paramonov.androidtasks.domain.mapper

import ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast.ForecastDomainModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast.ForecastListWeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast.ForecastUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherDataUiModel
import javax.inject.Inject

class ForecastUiModelMapper @Inject constructor(){
    fun mapDomainToUiModel(input: ForecastDomainModel): ForecastUiModel {
        return with(input) {
            val mappedList = input.forecastList.map {
                val weatherDataUiModel = WeatherDataUiModel(
                    it.weatherData.main,
                    it.weatherData.description,
                    it.weatherData.icon
                )
                ForecastListWeatherUiModel(
                    it.time,
                    weatherDataUiModel,
                    it.temperature
                )
            }
            ForecastUiModel(
                forecasts = mappedList,
                city = city
            )
        }
    }
}