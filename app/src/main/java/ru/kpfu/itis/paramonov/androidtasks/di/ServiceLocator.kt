package ru.kpfu.itis.paramonov.androidtasks.di

import android.content.Context
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.data.interceptor.AppIdInterceptor
import ru.kpfu.itis.paramonov.androidtasks.data.mapper.WeatherDomainModelMapper
import ru.kpfu.itis.paramonov.androidtasks.data.remote.OpenWeatherApi
import ru.kpfu.itis.paramonov.androidtasks.data.repository.WeatherRepositoryImpl
import ru.kpfu.itis.paramonov.androidtasks.domain.mapper.WeatherUiModelMapper
import ru.kpfu.itis.paramonov.androidtasks.domain.usecase.GetWeatherDataUseCase
import ru.kpfu.itis.paramonov.androidtasks.utils.Keys
import java.lang.ref.WeakReference

object ServiceLocator {
    private lateinit var weatherApi: OpenWeatherApi

    private lateinit var okHttpClient: OkHttpClient

    private lateinit var weatherRepository: WeatherRepositoryImpl

    lateinit var getWeatherUseCase: GetWeatherDataUseCase
        private set

    private val dispatcher = Dispatchers.IO

    private val weatherDomainModelMapper = WeatherDomainModelMapper()

    private val weatherUiModelMapper = WeatherUiModelMapper()

    private var ctxRef: WeakReference<Context>? = null

    fun provideContext(): Context {
        return ctxRef?.get() ?: throw IllegalStateException("Context is null")
    }

    fun initDataDependencies(ctx: Context) {
        ctxRef = WeakReference(ctx)

        buildOkHttpClient()
        initWeatherApi()

        weatherRepository = WeatherRepositoryImpl(
            api = weatherApi,
            domainModelMapper = weatherDomainModelMapper,
            //resManager = ResManager(ctx = ctx),
        )
    }

    fun initDomainDependencies() {
        getWeatherUseCase = GetWeatherDataUseCase(
            dispatcher = dispatcher,
            repository = weatherRepository,
            mapper = weatherUiModelMapper,
        )
    }


    private fun buildOkHttpClient() {
        val clientBuilder = OkHttpClient.Builder().addInterceptor(AppIdInterceptor())
            .addInterceptor { chain ->
                val newUrl = chain.request().url.newBuilder()
                    .addQueryParameter(Keys.UNITS_KEY, "metric")
                    .build()

                val requestBuilder = chain.request().newBuilder().url(newUrl)

                chain.proceed(requestBuilder.build())
            }

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        okHttpClient = clientBuilder.build()
    }

    private fun initWeatherApi() {
        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.OPEN_WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = builder.create(OpenWeatherApi::class.java)
    }
}