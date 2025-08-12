package com.stormglass.weather.model

import java.text.SimpleDateFormat
import java.util.*

data class WeatherData(
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val description: String,
    val weatherType: String,
    val season: String,
    val city: String,
    val reportTime: String
) {
    companion object {
        fun fromOpenWeatherResponse(response: com.stormglass.weather.api.OpenWeatherResponse): WeatherData? {
            if (response.cod != 200) return null
            
            val weather = response.weather?.firstOrNull()
            val main = response.main
            val wind = response.wind
            val cityName = response.name
            
            if (weather == null || main == null || cityName == null) return null
            
            val weatherType = mapWeatherTextToType(weather.description ?: "")
            val season = determineSeasonFromWeather(weather.description ?: "", main.temp ?: 20.0)
            val reportTime = formatTimestamp(response.dt ?: System.currentTimeMillis() / 1000)
            
            return WeatherData(
                temperature = main.temp ?: 0.0,
                humidity = main.humidity ?: 0,
                windSpeed = wind?.speed ?: 0.0,
                description = weather.description ?: "",
                weatherType = weatherType,
                season = season,
                city = cityName,
                reportTime = reportTime
            )
        }
        
        private fun mapWeatherTextToType(weatherText: String): String {
            return when {
                weatherText.contains("晴") || weatherText.contains("clear") -> "sunny"
                weatherText.contains("雨") || weatherText.contains("rain") -> "rainy"
                weatherText.contains("雪") || weatherText.contains("snow") -> "snowy"
                weatherText.contains("云") || weatherText.contains("cloud") || weatherText.contains("阴") -> "cloudy"
                else -> "sunny"
            }
        }
        
        private fun determineSeasonFromWeather(weatherText: String, temperature: Double): String {
            return when {
                temperature >= 25 -> "summer"
                temperature >= 15 -> "spring"
                temperature >= 5 -> "autumn"
                else -> "winter"
            }
        }
        
        private fun formatTimestamp(timestamp: Long): String {
            val date = Date(timestamp * 1000)
            val formatter = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
            return formatter.format(date)
        }
    }
}
