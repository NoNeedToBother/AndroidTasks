package ru.kpfu.itis.paramonov.androidtasks.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.domain.mapper.WeatherUiModelMapper
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.WeatherRepository
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository,
    private val mapper: WeatherUiModelMapper,
) {
    suspend operator fun invoke(city: String): WeatherUiModel {
        return withContext(dispatcher) {
            val weatherData = repository.getCurrentWeatherByCity(city = city)
            mapper.mapDomainToUiModel(weatherData)
        }
    }
}