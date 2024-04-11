package ru.kpfu.itis.paramonov.androidtasks.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.paramonov.androidtasks.data.mapper.ResponseDataMapper
import ru.kpfu.itis.paramonov.androidtasks.data.response.ResponseDataLog
import javax.inject.Inject


class ResponseInterceptor @Inject constructor(
    private val responseDataLog: ResponseDataLog,
    private val responseDataMapper: ResponseDataMapper
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = chain.proceed(request)

        responseDataLog.addResponse(responseDataMapper.getFromRequestAndResponse(request, response))
        return response
    }
}