package com.stormglass.weatherbottle.repository

import com.stormglass.weatherbottle.api.WeatherApiService
import com.stormglass.weatherbottle.data.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val apiService: WeatherApiService) {
    
    companion object {
        private const val API_KEY = "YOUR_WEATHER_API_KEY" // 需要替换为实际的API密钥
        private const val WUHAN_HONGSHAN = "30.5469,114.3419" // 武汉洪山区坐标
        private const val HEFEI_SHUSHAN = "31.8636,117.2654" // 合肥蜀山区坐标
    }
    
    suspend fun getWuhanWeather(): WeatherResponse? {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getCurrentWeather(API_KEY, WUHAN_HONGSHAN)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun getHefeiWeather(): WeatherResponse? {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getCurrentWeather(API_KEY, HEFEI_SHUSHAN)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
