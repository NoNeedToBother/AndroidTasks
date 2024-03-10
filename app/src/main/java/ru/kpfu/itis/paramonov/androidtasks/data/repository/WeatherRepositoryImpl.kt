package ru.kpfu.itis.paramonov.androidtasks.data.repository

import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.data.exceptions.EmptyWeatherResponseException
import ru.kpfu.itis.paramonov.androidtasks.data.mapper.WeatherDomainModelMapper
import ru.kpfu.itis.paramonov.androidtasks.data.remote.OpenWeatherApi
import ru.kpfu.itis.paramonov.androidtasks.domain.model.WeatherDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.WeatherRepository
import ru.kpfu.itis.paramonov.androidtasks.utils.ResManager
import java.lang.RuntimeException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherApi,
    private val domainModelMapper: WeatherDomainModelMapper,
    private val resManager: ResManager,
): WeatherRepository {
    override suspend fun getCurrentWeatherByCity(city: String): WeatherDomainModel {
        val domainModel = domainModelMapper.mapResponseToDomainModel(
            input = api.getCurrentWeatherByCity(city = city)
        )
        return if (domainModel != null && domainModel.isEmptyResponse().not()) {
            domainModel
        } else {
            throw EmptyWeatherResponseException(message = resManager.getString(R.string.empty_weather_response))
        }
    }

}