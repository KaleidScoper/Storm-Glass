package com.stormglass.weatherbottle.repository

import com.stormglass.weatherbottle.api.WeatherApiService
import com.stormglass.weatherbottle.data.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val apiService: WeatherApiService) {
    
    companion object {
        // 和风天气API密钥 - 需要在 https://dev.qweather.com/ 注册获取
        private const val API_KEY = "27ca5c2719a243148a7318f89455baa2"
        
        // 和风天气使用城市ID，更稳定
        private const val WUHAN_HONGSHAN = "101200107" // 武汉洪山区
        private const val HEFEI_SHUSHAN = "101220106" // 合肥蜀山区
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
