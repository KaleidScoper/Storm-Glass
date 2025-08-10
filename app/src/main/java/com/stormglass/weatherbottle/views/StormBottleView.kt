package com.stormglass.weatherbottle.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min

class StormBottleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bottlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val characterPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val weatherPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private var userIdentity: UserIdentity = UserIdentity.LEMON
    private var weatherType: WeatherType = WeatherType.SUNNY
    private var animationProgress = 0f
    
    private var bottleRect = RectF()
    private var characterRect = RectF()
    
    private val weatherAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 3000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
        interpolator = LinearInterpolator()
        addUpdateListener { animator ->
            animationProgress = animator.animatedValue as Float
            invalidate()
        }
    }
    
    enum class UserIdentity {
        LEMON, TOMATO
    }
    
    enum class WeatherType {
        SUNNY, CLOUDY, RAINY, SNOWY
    }
    
    init {
        startWeatherAnimation()
    }
    
    fun setUserIdentity(identity: UserIdentity) {
        userIdentity = identity
        invalidate()
    }
    
    fun setWeatherType(weather: WeatherType) {
        weatherType = weather
        invalidate()
    }
    
    private fun startWeatherAnimation() {
        weatherAnimator.start()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val bottleSize = min(width, height) * 0.6f
        
        // 绘制风暴瓶
        drawStormBottle(canvas, centerX, centerY, bottleSize)
        
        // 绘制卡通角色
        drawCharacter(canvas, centerX, centerY, bottleSize)
        
        // 绘制天气效果
        drawWeatherEffects(canvas, centerX, centerY, bottleSize)
    }
    
    private fun drawStormBottle(canvas: Canvas, centerX: Float, centerY: Float, size: Float) {
        // 瓶子主体
        bottleRect.set(
            centerX - size / 2,
            centerY - size / 2,
            centerX + size / 2,
            centerY + size / 2
        )
        
        // 瓶子外框
        bottlePaint.style = Paint.Style.STROKE
        bottlePaint.strokeWidth = 8f
        bottlePaint.color = Color.parseColor("#1976D2")
        canvas.drawRoundRect(bottleRect, 20f, 20f, bottlePaint)
        
        // 瓶子玻璃效果
        bottlePaint.style = Paint.Style.FILL
        bottlePaint.color = Color.parseColor("#E3F2FD")
        bottlePaint.alpha = 100
        canvas.drawRoundRect(bottleRect, 20f, 20f, bottlePaint)
        
        // 瓶子内部内容（根据用户身份显示相反的角色）
        val innerSize = size * 0.4f
        val innerX = centerX
        val innerY = centerY
        
        when (userIdentity) {
            UserIdentity.LEMON -> {
                // 显示西红柿
                drawTomato(canvas, innerX, innerY, innerSize)
            }
            UserIdentity.TOMATO -> {
                // 显示柠檬
                drawLemon(canvas, innerX, innerY, innerSize)
            }
        }
    }
    
    private fun drawCharacter(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        val characterSize = bottleSize * 0.8f
        val characterX = centerX + bottleSize * 0.6f
        val characterY = centerY
        
        characterRect.set(
            characterX - characterSize / 2,
            characterY - characterSize / 2,
            characterX + characterSize / 2,
            characterY + characterSize / 2
        )
        
        when (userIdentity) {
            UserIdentity.LEMON -> {
                drawLemonCharacter(canvas, characterX, characterY, characterSize)
            }
            UserIdentity.TOMATO -> {
                drawTomatoCharacter(canvas, characterX, characterY, characterSize)
            }
        }
    }
    
    private fun drawLemon(canvas: Canvas, x: Float, y: Float, size: Float) {
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#FFEB3B")
        
        // 柠檬主体
        canvas.drawCircle(x, y, size / 2, paint)
        
        // 柠檬纹理
        paint.color = Color.parseColor("#FDD835")
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
        
        for (i in 0..2) {
            val angle = i * 120f * Math.PI / 180f
            val startX = x + cos(angle).toFloat() * size * 0.3f
            val startY = y + sin(angle).toFloat() * size * 0.3f
            val endX = x + cos(angle).toFloat() * size * 0.4f
            val endY = y + sin(angle).toFloat() * size * 0.4f
            canvas.drawLine(startX, startY, endX, endY, paint)
        }
    }
    
    private fun drawTomato(canvas: Canvas, x: Float, y: Float, size: Float) {
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F44336")
        
        // 西红柿主体
        canvas.drawCircle(x, y, size / 2, paint)
        
        // 西红柿叶子
        paint.color = Color.parseColor("#4CAF50")
        val leafPath = Path()
        leafPath.moveTo(x, y - size / 2)
        leafPath.quadTo(x + size * 0.2f, y - size * 0.6f, x + size * 0.3f, y - size * 0.4f)
        leafPath.quadTo(x + size * 0.2f, y - size * 0.2f, x, y - size * 0.1f)
        leafPath.close()
        canvas.drawPath(leafPath, paint)
    }
    
    private fun drawLemonCharacter(canvas: Canvas, x: Float, y: Float, size: Float) {
        // 柠檬身体
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#FFEB3B")
        canvas.drawCircle(x, y, size / 2, paint)
        
        // 眼睛
        paint.color = Color.BLACK
        val eyeSize = size * 0.15f
        canvas.drawCircle(x - size * 0.2f, y - size * 0.1f, eyeSize, paint)
        canvas.drawCircle(x + size * 0.2f, y - size * 0.1f, eyeSize, paint)
        
        // 嘴巴（微笑）
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = Color.BLACK
        val mouthRect = RectF(x - size * 0.2f, y + size * 0.1f, x + size * 0.2f, y + size * 0.3f)
        canvas.drawArc(mouthRect, 0f, 180f, false, paint)
        
        // 手臂（指向瓶子）
        paint.strokeWidth = 6f
        val armStartX = x - size * 0.4f
        val armStartY = y
        val armEndX = x - size * 0.6f
        val armEndY = y - size * 0.2f
        canvas.drawLine(armStartX, armStartY, armEndX, armEndY, paint)
    }
    
    private fun drawTomatoCharacter(canvas: Canvas, x: Float, y: Float, size: Float) {
        // 西红柿身体
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#F44336")
        canvas.drawCircle(x, y, size / 2, paint)
        
        // 眼睛
        paint.color = Color.WHITE
        val eyeSize = size * 0.15f
        canvas.drawCircle(x - size * 0.2f, y - size * 0.1f, eyeSize, paint)
        canvas.drawCircle(x + size * 0.2f, y - size * 0.1f, eyeSize, paint)
        
        // 眼珠
        paint.color = Color.BLACK
        val pupilSize = eyeSize * 0.6f
        canvas.drawCircle(x - size * 0.2f, y - size * 0.1f, pupilSize, paint)
        canvas.drawCircle(x + size * 0.2f, y - size * 0.1f, pupilSize, paint)
        
        // 嘴巴（微笑）
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = Color.WHITE
        val mouthRect = RectF(x - size * 0.2f, y + size * 0.1f, x + size * 0.2f, y + size * 0.3f)
        canvas.drawArc(mouthRect, 0f, 180f, false, paint)
        
        // 手臂（指向瓶子）
        paint.strokeWidth = 6f
        paint.color = Color.WHITE
        val armStartX = x - size * 0.4f
        val armStartY = y
        val armEndX = x - size * 0.6f
        val armEndY = y - size * 0.2f
        canvas.drawLine(armStartX, armStartY, armEndX, armEndY, paint)
    }
    
    private fun drawWeatherEffects(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        when (weatherType) {
            WeatherType.SUNNY -> drawSunnyWeather(canvas, centerX, centerY, bottleSize)
            WeatherType.CLOUDY -> drawCloudyWeather(canvas, centerX, centerY, bottleSize)
            WeatherType.RAINY -> drawRainyWeather(canvas, centerX, centerY, bottleSize)
            WeatherType.SNOWY -> drawSnowyWeather(canvas, centerX, centerY, bottleSize)
        }
    }
    
    private fun drawSunnyWeather(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        // 太阳光芒
        weatherPaint.style = Paint.Style.STROKE
        weatherPaint.strokeWidth = 4f
        weatherPaint.color = Color.parseColor("#FF9800")
        
        val sunRadius = bottleSize * 0.3f
        val sunX = centerX - bottleSize * 0.8f
        val sunY = centerY - bottleSize * 0.8f
        
        // 太阳主体
        weatherPaint.style = Paint.Style.FILL
        canvas.drawCircle(sunX, sunY, sunRadius, weatherPaint)
        
        // 太阳光芒
        weatherPaint.style = Paint.Style.STROKE
        for (i in 0..7) {
            val angle = i * 45f * Math.PI / 180f
            val startX = sunX + cos(angle).toFloat() * sunRadius
            val startY = sunY + sin(angle).toFloat() * sunRadius
            val endX = sunX + cos(angle).toFloat() * (sunRadius + 20f)
            val endY = sunY + sin(angle).toFloat() * (sunRadius + 20f)
            canvas.drawLine(startX, startY, endX, endY, weatherPaint)
        }
    }
    
    private fun drawCloudyWeather(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        weatherPaint.style = Paint.Style.FILL
        weatherPaint.color = Color.parseColor("#9E9E9E")
        
        val cloudX = centerX - bottleSize * 0.8f
        val cloudY = centerY - bottleSize * 0.8f
        
        // 绘制云朵
        val cloudRadius = bottleSize * 0.2f
        canvas.drawCircle(cloudX, cloudY, cloudRadius, weatherPaint)
        canvas.drawCircle(cloudX + cloudRadius * 0.8f, cloudY, cloudRadius * 0.8f, weatherPaint)
        canvas.drawCircle(cloudX - cloudRadius * 0.8f, cloudY, cloudRadius * 0.8f, weatherPaint)
        canvas.drawCircle(cloudX + cloudRadius * 0.4f, cloudY - cloudRadius * 0.3f, cloudRadius * 0.6f, weatherPaint)
        canvas.drawCircle(cloudX - cloudRadius * 0.4f, cloudY - cloudRadius * 0.3f, cloudRadius * 0.6f, weatherPaint)
    }
    
    private fun drawRainyWeather(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        weatherPaint.style = Paint.Style.STROKE
        weatherPaint.strokeWidth = 3f
        weatherPaint.color = Color.parseColor("#2196F3")
        
        val rainStartX = centerX - bottleSize * 0.8f
        val rainStartY = centerY - bottleSize * 0.8f
        
        // 绘制雨滴
        for (i in 0..5) {
            val x = rainStartX + i * 30f
            val startY = rainStartY + 20f
            val endY = startY + 40f
            canvas.drawLine(x, startY, x + 5f, endY, weatherPaint)
        }
    }
    
    private fun drawSnowyWeather(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        weatherPaint.style = Paint.Style.FILL
        weatherPaint.color = Color.parseColor("#E1F5FE")
        
        val snowStartX = centerX - bottleSize * 0.8f
        val snowStartY = centerY - bottleSize * 0.8f
        
        // 绘制雪花
        for (i in 0..5) {
            val x = snowStartX + i * 30f
            val y = snowStartY + 20f + (i * 10f * animationProgress)
            val snowSize = 8f
            
            // 简单的雪花图案
            canvas.drawCircle(x, y, snowSize, weatherPaint)
            canvas.drawCircle(x + snowSize * 0.7f, y, snowSize * 0.5f, weatherPaint)
            canvas.drawCircle(x - snowSize * 0.7f, y, snowSize * 0.5f, weatherPaint)
            canvas.drawCircle(x, y + snowSize * 0.7f, snowSize * 0.5f, weatherPaint)
            canvas.drawCircle(x, y - snowSize * 0.7f, snowSize * 0.5f, weatherPaint)
        }
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        weatherAnimator.cancel()
    }
}
