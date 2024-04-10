package ru.kpfu.itis.paramonov.androidtasks.data.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.kpfu.itis.paramonov.androidtasks.data.model.response.ResponseData
import ru.kpfu.itis.paramonov.androidtasks.data.response.ResponseDataLog
import ru.kpfu.itis.paramonov.androidtasks.utils.bodyCopy
import javax.inject.Inject


class ResponseInterceptor @Inject constructor(
    private val responseDataLog: ResponseDataLog
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = chain.proceed(request)

        responseDataLog.addResponse(getFromRequestAndResponse(request, response))
        return response
    }

    private fun getFromRequestAndResponse(request: Request, response: Response): ResponseData {
        return ResponseData(
            request.method,
            request.url.toString(),
            request.headers.toMap(),
            request.body.toString(),
            response.bodyCopy?.string() ?: "null"
        )
    }
}