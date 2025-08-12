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
            viewModel.fetchWeather("wuhan") // 武汉洪山区天气
        }

        binding.tomatoButton.setOnClickListener {
            currentIdentity = "tomato"
            showStormBottle()
            viewModel.fetchWeather("hefei") // 合肥蜀山区天气
        }
    }

    private fun showStormBottle() {
        binding.identitySelectionLayout.visibility = View.GONE
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
        
        // 观察天气数据变化
        viewModel.weatherData.observe(this) { weather ->
            weather?.let {
                binding.weatherDetails.text = """
                    温度: ${it.temperature}°C
                    湿度: ${it.humidity}%
                    风速: ${it.windSpeed} m/s
                    天气: ${it.description}
                """.trimIndent()
                
                // 根据天气更新瓶子和小水果
                updateWeatherUI(it.weatherType)
            }
        }
    }

    private fun setupCollapsibleToolbar() {
        var isExpanded = false
        
        binding.collapsedToolbar.setOnClickListener {
            if (isExpanded) {
                // 收起功能栏
                binding.expandedToolbar.visibility = View.GONE
                binding.collapsedToolbar.visibility = View.VISIBLE
                isExpanded = false
            } else {
                // 展开功能栏
                binding.expandedToolbar.visibility = View.VISIBLE
                binding.collapsedToolbar.visibility = View.GONE
                isExpanded = true
            }
        }
    }

    private fun updateWeatherUI(weatherType: String) {
        val season = getCurrentSeason()
        
        // 更新瓶底（季节）
        val bottleBottomRes = when (season) {
            "spring" -> R.drawable.bottle_bottom_spring
            "summer" -> R.drawable.bottle_bottom_summer
            "autumn" -> R.drawable.bottle_bottom_autumn
            "winter" -> R.drawable.bottle_bottom_winter
            else -> R.drawable.bottle_bottom_spring
        }
        binding.bottleBottom.setImageResource(bottleBottomRes)
        
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
            updateWeatherUI("sunny")
        }
        
        binding.btnRainy.setOnClickListener {
            updateWeatherUI("rainy")
        }
        
        binding.btnCloudy.setOnClickListener {
            updateWeatherUI("cloudy")
        }
        
        binding.btnSnowy.setOnClickListener {
            updateWeatherUI("snowy")
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
        // 更新瓶底（季节）
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
}
