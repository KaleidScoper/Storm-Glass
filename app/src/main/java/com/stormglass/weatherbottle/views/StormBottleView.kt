package com.stormglass.weatherbottle.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.stormglass.weatherbottle.R
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
    
    // 图片资源
    private var lemonBodyDrawable: Drawable? = null
    private var tomatoBodyDrawable: Drawable? = null
    private var fruitLegDrawable: Drawable? = null
    private var fruitExpressionDrawable: Drawable? = null
    private var weatherBottleSunnyDrawable: Drawable? = null
    private var weatherBottleCloudyDrawable: Drawable? = null
    private var weatherBottleRainyDrawable: Drawable? = null
    private var lemonInBottleDrawable: Drawable? = null
    private var tomatoInBottleDrawable: Drawable? = null
    
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
        loadDrawables()
        startWeatherAnimation()
    }
    
    private fun loadDrawables() {
        try {
            // 加载图片资源
            lemonBodyDrawable = ContextCompat.getDrawable(context, R.drawable.lemon_body)
            tomatoBodyDrawable = ContextCompat.getDrawable(context, R.drawable.tomato_body)
            fruitLegDrawable = ContextCompat.getDrawable(context, R.drawable.fruit_leg)
            fruitExpressionDrawable = ContextCompat.getDrawable(context, R.drawable.fruit_expression)
            weatherBottleSunnyDrawable = ContextCompat.getDrawable(context, R.drawable.weather_bottle_sunny)
            weatherBottleCloudyDrawable = ContextCompat.getDrawable(context, R.drawable.weather_bottle_cloudy)
            weatherBottleRainyDrawable = ContextCompat.getDrawable(context, R.drawable.weather_bottle_rainy)
            lemonInBottleDrawable = ContextCompat.getDrawable(context, R.drawable.lemon_in_bottle)
            tomatoInBottleDrawable = ContextCompat.getDrawable(context, R.drawable.tomato_in_bottle)
        } catch (e: Exception) {
            // 如果图片资源不存在，使用程序绘制作为后备方案
        }
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
        
        // 按照分层顺序绘制
        // 第1层：瓶外水果的右侧腿
        drawRightLeg(canvas, centerX, centerY, bottleSize)
        
        // 第2层：瓶外水果身体
        drawCharacter(canvas, centerX, centerY, bottleSize)
        
        // 第3层：天气瓶下层（包含瓶底、草地/雪地、瓶子外形）
        drawWeatherBottleLower(canvas, centerX, centerY, bottleSize)
        
        // 第4层：瓶内水果
        drawInnerFruit(canvas, centerX, centerY, bottleSize)
        
        // 第5层：天气瓶上层（高光和天气效果）
        drawWeatherBottleUpper(canvas, centerX, centerY, bottleSize)
        
        // 第6层：瓶外水果的左侧腿
        drawLeftLeg(canvas, centerX, centerY, bottleSize)
        
        // 表情动画（围绕脸部移动）
        drawExpression(canvas, centerX, centerY, bottleSize)
    }
    
    private fun drawRightLeg(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        val characterSize = bottleSize * 0.8f
        val characterX = centerX + bottleSize * 0.6f
        val characterY = centerY
        
        if (fruitLegDrawable != null) {
            // 绘制右侧腿（被瓶子遮盖）
            val legRect = RectF(
                characterX + characterSize * 0.3f,
                characterY + characterSize * 0.4f,
                characterX + characterSize * 0.7f,
                characterY + characterSize * 0.8f
            )
            fruitLegDrawable?.bounds = legRect.toRect()
            fruitLegDrawable?.draw(canvas)
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
                if (lemonBodyDrawable != null) {
                    lemonBodyDrawable?.bounds = characterRect.toRect()
                    lemonBodyDrawable?.draw(canvas)
                } else {
                    drawLemonCharacter(canvas, characterX, characterY, characterSize)
                }
            }
            UserIdentity.TOMATO -> {
                if (tomatoBodyDrawable != null) {
                    tomatoBodyDrawable?.bounds = characterRect.toRect()
                    tomatoBodyDrawable?.draw(canvas)
                } else {
                    drawTomatoCharacter(canvas, characterX, characterY, characterSize)
                }
            }
        }
    }
    
    private fun drawWeatherBottleLower(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        bottleRect.set(
            centerX - bottleSize / 2,
            centerY - bottleSize / 2,
            centerX + bottleSize / 2,
            centerY + bottleSize / 2
        )
        
        // 根据天气类型选择对应的瓶子下层图片
        val bottleDrawable = when (weatherType) {
            WeatherType.SUNNY -> weatherBottleSunnyDrawable
            WeatherType.CLOUDY -> weatherBottleCloudyDrawable
            WeatherType.RAINY -> weatherBottleRainyDrawable
            WeatherType.SNOWY -> weatherBottleSunnyDrawable // 暂时用晴天版本
        }
        
        if (bottleDrawable != null) {
            bottleDrawable.bounds = bottleRect.toRect()
            bottleDrawable.draw(canvas)
        } else {
            // 后备方案：程序绘制瓶子
            drawStormBottle(canvas, centerX, centerY, bottleSize)
        }
    }
    
    private fun drawInnerFruit(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        val innerSize = bottleSize * 0.4f
        val innerX = centerX
        val innerY = centerY + bottleSize * 0.1f // 稍微向下，站在瓶底
        
        val innerFruitDrawable = when (userIdentity) {
            UserIdentity.LEMON -> tomatoInBottleDrawable
            UserIdentity.TOMATO -> lemonInBottleDrawable
        }
        
        if (innerFruitDrawable != null) {
            val fruitRect = RectF(
                innerX - innerSize / 2,
                innerY - innerSize / 2,
                innerX + innerSize / 2,
                innerY + innerSize / 2
            )
            innerFruitDrawable.bounds = fruitRect.toRect()
            innerFruitDrawable.draw(canvas)
        } else {
            // 后备方案：程序绘制
            when (userIdentity) {
                UserIdentity.LEMON -> drawTomato(canvas, innerX, innerY, innerSize)
                UserIdentity.TOMATO -> drawLemon(canvas, innerX, innerY, innerSize)
            }
        }
    }
    
    private fun drawWeatherBottleUpper(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        // 天气瓶上层：只包含高光和天气效果，不包含瓶底
        val upperRect = RectF(
            centerX - bottleSize / 2,
            centerY - bottleSize / 2,
            centerX + bottleSize / 2,
            centerY + bottleSize / 2
        )
        
        // 这里应该加载专门的天气效果上层图片
        // 暂时使用程序绘制的天气效果
        drawWeatherEffects(canvas, centerX, centerY, bottleSize)
    }
    
    private fun drawLeftLeg(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        val characterSize = bottleSize * 0.8f
        val characterX = centerX + bottleSize * 0.6f
        val characterY = centerY
        
        if (fruitLegDrawable != null) {
            // 绘制左侧腿（在瓶子上层）
            val legRect = RectF(
                characterX - characterSize * 0.7f,
                characterY + characterSize * 0.4f,
                characterX - characterSize * 0.3f,
                characterY + characterSize * 0.8f
            )
            fruitLegDrawable?.bounds = legRect.toRect()
            fruitLegDrawable?.draw(canvas)
        }
    }
    
    private fun drawExpression(canvas: Canvas, centerX: Float, centerY: Float, bottleSize: Float) {
        val characterSize = bottleSize * 0.8f
        val characterX = centerX + bottleSize * 0.6f
        val characterY = centerY
        
        if (fruitExpressionDrawable != null) {
            // 表情围绕脸部缓慢移动，模拟注视效果
            val expressionSize = characterSize * 0.3f
            val moveRadius = characterSize * 0.1f
            val moveX = characterX + cos(animationProgress * 2 * Math.PI).toFloat() * moveRadius
            val moveY = characterY - characterSize * 0.2f + sin(animationProgress * 2 * Math.PI).toFloat() * moveRadius
            
            val expressionRect = RectF(
                moveX - expressionSize / 2,
                moveY - expressionSize / 2,
                moveX + expressionSize / 2,
                moveY + expressionSize / 2
            )
            fruitExpressionDrawable?.bounds = expressionRect.toRect()
            fruitExpressionDrawable?.draw(canvas)
        }
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
        bottlePaint.color = context.getColor(R.color.bottle_border)
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
