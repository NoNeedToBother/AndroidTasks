package ru.kpfu.itis.paramonov.androidtasks.data.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.data.interceptor.AppIdInterceptor
import ru.kpfu.itis.paramonov.androidtasks.data.interceptor.MetricInterceptor
import ru.kpfu.itis.paramonov.androidtasks.data.interceptor.ResponseInterceptor
import ru.kpfu.itis.paramonov.androidtasks.data.remote.OpenWeatherApi
import ru.kpfu.itis.paramonov.androidtasks.data.response.ResponseDataLog
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun okHttpClient(
        appIdInterceptor: AppIdInterceptor,
        metricInterceptor: MetricInterceptor,
        responseInterceptor: ResponseInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(appIdInterceptor)
            .addInterceptor(metricInterceptor)
            .addInterceptor(responseInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun weatherApi(okHttpClient: OkHttpClient): OpenWeatherApi {
        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.OPEN_WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return builder.create(OpenWeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun responseDataLog(): ResponseDataLog {
        return ResponseDataLog()
    }
}