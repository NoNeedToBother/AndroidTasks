package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.data.handler.ExceptionHandlerDelegate
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetWeatherDataUseCase
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Params
import java.util.Timer
import java.util.TimerTask

class WeatherViewModel @AssistedInject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    @Assisted(Params.CITIES_LIST_KEY) private val cities: List<String>
) : BaseViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(Params.CITIES_LIST_KEY) cities: List<String>): WeatherViewModel
    }

    private val _currentWeatherFlow = MutableStateFlow<WeatherDataResult?>(null)
    val currentWeatherFlow: StateFlow<WeatherDataResult?>
        get() = _currentWeatherFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: StateFlow<Boolean>
        get() = _loadingFlow

    private var timer: Timer? = null

    fun startCollectingWeatherInfo() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                _currentWeatherFlow.value = null
                _loadingFlow.value = true
                getWeatherInfo()
            }
        }, 100, DELAY)
    }

    fun stopCollectingWeatherInfo() {
        timer?.cancel()
    }

    private fun getWeatherInfo() {
        viewModelScope.launch {
            try {
                val weatherUiModelList: MutableList<WeatherUiModel> = mutableListOf()
                for (city in cities) {
                    weatherUiModelList.add(getWeatherInfo(city))
                }
                _currentWeatherFlow.value = WeatherDataResult.Success(weatherUiModelList)
            } catch (ex: Exception) {
                val resEx = exceptionHandlerDelegate.handleException(ex)
                _currentWeatherFlow.value = WeatherDataResult.Failure(resEx)
            } finally {
                _loadingFlow.value = false
            }
        }
    }

    private suspend fun getWeatherInfo(city: String): WeatherUiModel {
        return getWeatherDataUseCase.invoke(city)
    }

    sealed class WeatherDataResult {
        class Success(private val result: MutableList<WeatherUiModel>): WeatherDataResult(), Result.Success<MutableList<WeatherUiModel>> {
            override fun getValue(): MutableList<WeatherUiModel> = result
        }
        class Failure(private val ex: Throwable): WeatherDataResult(), Result.Failure {
            override fun getException(): Throwable = ex
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopCollectingWeatherInfo()
        timer = null
    }

    companion object {
        private const val DELAY = 10 * 60 * 1000L
    }
}