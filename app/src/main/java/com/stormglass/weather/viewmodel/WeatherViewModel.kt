package com.stormglass.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stormglass.weather.api.WeatherApiService
import com.stormglass.weather.api.OpenWeatherResponse
import com.stormglass.weather.model.WeatherData
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

class WeatherViewModel : ViewModel() {
    
    private val _weatherData = MutableLiveData<WeatherData?>()
    val weatherData: LiveData<WeatherData?> = _weatherData
    
    private val _backgroundWeatherData = MutableLiveData<WeatherData?>()
    val backgroundWeatherData: LiveData<WeatherData?> = _backgroundWeatherData
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    // OpenWeatherMap API密钥 - 请替换为您的实际API密钥
    // 注意：这个密钥可能已过期，请到 https://openweathermap.org/api 注册获取新的API密钥
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
        Log.d("WeatherViewModel", "开始获取瓶子天气数据，城市: $cityName")
        fetchWeatherInternal(cityName, isBackground = false)
    }
    
    fun fetchBackgroundWeather(cityName: String) {
        Log.d("WeatherViewModel", "开始获取背景天气数据，城市: $cityName")
        fetchWeatherInternal(cityName, isBackground = true)
    }
    
    private fun fetchWeatherInternal(cityName: String, isBackground: Boolean) {
        Log.d("WeatherViewModel", "开始获取天气数据，城市: $cityName, 背景: $isBackground")
        
        // 检查API密钥是否有效
        if (API_KEY == "YOUR_OPENWEATHER_API_KEY_HERE" || API_KEY.isEmpty()) {
            Log.d("WeatherViewModel", "API密钥未配置，使用模拟数据")
            _errorMessage.postValue("API密钥未配置，显示模拟数据")
            useSimulatedWeatherData(cityName, isBackground)
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                _errorMessage.postValue("")
                
                // 尝试多种城市名称格式
                val cityNames = listOf(cityName, getChineseCityName(cityName), getEnglishCityName(cityName))
                var response: OpenWeatherResponse? = null
                var lastError: String = ""
                
                for (city in cityNames) {
                    try {
                        Log.d("WeatherViewModel", "尝试获取天气数据，城市: $city")
                        response = weatherApiService.getCurrentWeather(city, API_KEY)
                        
                        if (response.cod == 200) {
                            Log.d("WeatherViewModel", "成功获取天气数据，城市: $city")
                            break
                        } else {
                            lastError = "API错误: ${response.cod}"
                            Log.e("WeatherViewModel", "API返回错误: ${response.cod} for city: $city")
                        }
                    } catch (e: Exception) {
                        lastError = "网络请求失败: ${e.message}"
                        Log.e("WeatherViewModel", "网络请求失败 for city: $city", e)
                    }
                }
                
                if (response?.cod == 200) {
                    Log.d("WeatherViewModel", "API响应成功: ${response.toString()}")
                    val weatherData = WeatherData.fromOpenWeatherResponse(response)
                    if (weatherData != null) {
                        if (isBackground) {
                            _backgroundWeatherData.postValue(weatherData)
                            Log.d("WeatherViewModel", "背景天气数据获取成功: $weatherData")
                        } else {
                            _weatherData.postValue(weatherData)
                            Log.d("WeatherViewModel", "瓶子天气数据获取成功: $weatherData")
                        }
                    } else {
                        Log.e("WeatherViewModel", "无法解析天气数据")
                        _errorMessage.postValue("数据解析失败")
                        useSimulatedWeatherData(cityName, isBackground)
                    }
                } else {
                    Log.e("WeatherViewModel", "所有城市名称都失败，最后错误: $lastError")
                    _errorMessage.postValue("获取真实天气失败: $lastError，显示模拟数据")
                    useSimulatedWeatherData(cityName, isBackground)
                }
                
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "网络请求失败", e)
                _errorMessage.postValue("网络请求失败: ${e.message}，显示模拟数据")
                useSimulatedWeatherData(cityName, isBackground)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    
    private fun getChineseCityName(englishName: String): String {
        return when (englishName.lowercase()) {
            "wuhan" -> "武汉"
            "hefei" -> "合肥"
            else -> englishName
        }
    }
    
    private fun getEnglishCityName(chineseName: String): String {
        return when (chineseName) {
            "武汉" -> "Wuhan"
            "合肥" -> "Hefei"
            else -> chineseName
        }
    }
    
    private fun useSimulatedWeatherData(cityName: String, isBackground: Boolean = false) {
        Log.d("WeatherViewModel", "使用模拟天气数据，背景: $isBackground")
        
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
        
        if (isBackground) {
            _backgroundWeatherData.postValue(simulatedWeather)
        } else {
            _weatherData.postValue(simulatedWeather)
        }
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
