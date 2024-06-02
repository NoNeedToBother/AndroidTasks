package ru.kpfu.itis.paramonov.androidtasks.data.model.response

data class ResponseData(
    val code: Int,
    val method: String,
    val url: String,
    val headers: Map<String, String>,
    val requestBody: String,
    val responseBody: String
)