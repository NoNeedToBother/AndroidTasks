package ru.kpfu.itis.paramonov.androidtasks.data.repository

import ru.kpfu.itis.paramonov.androidtasks.data.mapper.ResponseDomainModelMapper
import ru.kpfu.itis.paramonov.androidtasks.data.response.ResponseDataLog
import ru.kpfu.itis.paramonov.androidtasks.domain.model.response.ResponseDomainModel
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.ResponseRepository
import javax.inject.Inject

class ResponseRepositoryImpl @Inject constructor(
    private val log: ResponseDataLog,
    private val responseDomainModelMapper: ResponseDomainModelMapper
): ResponseRepository {
    override fun getResponses(): List<ResponseDomainModel> {
        return log.responseDataList.map { data ->
            responseDomainModelMapper.mapResponseToDomainModel(data)
        }.reversed()
    }

    override fun get(pos: Int): ResponseDomainModel {
        return getResponses()[pos]
    }
}