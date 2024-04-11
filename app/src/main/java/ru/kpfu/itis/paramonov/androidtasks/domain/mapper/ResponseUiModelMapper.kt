package ru.kpfu.itis.paramonov.androidtasks.domain.mapper

import ru.kpfu.itis.paramonov.androidtasks.domain.model.response.ResponseDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.response.ResponseStatusDomainModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseStatusUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import javax.inject.Inject

class ResponseUiModelMapper @Inject constructor() {

    fun mapDomainToUiModel(input: ResponseDomainModel): ResponseUiModel {
        return ResponseUiModel(
            input.method,
            getResponseStatus(input.status),
            input.url,
            input.headers,
            input.requestBody,
            input.responseBody
        )
    }

    private fun getResponseStatus(status: ResponseStatusDomainModel): ResponseStatusUiModel {
        return when (status) {
            ResponseStatusDomainModel.ERROR -> ResponseStatusUiModel.ERROR
            ResponseStatusDomainModel.NEUTRAL -> ResponseStatusUiModel.NEUTRAL
            ResponseStatusDomainModel.SUCCESS -> ResponseStatusUiModel.SUCCESS
        }
    }
}