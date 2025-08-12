# OpenWeatherMap API配置说明

## 1. 注册OpenWeatherMap账号

1. 访问 [OpenWeatherMap官网](https://openweathermap.org/)
2. 点击右上角"Sign In" → "Sign Up"
3. 填写邮箱、密码，完成注册
4. 登录后进入控制台

## 2. 获取API密钥

1. 登录后，点击"API keys"标签
2. 系统会自动生成一个默认的API密钥
3. 复制这个API密钥（通常以"owm_"开头）

## 3. 配置API密钥

1. 打开 `app/src/main/java/com/stormglass/weather/viewmodel/WeatherViewModel.kt`
2. 找到第47行：`private val API_KEY = "YOUR_OPENWEATHER_API_KEY_HERE"`
3. 将 `YOUR_OPENWEATHER_API_KEY_HERE` 替换为您的实际API密钥

```kotlin
private val API_KEY = "您的实际API密钥"
```

## 4. API特点

- **免费额度**：1000次/天
- **响应速度**：全球CDN，响应快
- **数据准确**：国际知名气象服务
- **支持城市**：全球所有城市
- **中文支持**：支持中文城市名
- **返回数据**：温度、湿度、风速、天气描述等

## 5. 城市名称

OpenWeatherMap支持多种城市名称格式：
- 中文：武汉、合肥
- 英文：Wuhan、Hefei
- 拼音：Wuhan、Hefei

## 6. 测试

配置完成后：
1. 重新编译运行应用
2. 选择身份后应该能看到真实天气数据
3. 天气信息会显示城市、温度、天气描述等
4. 季节会根据天气和温度智能判断

## 7. 故障排除

如果仍然显示"模拟数据"：
1. 检查API密钥是否正确配置
2. 确认网络连接正常
3. 查看Logcat日志中的错误信息
4. 确认API密钥已激活（可能需要等待几小时）

## 8. 日志查看

应用会输出详细的API请求日志，包括：
- 请求的城市名称
- API响应数据
- 错误信息等

通过Logcat可以查看完整的调试信息。

## 9. 注意事项

- 新注册的API密钥可能需要等待1-2小时才能激活
- 免费版本有1000次/天的调用限制
- 建议在正式使用前先测试API密钥是否有效

