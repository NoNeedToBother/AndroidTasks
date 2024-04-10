package ru.kpfu.itis.paramonov.androidtasks.domain.repository

import ru.kpfu.itis.paramonov.androidtasks.data.model.response.ResponseData

interface ResponseRepository {

    fun getResponses(): List<ResponseData>
}