package ru.kpfu.itis.paramonov.androidtasks.domain.repository

import ru.kpfu.itis.paramonov.androidtasks.domain.model.response.ResponseDomainModel

interface ResponseRepository {

    fun getResponses(): List<ResponseDomainModel>

    fun get(pos: Int): ResponseDomainModel
}