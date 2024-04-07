package ru.kpfu.itis.paramonov.androidtasks.data.repository

import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.EmptyWeatherResponseException
import ru.kpfu.itis.paramonov.androidtasks.data.mapper.ForecastDomainModelMapper
import ru.kpfu.itis.paramonov.androidtasks.data.mapper.WeatherDomainModelMapper
import ru.kpfu.itis.paramonov.androidtasks.data.remote.OpenWeatherApi
import ru.kpfu.itis.paramonov.androidtasks.domain.model.forecast.ForecastDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.weather.WeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.WeatherRepository
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherApi,
    private val weatherDomainModelMapper: WeatherDomainModelMapper,
    private val forecastDomainModelMapper: ForecastDomainModelMapper,
    private val resourceManager: ResourceManager,
): WeatherRepository {
    override suspend fun getCurrentWeatherByCity(city: String): WeatherDomainModel {
        val domainModel = weatherDomainModelMapper.mapResponseToDomainModel(
            input = api.getCurrentWeatherByCity(city = city)
        )
        return if (domainModel != null && domainModel.isEmpty().not()) {
            domainModel
        } else {
            throw EmptyWeatherResponseException(message = resourceManager.getString(R.string.empty_weather_response))
        }
    }

    override suspend fun getForecastByCoordinates(longitude: Double, latitude: Double): ForecastDomainModel {
        val domainModel = forecastDomainModelMapper.mapResponseToDomainModel(
            input = api.getForecastByCoordinates(longitude = longitude, latitude = latitude)
        )
        return if (domainModel != null && domainModel.isEmpty().not()) {
            domainModel
        }
        else throw EmptyWeatherResponseException(message = resourceManager.getString(R.string.empty_forecast_response))
    }

}