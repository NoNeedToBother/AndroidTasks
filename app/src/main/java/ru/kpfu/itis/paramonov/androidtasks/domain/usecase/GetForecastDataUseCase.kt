package ru.kpfu.itis.paramonov.androidtasks.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.domain.mapper.ForecastUiModelMapper
import ru.kpfu.itis.paramonov.androidtasks.domain.mapper.WeatherUiModelMapper
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.WeatherRepository
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast.ForecastUiModel
import javax.inject.Inject

class GetForecastDataUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository,
    private val mapper: ForecastUiModelMapper,
) {

    suspend operator fun invoke(longitude: Double, latitude: Double): ForecastUiModel {
        return withContext(dispatcher) {
            val forecastData = repository.getForecastByCoordinates(longitude, latitude)
            mapper.mapDomainToUiModel(forecastData)
        }
    }
}