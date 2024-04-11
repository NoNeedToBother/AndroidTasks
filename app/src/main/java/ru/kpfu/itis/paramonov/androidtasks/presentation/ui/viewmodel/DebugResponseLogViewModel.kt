package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetResponsesLogUseCase
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import java.lang.Exception

class DebugResponseLogViewModel(
    private val getResponsesLogUseCase: GetResponsesLogUseCase
): BaseViewModel() {

    private val _responseLogFlow = MutableStateFlow<ResponseDataResult?>(null)

    val responseLogFlow: StateFlow<ResponseDataResult?> get() = _responseLogFlow

    fun getResponses() {
        _responseLogFlow.value = null

        viewModelScope.launch {
            try {
                val responses = getResponsesLogUseCase.invoke()
                _responseLogFlow.value = ResponseDataResult.Success(responses)
            } catch (ex: Exception) {
                _responseLogFlow.value = ResponseDataResult.Failure(ex)
            }
        }
    }

    sealed interface ResponseDataResult: Result {
        class Success(private val result: List<ResponseUiModel>): ResponseDataResult, Result.Success<List<ResponseUiModel>> {
            override fun getValue(): List<ResponseUiModel> = result
        }
        class Failure(private val ex: Exception): ResponseDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}