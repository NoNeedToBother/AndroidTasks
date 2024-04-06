package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.data.handler.ExceptionHandlerDelegate
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetWeatherDataUseCase
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : BaseViewModel() {

    private val _currentWeatherFlow = MutableStateFlow<WeatherDataResult?>(null)
    val currentWeatherFlow: StateFlow<WeatherDataResult?>
        get() = _currentWeatherFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: StateFlow<Boolean>
        get() = _loadingFlow

    fun getWeatherInfo(city: String) {
        _currentWeatherFlow.value = null
        _loadingFlow.value = true
        viewModelScope.launch {
            try {
                val weatherUiModel = getWeatherDataUseCase.invoke(city)
                _currentWeatherFlow.value = WeatherDataResult.Success(listOf(weatherUiModel))
            } catch (ex: Exception) {
                val resEx = exceptionHandlerDelegate.handleException(ex)
                _currentWeatherFlow.value = WeatherDataResult.Failure(resEx)
            } finally {
                _loadingFlow.value = false
            }
        }
    }

    sealed class WeatherDataResult {
        class Success(private val result: List<WeatherUiModel>): WeatherDataResult(), Result.Success<List<WeatherUiModel>> {
            override fun getValue(): List<WeatherUiModel> = result
        }
        class Failure(private val ex: Throwable): WeatherDataResult(), Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}