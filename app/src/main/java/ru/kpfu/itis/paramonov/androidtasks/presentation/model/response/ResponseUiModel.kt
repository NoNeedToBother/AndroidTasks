package ru.kpfu.itis.paramonov.androidtasks.presentation.model.response

data class ResponseUiModel(
    val method: String,
    val status: ResponseStatusUiModel,
    val url: String,
    val headers: Map<String, String>,
    val requestBody: String,
    val responseBody: String
)