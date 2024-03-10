package ru.kpfu.itis.paramonov.androidtasks.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.utils.Params

class AppIdInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url.newBuilder()
            .addQueryParameter(Params.APP_ID_KEY, BuildConfig.OPEN_WEATHER_BASE_URL)
            .build()

        val requestBuilder = chain.request().newBuilder().url(newUrl)

        return chain.proceed(requestBuilder.build())
    }
}