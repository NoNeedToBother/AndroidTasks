package ru.kpfu.itis.paramonov.androidtasks.data.mapper

import ru.kpfu.itis.paramonov.androidtasks.data.model.response.ResponseData
import ru.kpfu.itis.paramonov.androidtasks.domain.model.response.ResponseDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.model.response.ResponseStatusDomainModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Params
import javax.inject.Inject

class ResponseDomainModelMapper @Inject constructor() {
    fun mapResponseToDomainModel(input: ResponseData): ResponseDomainModel {
        return ResponseDomainModel(
            input.method,
            input.code,
            getResponseStatus(input.code),
            input.url,
            input.headers,
            input.requestBody,
            input.responseBody
        )
    }

    private fun getResponseStatus(code: Int): ResponseStatusDomainModel {
        return when (code) {
            in Params.RESPONSE_INFO_CODE_LOWER_BOUND..Params.RESPONSE_INFO_CODE_UPPER_BOUND ->
                ResponseStatusDomainModel.NEUTRAL
            in Params.RESPONSE_SUCCESS_CODE_LOWER_BOUND..Params.RESPONSE_SUCCESS_CODE_UPPER_BOUND ->
                ResponseStatusDomainModel.SUCCESS
            in Params.RESPONSE_REDIRECTION_CODE_LOWER_BOUND..Params.RESPONSE_REDIRECTION_CODE_UPPER_BOUND ->
                ResponseStatusDomainModel.NEUTRAL
            in Params.RESPONSE_CLIENT_ERROR_CODE_LOWER_BOUND..Params.RESPONSE_CLIENT_ERROR_CODE_UPPER_BOUND ->
                ResponseStatusDomainModel.ERROR
            in Params.RESPONSE_SERVER_ERROR_CODE_LOWER_BOUND..Params.RESPONSE_SERVER_ERROR_CODE_UPPER_BOUND ->
                ResponseStatusDomainModel.ERROR
            else -> ResponseStatusDomainModel.NEUTRAL
        }
    }
}