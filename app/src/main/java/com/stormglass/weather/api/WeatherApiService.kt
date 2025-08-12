package com.stormglass.weather.api

import retrofit2.http.GET
import retrofit2.http.Query

// OpenWeatherMap API接口
interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_cn"
    ): OpenWeatherResponse
}

// OpenWeatherMap响应数据
data class OpenWeatherResponse(
    val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    val main: Main?,
    val visibility: Int?,
    val wind: Wind?,
    val clouds: Clouds?,
    val dt: Long?,
    val sys: Sys?,
    val timezone: Int?,
    val id: Int?,
    val name: String?,
    val cod: Int?
)

data class Coord(
    val lon: Double?,
    val lat: Double?
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Main(
    val temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val humidity: Int?
)

data class Wind(
    val speed: Double?,
    val deg: Int?
)

data class Clouds(
    val all: Int?
)

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)
