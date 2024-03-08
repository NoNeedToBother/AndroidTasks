package ru.kpfu.itis.paramonov.androidtasks.data.repository

import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.data.mapper.WeatherDomainModelMapper
import ru.kpfu.itis.paramonov.androidtasks.data.remote.OpenWeatherApi
import ru.kpfu.itis.paramonov.androidtasks.domain.model.WeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.WeatherRepository
import java.lang.RuntimeException

class WeatherRepositoryImpl(
    private val api: OpenWeatherApi,
    private val domainModelMapper: WeatherDomainModelMapper,
    //private val resManager: ResManager,
): WeatherRepository {
    override suspend fun getCurrentWeatherByCity(city: String): WeatherDomainModel {
        val domainModel = domainModelMapper.mapResponseToDomainModel(
            input = api.getCurrentWeatherByCity(city = city)
        )
        return if (domainModel != null && domainModel.isEmptyResponse().not()) {
            domainModel
        } else {
            throw RuntimeException()
            //throw EmptyWeatherResponseException(message = resManager.getString(R.string.empty_weather_response))
        }
    }

}