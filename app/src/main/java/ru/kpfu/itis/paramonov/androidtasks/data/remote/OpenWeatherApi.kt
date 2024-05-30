package ru.kpfu.itis.paramonov.androidtasks.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.paramonov.androidtasks.data.model.forecast.ForecastResponse
import ru.kpfu.itis.paramonov.androidtasks.data.model.weather.WeatherResponse

interface OpenWeatherApi {
    @GET("weather")
    suspend fun getCurrentWeatherByCity(
        @Query(value = "q") city: String,
    ): WeatherResponse?

    @GET("forecast")
    suspend fun getForecastByCoordinates(
        @Query(value = "lon") longitude: Double,
        @Query(value = "lat") latitude: Double
    ): ForecastResponse?
}