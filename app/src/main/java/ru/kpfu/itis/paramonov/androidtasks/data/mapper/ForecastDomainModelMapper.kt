package ru.kpfu.itis.paramonov.androidtasks.data.mapper

import ru.kpfu.itis.paramonov.androidtasks.data.model.forecast.ForecastListWeather
import ru.kpfu.itis.paramonov.androidtasks.data.model.forecast.ForecastResponse
import ru.kpfu.itis.paramonov.androidtasks.data.model.weather.ResponseTemperatureData
import ru.kpfu.itis.paramonov.androidtasks.data.model.weather.ResponseWeatherData
import ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast.ForecastListWeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast.ForecastDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDataDomainModel
import ru.kpfu.itis.paramonov.androidtasks.utils.DateTimeParser
import ru.kpfu.itis.paramonov.androidtasks.utils.Params
import javax.inject.Inject

class ForecastDomainModelMapper @Inject constructor(
    private val dateTimeParser: DateTimeParser
) {

    fun mapResponseToDomainModel(input: ForecastResponse?): ForecastDomainModel? {
        return input?.let {response ->
            val forecastList = ArrayList<ForecastListWeatherDomainModel>()
            response.forecastList?.let { forecasts ->
                for (forecast in forecasts) {
                    getForecastDomainListWeather(forecast)?.let {
                        forecastList.add(it)
                    }
                }
            }
            ForecastDomainModel(
                forecastList = forecastList,
                city = response.city?.city ?: Params.CITY_EMPTY_DATA,
            )
        }
    }

    private fun getForecastDomainListWeather(data: ForecastListWeather?): ForecastListWeatherDomainModel? {
        return data?.let {
            if (checkForecastListWeather(it)) {
                val temperatureData = it.temperatureData as ResponseTemperatureData
                val weatherData = (it.weatherData as List<ResponseWeatherData>)[0]
                ForecastListWeatherDomainModel(
                    time = dateTimeParser.parseTime(it.time as String),
                    temperature = temperatureData.temp as Double,
                    weatherData = WeatherDataDomainModel(
                        main = weatherData.main as String,
                        description = weatherData.description as String,
                        icon = weatherData.icon as String
                    )
                )
            }
            else null
        }
    }

    private fun checkForecastListWeather(data: ForecastListWeather): Boolean {
        return with(data) {
            var correct = true
            if (time == null) correct = false
            if (!weatherData.isNullOrEmpty()) {
                with(weatherData[0]) {
                    if (main == null || description == null || icon == null) correct = false
                }
            } else correct = false
            temperatureData?.run {
                if (temp == null) correct = false
            } ?: {
                correct = false
            }
            correct
        }
    }
}