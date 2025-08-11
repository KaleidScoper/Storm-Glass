package com.stormglass.weatherbottle.api

import com.stormglass.weatherbottle.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v7/weather/now")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("location") location: String,
        @Query("lang") lang: String = "zh"
    ): WeatherResponse
}
