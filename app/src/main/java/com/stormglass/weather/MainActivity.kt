package com.stormglass.weather

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.stormglass.weather.databinding.ActivityMainBinding
import com.stormglass.weather.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private var currentIdentity: String = "lemon" // 默认柠檬身份
    private var isUserSelectedWeather = false // 用户是否手动选择了天气
    private var userSelectedWeatherType = "sunny" // 用户选择的天气类型

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        
        setupIdentitySelection()
        setupStormBottle()
        setupWeatherButtons()
        setupSeasonButtons()
        setupExpressionButtons()
        
        // 开始表情动画
        startExpressionAnimation()
    }

    private fun setupIdentitySelection() {
        binding.lemonButton.setOnClickListener {
            currentIdentity = "lemon"
            showStormBottle()
            viewModel.fetchWeather("武汉") // 武汉天气
        }

        binding.tomatoButton.setOnClickListener {
            currentIdentity = "tomato"
            showStormBottle()
            viewModel.fetchWeather("合肥") // 合肥天气
        }
    }

    private fun showStormBottle() {
        // 隐藏身份选择界面
        binding.identitySelectionLayout.visibility = View.GONE
        
        // 显示风暴瓶界面
        binding.stormBottleLayout.visibility = View.VISIBLE
        
        // 根据身份更新UI
        updateUIForIdentity()
    }

    private fun updateUIForIdentity() {
        if (currentIdentity == "lemon") {
            // 柠檬身份：瓶子里显示西红柿
            binding.bigFruitBody.setImageResource(R.drawable.lemon_body)
            binding.bigFruitExpression.setImageResource(R.drawable.lemon_expression_happy)
            // 立即更新小水果为西红柿（柠檬身份看西红柿）
            binding.smallFruit.setImageResource(R.drawable.small_tomato_sunny)
        } else {
            // 西红柿身份：瓶子里显示柠檬
            binding.bigFruitBody.setImageResource(R.drawable.tomato_body)
            binding.bigFruitExpression.setImageResource(R.drawable.tomato_expression_happy)
            // 立即更新小水果为柠檬（西红柿身份看柠檬）
            binding.smallFruit.setImageResource(R.drawable.small_lemon_sunny)
        }
        
        // 确保小水果立即显示正确，不等待天气数据加载
        // 天气数据加载完成后，updateWeatherUI会再次更新小水果以反映天气变化
    }

    private fun setupStormBottle() {
        // 设置折叠/展开功能栏的点击事件
        setupCollapsibleToolbar()
        
        // 设置恢复真实天气按钮
        binding.btnResetWeather.setOnClickListener {
            resetToRealWeather()
        }
        
        // 观察天气数据变化
        viewModel.weatherData.observe(this) { weather ->
            weather?.let {
                binding.weatherDetails.text = """
                    城市: ${it.city}
                    温度: ${it.temperature}°C
                    湿度: ${it.humidity}%
                    风速: ${it.windSpeed} m/s
                    天气: ${it.description}
                    报告时间: ${it.reportTime}
                """.trimIndent()
                
                // 只有在用户没有手动选择天气时才更新UI
                if (!isUserSelectedWeather) {
                    updateWeatherUI(it.weatherType)
                    // 同时更新季节
                    updateSeasonUI(it.season)
                }
            }
        }

        // 观察加载状态
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.weatherDetails.text = "正在获取天气数据..."
            }
        }

        // 观察错误信息
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                binding.weatherDetails.text = """
                    ${binding.weatherDetails.text}
                    
                    注意: $errorMessage
                """.trimIndent()
            }
        }
    }

    private fun setupCollapsibleToolbar() {
        var isExpanded = false
        
        binding.collapsedToolbar.setOnClickListener {
            if (!isExpanded) {
                // 展开功能栏
                binding.expandedToolbar.visibility = View.VISIBLE
                binding.collapsedToolbar.visibility = View.GONE
                isExpanded = true
            }
        }

        // 添加关闭按钮的点击事件
        binding.closeButton.setOnClickListener {
            // 收起功能栏
            binding.expandedToolbar.visibility = View.GONE
            binding.collapsedToolbar.visibility = View.VISIBLE
            isExpanded = false
        }
    }

    private fun updateWeatherUI(weatherType: String) {
        // 只更新天气相关的内容，不修改季节
        // 更新小水果
        val smallFruitRes = if (currentIdentity == "lemon") {
            when (weatherType) {
                "sunny" -> R.drawable.small_tomato_sunny
                "rainy" -> R.drawable.small_tomato_rainy
                "cloudy" -> R.drawable.small_tomato_cloudy
                "snowy" -> R.drawable.small_tomato_snowy
                else -> R.drawable.small_tomato_sunny
            }
        } else {
            when (weatherType) {
                "sunny" -> R.drawable.small_lemon_sunny
                "rainy" -> R.drawable.small_lemon_rainy
                "cloudy" -> R.drawable.small_lemon_cloudy
                "snowy" -> R.drawable.small_lemon_snowy
                else -> R.drawable.small_lemon_sunny
            }
        }
        binding.smallFruit.setImageResource(smallFruitRes)
        
        // 更新瓶子
        val bottleRes = when (weatherType) {
            "sunny" -> R.drawable.bottle_sunny
            "rainy" -> R.drawable.bottle_rainy
            "cloudy" -> R.drawable.bottle_cloudy
            "snowy" -> R.drawable.bottle_snowy
            else -> R.drawable.bottle_sunny
        }
        binding.bottle.setImageResource(bottleRes)
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

    private fun setupWeatherButtons() {
        binding.btnSunny.setOnClickListener {
            isUserSelectedWeather = true
            userSelectedWeatherType = "sunny"
            updateWeatherUI("sunny")
            // 不立即刷新真实天气数据，保持用户选择
        }
        
        binding.btnRainy.setOnClickListener {
            isUserSelectedWeather = true
            userSelectedWeatherType = "rainy"
            updateWeatherUI("rainy")
            // 不立即刷新真实天气数据，保持用户选择
        }
        
        binding.btnCloudy.setOnClickListener {
            isUserSelectedWeather = true
            userSelectedWeatherType = "cloudy"
            updateWeatherUI("cloudy")
            // 不立即刷新真实天气数据，保持用户选择
        }
        
        binding.btnSnowy.setOnClickListener {
            isUserSelectedWeather = true
            userSelectedWeatherType = "snowy"
            updateWeatherUI("snowy")
            // 不立即刷新真实天气数据，保持用户选择
        }
    }

    private fun setupSeasonButtons() {
        binding.btnSpring.setOnClickListener {
            updateSeasonUI("spring")
        }
        
        binding.btnSummer.setOnClickListener {
            updateSeasonUI("summer")
        }
        
        binding.btnAutumn.setOnClickListener {
            updateSeasonUI("autumn")
        }
        
        binding.btnWinter.setOnClickListener {
            updateSeasonUI("winter")
        }
    }

    private fun updateSeasonUI(season: String) {
        // 更新瓶底背景
        val bottleBottomRes = when (season) {
            "spring" -> R.drawable.bottle_bottom_spring
            "summer" -> R.drawable.bottle_bottom_summer
            "autumn" -> R.drawable.bottle_bottom_autumn
            "winter" -> R.drawable.bottle_bottom_winter
            else -> R.drawable.bottle_bottom_spring
        }
        binding.bottleBottom.setImageResource(bottleBottomRes)
    }

    private fun setupExpressionButtons() {
        binding.btnHappy.setOnClickListener {
            updateExpression("happy")
        }
        
        binding.btnSad.setOnClickListener {
            updateExpression("sad")
        }
        
        binding.btnCalm.setOnClickListener {
            updateExpression("calm")
        }
        
        binding.btnAngry.setOnClickListener {
            updateExpression("angry")
        }
        
        binding.btnConfused.setOnClickListener {
            updateExpression("confused")
        }
    }

    private fun updateExpression(expression: String) {
        val expressionRes = if (currentIdentity == "lemon") {
            when (expression) {
                "happy" -> R.drawable.lemon_expression_happy
                "sad" -> R.drawable.lemon_expression_sad
                "calm" -> R.drawable.lemon_expression_calm
                "angry" -> R.drawable.lemon_expression_angry
                "confused" -> R.drawable.lemon_expression_confused
                else -> R.drawable.lemon_expression_happy
            }
        } else {
            when (expression) {
                "happy" -> R.drawable.tomato_expression_happy
                "sad" -> R.drawable.tomato_expression_sad
                "calm" -> R.drawable.tomato_expression_calm
                "angry" -> R.drawable.tomato_expression_angry
                "confused" -> R.drawable.tomato_expression_confused
                else -> R.drawable.tomato_expression_happy
            }
        }
        
        binding.bigFruitExpression.setImageResource(expressionRes)
        
        // 重新开始表情动画
        startExpressionAnimation()
    }

    private fun startExpressionAnimation() {
        // 表情缓慢移动动画，模拟注视动作
        val animator = ObjectAnimator.ofFloat(binding.bigFruitExpression, "translationX", 0f, 10f, -10f, 0f)
        animator.duration = 4000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.interpolator = LinearInterpolator()
        animator.start()
        
        val animatorY = ObjectAnimator.ofFloat(binding.bigFruitExpression, "translationY", 0f, 5f, -5f, 0f)
        animatorY.duration = 3000
        animatorY.repeatCount = ValueAnimator.INFINITE
        animatorY.repeatMode = ValueAnimator.REVERSE
        animatorY.interpolator = LinearInterpolator()
        animatorY.start()
    }

    private fun refreshWeatherData() {
        // 重置用户选择标志，允许API数据更新UI
        isUserSelectedWeather = false
        
        // 根据当前身份刷新天气数据
        val city = if (currentIdentity == "lemon") "wuhan" else "hefei"
        viewModel.fetchWeather(city)
    }

    private fun resetToRealWeather() {
        // 重置用户选择，恢复真实天气显示
        isUserSelectedWeather = false
        refreshWeatherData()
    }
}
