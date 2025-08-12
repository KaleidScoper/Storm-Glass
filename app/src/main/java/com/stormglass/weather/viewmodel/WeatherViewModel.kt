package com.stormglass.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stormglass.weather.api.WeatherApiService
import com.stormglass.weather.model.WeatherData
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

class WeatherViewModel : ViewModel() {
    
    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> = _weatherData
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    // OpenWeatherMap API密钥 - 请替换为您的实际API密钥
    private val API_KEY = "0b88a198e71f4de0383e21afe6312d1e"
    
    private val weatherApiService: WeatherApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        retrofit.create(WeatherApiService::class.java)
    }
    
    fun fetchWeather(cityName: String) {
        if (API_KEY == "YOUR_OPENWEATHER_API_KEY_HERE") {
            Log.d("WeatherViewModel", "API密钥未配置，使用模拟数据")
            useSimulatedWeatherData(cityName)
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                _errorMessage.postValue("")
                
                Log.d("WeatherViewModel", "开始获取天气数据，城市: $cityName")
                
                val response = weatherApiService.getCurrentWeather(cityName, API_KEY)
                
                Log.d("WeatherViewModel", "API响应: $response")
                
                if (response.cod == 200) {
                    val weatherData = WeatherData.fromOpenWeatherResponse(response)
                    if (weatherData != null) {
                        _weatherData.postValue(weatherData!!) // Applied fix here
                        Log.d("WeatherViewModel", "天气数据获取成功: $weatherData")
                    } else {
                        Log.e("WeatherViewModel", "无法解析天气数据")
                        _errorMessage.postValue("数据解析失败")
                        useSimulatedWeatherData(cityName)
                    }
                } else {
                    Log.e("WeatherViewModel", "API返回错误: ${response.cod}")
                    _errorMessage.postValue("API错误: ${response.cod}")
                    useSimulatedWeatherData(cityName)
                }
                
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "网络请求失败", e)
                _errorMessage.postValue("网络请求失败: ${e.message}")
                useSimulatedWeatherData(cityName)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    private fun useSimulatedWeatherData(cityName: String) {
        Log.d("WeatherViewModel", "使用模拟天气数据")
        
        val simulatedWeather = WeatherData(
            temperature = 22.0,
            humidity = 65,
            windSpeed = 3.0,
            description = "模拟天气数据",
            weatherType = "sunny",
            season = getCurrentSeason(),
            city = cityName,
            reportTime = "模拟数据"
        )
        
        _weatherData.postValue(simulatedWeather)
    }
    
    private fun getCurrentSeason(): String {
        val month = java.time.LocalDate.now().monthValue
        return when (month) {
            in 3..5 -> "spring"
            in 6..8 -> "summer"
            in 9..11 -> "autumn"
            else -> "winter"
        }
    }
    
    fun clearError() {
        _errorMessage.postValue("")
    }
}
