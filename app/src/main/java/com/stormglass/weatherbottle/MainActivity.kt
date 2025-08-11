package com.stormglass.weatherbottle

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.stormglass.weatherbottle.api.WeatherApiService
import com.stormglass.weatherbottle.data.WeatherType as DataWeatherType
import com.stormglass.weatherbottle.data.toWeatherType // Added import
import com.stormglass.weatherbottle.databinding.ActivityMainBinding
import com.stormglass.weatherbottle.repository.WeatherRepository
import com.stormglass.weatherbottle.views.StormBottleView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherRepository: WeatherRepository
    private var userIdentity: StormBottleView.UserIdentity = StormBottleView.UserIdentity.LEMON
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupWeatherApi()
        setupUI()
        updateCurrentTime()
    }
    
    private fun setupWeatherApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://devapi.qweather.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        val weatherApiService = retrofit.create(WeatherApiService::class.java)
        weatherRepository = WeatherRepository(weatherApiService)
    }
    
    private fun setupUI() {
        // 柠檬按钮点击事件
        binding.btnLemon.setOnClickListener {
            userIdentity = StormBottleView.UserIdentity.LEMON
            showStormBottle()
            loadWuhanWeather()
        }
        
        // 西红柿按钮点击事件
        binding.btnTomato.setOnClickListener {
            userIdentity = StormBottleView.UserIdentity.TOMATO
            showStormBottle()
            loadHefeiWeather()
        }
        
        // 信息栏展开/收起点击事件
        binding.topInfoLayout.setOnClickListener {
            toggleInfoExpansion()
        }
        
        // 天气切换按钮点击事件
        binding.btnSunny.setOnClickListener {
            binding.stormBottleView.setWeatherType(StormBottleView.WeatherType.SUNNY)
        }
        
        binding.btnCloudy.setOnClickListener {
            binding.stormBottleView.setWeatherType(StormBottleView.WeatherType.CLOUDY)
        }
        
        binding.btnRainy.setOnClickListener {
            binding.stormBottleView.setWeatherType(StormBottleView.WeatherType.RAINY)
        }
    }
    
    private fun toggleInfoExpansion() {
        val expandedInfo = binding.expandedInfo
        val expandArrow = binding.ivExpandArrow
        
        if (expandedInfo.visibility == View.VISIBLE) {
            // 收起信息
            expandedInfo.visibility = View.GONE
            expandArrow.animate()
                .rotation(0f)
                .setDuration(200)
                .start()
        } else {
            // 展开信息
            expandedInfo.visibility = View.VISIBLE
            expandArrow.animate()
                .rotation(180f)
                .setDuration(200)
                .start()
        }
    }
    
    private fun showStormBottle() {
        binding.identitySelectionLayout.visibility = View.GONE
        binding.stormBottleLayout.visibility = View.VISIBLE
        
        // 设置风暴瓶视图的用户身份
        binding.stormBottleView.setUserIdentity(userIdentity)
        
        // 更新位置信息
        val locationText = when (userIdentity) {
            StormBottleView.UserIdentity.LEMON -> "武汉洪山区"
            StormBottleView.UserIdentity.TOMATO -> "合肥蜀山区"
        }
        binding.tvLocation.text = locationText
    }
    
    private fun loadWuhanWeather() {
        lifecycleScope.launch {
            try {
                val weatherResponse = weatherRepository.getWuhanWeather()
                if (weatherResponse != null) {
                    updateWeatherUI(weatherResponse)
                } else {
                    showError("无法获取武汉天气信息")
                }
            } catch (e: Exception) {
                showError("获取天气信息失败: ${e.message}")
            }
        }
    }
    
    private fun loadHefeiWeather() {
        lifecycleScope.launch {
            try {
                val weatherResponse = weatherRepository.getHefeiWeather()
                if (weatherResponse != null) {
                    updateWeatherUI(weatherResponse)
                } else {
                    showError("无法获取合肥天气信息")
                }
            } catch (e: Exception) {
                showError("获取天气信息失败: ${e.message}")
            }
        }
    }
    
    private fun updateWeatherUI(weatherResponse: com.stormglass.weatherbottle.data.WeatherResponse) {
        val current = weatherResponse.now
        val location = weatherResponse.location.firstOrNull()
        
        // 更新温度
        binding.tvTemperature.text = "${current.temp}°C"
        
        // 更新湿度
        binding.tvHumidity.text = "${current.humidity}%"
        
        // 更新风速
        binding.tvWindSpeed.text = "${current.windSpeed}m/s"
        
        // 更新天气描述
        binding.tvWeatherDescription.text = current.text
        
        // 更新风暴瓶的天气类型
        val weatherType = when (current.toWeatherType()) {
            DataWeatherType.SUNNY -> StormBottleView.WeatherType.SUNNY
            DataWeatherType.CLOUDY -> StormBottleView.WeatherType.CLOUDY
            DataWeatherType.RAINY -> StormBottleView.WeatherType.RAINY
            DataWeatherType.SNOWY -> StormBottleView.WeatherType.SNOWY
            DataWeatherType.UNKNOWN -> StormBottleView.WeatherType.SUNNY
            else -> StormBottleView.WeatherType.SUNNY 
        }
        binding.stormBottleView.setWeatherType(weatherType)
        
        // 更新时间
        updateCurrentTime()
    }
    
    private fun updateCurrentTime() {
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA)
        val currentTime = dateFormat.format(Date())
        binding.tvCurrentTime.text = currentTime
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    override fun onResume() {
        super.onResume()
        // 每分钟更新时间
        startTimeUpdate()
    }
    
    override fun onPause() {
        super.onPause()
        stopTimeUpdate()
    }
    
    private var timeUpdateJob: android.os.Handler? = null
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            updateCurrentTime()
            timeUpdateJob?.postDelayed(this, 60000) // 60秒更新一次
        }
    }
    
    private fun startTimeUpdate() {
        timeUpdateJob = android.os.Handler(android.os.Looper.getMainLooper())
        timeUpdateJob?.post(timeUpdateRunnable)
    }
    
    private fun stopTimeUpdate() {
        timeUpdateJob?.removeCallbacks(timeUpdateRunnable)
        timeUpdateJob = null
    }
}
