package com.stormglass.weather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stormglass.weather.api.WeatherApiService
import com.stormglass.weather.model.WeatherData
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // 和风天气API密钥 - 请替换为您的实际API密钥
    // 我的是27ca5c2719a243148a7318f89455baa2
    // private val API_KEY = "YOUR_API_KEY_HERE"
    // 请在此处填入您的和风天气API密钥
    private val API_KEY = "27ca5c2719a243148a7318f89455baa2"

    private val weatherApiService: WeatherApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://devapi.qweather.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    fun fetchWeather(city: String) {
        // API密钥已正确配置，直接进行网络请求
        Log.d("WeatherViewModel", "开始获取天气数据，城市: $city, API密钥: ${API_KEY.take(8)}...")
        
        _isLoading.postValue(true)
        _errorMessage.postValue("")

        viewModelScope.launch {
            try {
                val location = when (city) {
                    "wuhan" -> "101200101" // 武汉洪山区
                    "hefei" -> "101220101" // 合肥蜀山区
                    else -> "101200101"
                }
                
                Log.d("WeatherViewModel", "正在获取城市 $city 的天气数据，location ID: $location...")
                Log.d("WeatherViewModel", "API请求URL: https://devapi.qweather.com/v7/weather/now?location=$location&key=${API_KEY.take(8)}...")
                
                val response = weatherApiService.getCurrentWeather(location, API_KEY)
                
                Log.d("WeatherViewModel", "API响应: code=${response.code}, 天气描述=${response.now.text}")
                
                if (response.code == "200") {
                    val weather = response.now
                    val weatherType = mapWeatherTextToType(weather.text)
                    
                    Log.d("WeatherViewModel", "天气类型映射: ${weather.text} -> $weatherType")
                    
                    val weatherData = WeatherData(
                        temperature = weather.temp.toDoubleOrNull() ?: 0.0,
                        humidity = weather.humidity.toIntOrNull() ?: 0,
                        windSpeed = weather.windSpeed.toDoubleOrNull() ?: 0.0,
                        description = weather.text,
                        weatherType = weatherType
                    )
                    
                    Log.d("WeatherViewModel", "成功获取天气数据: $weatherData")
                    _weatherData.postValue(weatherData)
                    _errorMessage.postValue("")
                } else {
                    Log.e("WeatherViewModel", "API返回错误: ${response.code}")
                    _errorMessage.postValue("API返回错误: ${response.code}")
                    useSimulatedWeatherData(city)
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "获取天气数据失败", e)
                Log.e("WeatherViewModel", "异常类型: ${e.javaClass.simpleName}")
                Log.e("WeatherViewModel", "异常消息: ${e.message}")
                Log.e("WeatherViewModel", "异常堆栈: ${e.stackTrace.take(5).joinToString("\n")}")
                _errorMessage.postValue("网络请求失败: ${e.message}")
                useSimulatedWeatherData(city)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun useSimulatedWeatherData(city: String) {
        // 使用模拟数据，基于城市和时间
        val currentHour = java.time.LocalTime.now().hour
        val weatherType = when {
            currentHour in 6..18 -> "sunny" // 白天晴天
            currentHour in 19..22 -> "cloudy" // 晚上多云
            else -> "rainy" // 深夜雨天
        }
        
        val temperature = when (city) {
            "wuhan" -> 25.0 // 武汉温度
            "hefei" -> 23.0 // 合肥温度
            else -> 20.0
        }
        
        val simulatedWeather = WeatherData(
            temperature = temperature,
            humidity = 65,
            windSpeed = 2.5,
            description = "模拟天气数据",
            weatherType = weatherType
        )
        
        Log.d("WeatherViewModel", "使用模拟天气数据: $simulatedWeather")
        _weatherData.postValue(simulatedWeather)
    }

    private fun mapWeatherTextToType(weatherText: String): String {
        return when {
            weatherText.contains("晴") -> "sunny"
            weatherText.contains("雨") -> "rainy"
            weatherText.contains("云") || weatherText.contains("阴") -> "cloudy"
            weatherText.contains("雪") -> "snowy"
            weatherText.contains("雾") || weatherText.contains("霾") -> "cloudy"
            weatherText.contains("雷") -> "rainy"
            else -> "sunny"
        }
    }

    fun clearError() {
        _errorMessage.postValue("")
    }
}
