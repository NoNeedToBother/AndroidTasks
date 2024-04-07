package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.data.handler.ExceptionHandlerDelegate
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetForecastDataUseCase
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetWeatherDataUseCase
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast.ForecastUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Keys

class CityWeatherViewModel @AssistedInject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val getForecastDataUseCase: GetForecastDataUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    @Assisted(Keys.LONGITUDE_KEY) private val longitude: Double,
    @Assisted(Keys.LATITUDE_KEY) private val latitude: Double,
    @Assisted(Keys.CITY_KEY) private val city: String
): BaseViewModel() {

    private val _currentWeatherFlow = MutableStateFlow<WeatherDataResult?>(null)
    val currentWeatherFlow: StateFlow<WeatherDataResult?> get() = _currentWeatherFlow

    private val _forecastFlow = MutableStateFlow<ForecastDataResult?>(null)
    val forecastFlow: StateFlow<ForecastDataResult?> get() = _forecastFlow

    private val _weatherLoadingFlow = MutableStateFlow(false)
    val weatherLoadingFlow: StateFlow<Boolean> get() = _weatherLoadingFlow

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(Keys.LONGITUDE_KEY) longitude: Double,
                   @Assisted(Keys.LATITUDE_KEY) latitude: Double,
                   @Assisted(Keys.CITY_KEY) city: String): CityWeatherViewModel
    }

    fun getWeatherInfo() {
        _currentWeatherFlow.value = null
        _weatherLoadingFlow.value = true
        viewModelScope.launch {
            try {
                val weather = getWeatherDataUseCase.invoke(city)
                _currentWeatherFlow.value = WeatherDataResult.Success(weather)
            } catch (ex: Throwable) {
                val resEx = exceptionHandlerDelegate.handleException(ex)
                _currentWeatherFlow.value = WeatherDataResult.Failure(resEx)
            } finally {
                _weatherLoadingFlow.value = false
            }
        }
    }

    fun getForecastInfo() {
        _forecastFlow.value = null
        viewModelScope.launch {
            try {
                val forecast = getForecastDataUseCase.invoke(longitude, latitude)
                _forecastFlow.value = ForecastDataResult.Success(forecast)
            } catch (ex: Throwable) {
                val resEx = exceptionHandlerDelegate.handleException(ex)
                _currentWeatherFlow.value = WeatherDataResult.Failure(resEx)
            }
        }
    }

    sealed class WeatherDataResult {
        class Success(private val result: WeatherUiModel): WeatherDataResult(), Result.Success<WeatherUiModel> {
            override fun getValue(): WeatherUiModel = result
        }
        class Failure(private val ex: Throwable): WeatherDataResult(), Result.Failure {
            override fun getException(): Throwable = ex
        }
    }

    sealed class ForecastDataResult {
        class Success(private val result: ForecastUiModel): ForecastDataResult(), Result.Success<ForecastUiModel> {
            override fun getValue(): ForecastUiModel = result
        }
        class Failure(private val ex: Throwable): ForecastDataResult(), Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}