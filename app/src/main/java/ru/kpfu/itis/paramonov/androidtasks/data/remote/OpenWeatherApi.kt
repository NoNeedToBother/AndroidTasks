package ru.kpfu.itis.paramonov.androidtasks.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.paramonov.androidtasks.data.model.WeatherResponse

interface OpenWeatherApi {
    @GET("weather")
    suspend fun getCurrentWeatherByCity(
        @Query(value = "q") city: String,
    ): WeatherResponse?
}