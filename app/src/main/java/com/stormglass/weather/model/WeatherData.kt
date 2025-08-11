package com.stormglass.weather.model

data class WeatherData(
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val description: String,
    val weatherType: String
)
