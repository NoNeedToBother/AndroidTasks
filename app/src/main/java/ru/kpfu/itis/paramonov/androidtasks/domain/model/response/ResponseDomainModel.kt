package ru.kpfu.itis.paramonov.androidtasks.domain.model.response

data class ResponseDomainModel(
    val method: String,
    val status: ResponseStatusDomainModel,
    val url: String,
    val headers: Map<String, String>,
    val requestBody: String,
    val responseBody: String
)