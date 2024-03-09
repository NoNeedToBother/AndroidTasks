package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel

class WeatherViewModel : ViewModel() {

    private val _currentWeatherFlow = MutableStateFlow<WeatherUiModel?>(null)
    val currentWeatherFlow: StateFlow<WeatherUiModel?>
        get() = _currentWeatherFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: StateFlow<Boolean>
        get() = _loadingFlow

    val errorsChannel = Channel<Throwable>()

    fun getWeatherInfo(city: String) {
        //_currentWeatherFlow.value = null
        _loadingFlow.value = true
        viewModelScope.launch {
            try {
                _currentWeatherFlow.value = ServiceLocator.getWeatherUseCase.invoke(city)
            } catch (ex: Exception) {
                val resEx = ServiceLocator.exceptionHandlerDelegate.handleException(ex)
                errorsChannel.send(resEx)
            } finally {
                _loadingFlow.value = false
            }
        }
    }

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }
}