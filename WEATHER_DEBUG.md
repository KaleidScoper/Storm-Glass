# 天气获取问题调试指南

## 🔍 问题现象
天气信息处仍然显示"模拟天气数据"，无法获取真实天气信息。

## 📋 排查步骤

### 1. 检查Logcat日志
在Android Studio中：
1. 打开 **Logcat** 窗口
2. 在搜索框中输入：`WeatherViewModel`
3. 运行应用并选择身份
4. 查看详细的日志输出

### 2. 预期日志内容
**成功情况**：
```
WeatherViewModel: 开始获取天气数据，城市: wuhan, API密钥: 27ca5c2...
WeatherViewModel: 正在获取城市 wuhan 的天气数据，location ID: 101200101...
WeatherViewModel: API请求URL: https://devapi.qweather.com/v7/weather/now?location=101200101&key=27ca5c2...
WeatherViewModel: API响应: code=200, 天气描述=晴
WeatherViewModel: 天气类型映射: 晴 -> sunny
WeatherViewModel: 成功获取天气数据: WeatherData(...)
```

**失败情况**：
```
WeatherViewModel: 开始获取天气数据，城市: wuhan, API密钥: 27ca5c2...
WeatherViewModel: 正在获取城市 wuhan 的天气数据，location ID: 101200101...
WeatherViewModel: 获取天气数据失败
WeatherViewModel: 异常类型: [异常类型]
WeatherViewModel: 异常消息: [错误信息]
```

### 3. 常见问题及解决方案

#### 问题1：网络权限问题
**症状**：`SecurityException` 或网络相关异常
**解决**：确认 `AndroidManifest.xml` 中已添加网络权限

#### 问题2：API密钥问题
**症状**：`401 Unauthorized` 或 `403 Forbidden`
**解决**：检查API密钥是否正确，是否在有效期内

#### 问题3：网络连接问题
**症状**：`SocketTimeoutException` 或 `ConnectException`
**解决**：检查网络连接，尝试使用其他网络

#### 问题4：API服务问题
**症状**：`500 Internal Server Error` 或其他服务器错误
**解决**：等待一段时间后重试，或联系和风天气技术支持

### 4. 手动测试API
在浏览器中测试以下URL：
```
武汉天气：
https://devapi.qweather.com/v7/weather/now?location=101200101&key=27ca5c2719a243148a7318f89455baa2

合肥天气：
https://devapi.qweather.com/v7/weather/now?location=101220101&key=27ca5c2719a243148a7318f89455baa2
```

**预期响应**：
```json
{
  "code": "200",
  "now": {
    "obsTime": "2024-01-01T12:00:00+08:00",
    "temp": "25",
    "feelsLike": "26",
    "icon": "100",
    "text": "晴",
    "wind360": "0",
    "windDir": "北风",
    "windScale": "1",
    "windSpeed": "3",
    "humidity": "65",
    "precip": "0.0",
    "pressure": "1013",
    "vis": "25",
    "cloud": "0",
    "dew": "18"
  }
}
```

### 5. 临时解决方案
如果API持续失败，可以临时启用模拟数据：
```kotlin
// 在WeatherViewModel.kt中临时注释掉API调用
// val response = weatherApiService.getCurrentWeather(location, API_KEY)
// 直接使用模拟数据
useSimulatedWeatherData(city)
```

### 6. 高级调试
如果问题仍然存在，可以：
1. 检查网络代理设置
2. 尝试使用不同的网络环境
3. 检查防火墙设置
4. 验证API密钥的IP白名单设置

## 🚨 紧急联系
如果问题持续存在，请：
1. 收集完整的Logcat日志
2. 记录具体的错误信息
3. 联系和风天气技术支持
4. 提供API密钥和错误详情

## 📱 应用内测试
1. 启动应用
2. 选择身份（柠檬或西红柿）
3. 查看Logcat中的详细日志
4. 根据日志信息判断具体问题
