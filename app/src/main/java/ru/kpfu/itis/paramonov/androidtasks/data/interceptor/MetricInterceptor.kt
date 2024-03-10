package ru.kpfu.itis.paramonov.androidtasks.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.paramonov.androidtasks.utils.Params

class MetricInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url.newBuilder()
            .addQueryParameter(Params.UNITS_KEY, Params.METRIC_UNITS_KEY)
            .build()

        val requestBuilder = chain.request().newBuilder().url(newUrl)

        return chain.proceed(requestBuilder.build())
    }

}