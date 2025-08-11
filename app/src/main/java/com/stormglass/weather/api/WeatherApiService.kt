package com.stormglass.weather.api

import com.stormglass.weather.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    
    @GET("v7/weather/now")
    suspend fun getCurrentWeather(
        @Query("location") location: String,
        @Query("key") apiKey: String = "27ca5c2719a243148a7318f89455baa2"
    ): WeatherApiResponse
}

data class WeatherApiResponse(
    val code: String,
    val now: WeatherNow,
    val location: List<Location>
)

data class WeatherNow(
    val obsTime: String,
    val temp: String,
    val feelsLike: String,
    val icon: String,
    val text: String,
    val wind360: String,
    val windDir: String,
    val windScale: String,
    val windSpeed: String,
    val humidity: String,
    val precip: String,
    val pressure: String,
    val vis: String,
    val cloud: String,
    val dew: String
)

data class Location(
    val name: String,
    val id: String,
    val lat: String,
    val lon: String,
    val adm2: String,
    val adm1: String,
    val country: String,
    val tz: String,
    val utcOffset: String,
    val isDst: String,
    val type: String,
    val rank: String,
    val fxLink: String
)
