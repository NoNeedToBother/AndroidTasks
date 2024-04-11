package ru.kpfu.itis.paramonov.androidtasks.data.mapper

import okhttp3.Request
import okhttp3.Response
import ru.kpfu.itis.paramonov.androidtasks.data.model.response.ResponseData
import ru.kpfu.itis.paramonov.androidtasks.utils.bodyCopy
import javax.inject.Inject

class ResponseDataMapper @Inject constructor() {
    fun getFromRequestAndResponse(request: Request, response: Response): ResponseData {
        return ResponseData(
            response.code,
            request.method,
            request.url.toString(),
            request.headers.toMap(),
            request.body.toString(),
            response.bodyCopy?.string() ?: "null"
        )
    }
}