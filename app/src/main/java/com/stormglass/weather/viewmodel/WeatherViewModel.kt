package com.stormglass.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stormglass.weather.api.WeatherApiService
import com.stormglass.weather.model.WeatherData
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> = _weatherData

    private val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://devapi.qweather.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val location = when (city) {
                    "wuhan" -> "101200101" // 武汉洪山区
                    "hefei" -> "101220101" // 合肥蜀山区
                    else -> "101200101"
                }
                
                val response = weatherApiService.getCurrentWeather(location)
                
                if (response.code == "200") {
                    val weather = response.now
                    val weatherType = mapWeatherTextToType(weather.text)
                    
                    val weatherData = WeatherData(
                        temperature = weather.temp.toDoubleOrNull() ?: 0.0,
                        humidity = weather.humidity.toIntOrNull() ?: 0,
                        windSpeed = weather.windSpeed.toDoubleOrNull() ?: 0.0,
                        description = weather.text,
                        weatherType = weatherType
                    )
                    
                    _weatherData.postValue(weatherData)
                }
            } catch (e: Exception) {
                // 如果API调用失败，使用默认天气数据
                val defaultWeather = WeatherData(
                    temperature = 20.0,
                    humidity = 60,
                    windSpeed = 3.0,
                    description = "晴天",
                    weatherType = "sunny"
                )
                _weatherData.postValue(defaultWeather)
            }
        }
    }

    private fun mapWeatherTextToType(weatherText: String): String {
        return when {
            weatherText.contains("晴") -> "sunny"
            weatherText.contains("雨") -> "rainy"
            weatherText.contains("云") || weatherText.contains("阴") -> "cloudy"
            weatherText.contains("雪") -> "snowy"
            else -> "sunny"
        }
    }
}
