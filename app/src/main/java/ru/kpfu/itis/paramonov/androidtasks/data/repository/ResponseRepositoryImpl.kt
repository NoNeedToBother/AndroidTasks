package ru.kpfu.itis.paramonov.androidtasks.data.repository

import ru.kpfu.itis.paramonov.androidtasks.data.model.response.ResponseData
import ru.kpfu.itis.paramonov.androidtasks.data.response.ResponseDataLog
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.ResponseRepository
import javax.inject.Inject

class ResponseRepositoryImpl @Inject constructor(
    private val log: ResponseDataLog
): ResponseRepository {
    override fun getResponses(): List<ResponseData> {
        return log.responseDataList
    }
}