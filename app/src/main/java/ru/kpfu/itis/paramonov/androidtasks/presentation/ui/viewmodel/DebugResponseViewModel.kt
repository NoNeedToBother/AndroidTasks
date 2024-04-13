package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetResponseDataUseCase
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Keys

class DebugResponseViewModel @AssistedInject constructor(
    private val getResponseUseCase: GetResponseDataUseCase,
    @Assisted(Keys.POS_KEY) private val pos: Int
): BaseViewModel() {

    private val _responseFlow = MutableStateFlow<ResponseDataResult?>(null)
    val responseFlow: StateFlow<ResponseDataResult?> get() = _responseFlow

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(Keys.POS_KEY) pos: Int): DebugResponseViewModel
    }

    fun getResponseInfo() {
        _responseFlow.value = null
        viewModelScope.launch {
            try {
                val response = getResponseUseCase.invoke(pos)
                _responseFlow.value = ResponseDataResult.Success(response)
            } catch (ex: Exception) {
                _responseFlow.value = ResponseDataResult.Failure(ex)
            }
        }
    }

    sealed interface ResponseDataResult: Result {
        class Success(private val response: ResponseUiModel): ResponseDataResult, Result.Success<ResponseUiModel> {
            override fun getValue(): ResponseUiModel {
                return response
            }
        }
        class Failure(private val ex: Throwable): ResponseDataResult, Result.Failure {
            override fun getException(): Throwable {
                return ex
            }

        }
    }
}