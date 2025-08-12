package com.stormglass.weather.model

import com.stormglass.weather.api.OpenWeatherResponse
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
        fun fromOpenWeatherResponse(response: OpenWeatherResponse): WeatherData? {
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
            val text = weatherText.lowercase()
            return when {
                text.contains("晴") || text.contains("clear") || text.contains("sunny") -> "sunny"
                text.contains("雨") || text.contains("rain") || text.contains("drizzle") || text.contains("shower") -> "rainy"
                text.contains("雪") || text.contains("snow") || text.contains("sleet") -> "snowy"
                text.contains("云") || text.contains("cloud") || text.contains("阴") || text.contains("overcast") || text.contains("mist") || text.contains("fog") -> "cloudy"
                text.contains("雾") || text.contains("霾") || text.contains("haze") -> "cloudy"
                text.contains("雷") || text.contains("thunder") -> "rainy"
                else -> "sunny"
            }
        }
        
        private fun determineSeasonFromWeather(weatherText: String, temperature: Double): String {
            // 获取当前月份来更准确地判断季节
            val currentMonth = java.time.LocalDate.now().monthValue
            val currentDay = java.time.LocalDate.now().dayOfMonth
            
            // 根据月份和温度综合判断季节
            return when {
                // 夏季：6-8月，或温度很高
                currentMonth in 6..8 || temperature >= 25 -> "summer"
                // 春季：3-5月，或温度适中
                currentMonth in 3..5 || (temperature >= 15 && temperature < 25) -> "spring"
                // 秋季：9-11月，或温度较低
                currentMonth in 9..11 || (temperature >= 5 && temperature < 15) -> "autumn"
                // 冬季：12-2月，或温度很低
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
