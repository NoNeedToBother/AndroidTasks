package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.data.handler.ExceptionHandlerDelegate
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetWeatherDataUseCase
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : ViewModel() {

    private val _currentWeatherFlow = MutableStateFlow<WeatherUiModel?>(null)
    val currentWeatherFlow: StateFlow<WeatherUiModel?>
        get() = _currentWeatherFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: StateFlow<Boolean>
        get() = _loadingFlow

    val errorsChannel = Channel<Throwable>()

    fun getWeatherInfo(city: String) {
        _loadingFlow.value = true
        viewModelScope.launch {
            try {
                _currentWeatherFlow.value = getWeatherDataUseCase.invoke(city)
            } catch (ex: Exception) {
                val resEx = exceptionHandlerDelegate.handleException(ex)
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