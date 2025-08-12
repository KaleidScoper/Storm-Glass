# OpenWeatherMap API密钥配置指南

## 🚨 重要提示
当前应用使用的API密钥可能已过期或无效。请按照以下步骤获取新的API密钥。

## 📋 获取API密钥步骤

### 1. 注册OpenWeatherMap账号
1. 访问 [OpenWeatherMap官网](https://openweathermap.org/)
2. 点击右上角 "Sign In" → "Sign Up"
3. 填写邮箱、密码，完成注册
4. 登录后进入控制台

### 2. 获取免费API密钥
1. 登录后，点击 "API keys" 标签
2. 系统会自动生成一个默认的API密钥
3. 复制这个API密钥（通常以 "owm_" 开头）

### 3. 配置API密钥
1. 打开文件：`app/src/main/java/com/stormglass/weather/viewmodel/WeatherViewModel.kt`
2. 找到第47行：`private val API_KEY = "0b88a198e71f4de0383e21afe6312d1e"`
3. 将整个字符串替换为您的实际API密钥

```kotlin
// 将这行：
private val API_KEY = "0b88a198e71f4de0383e21afe6312d1e"

// 替换为：
private val API_KEY = "您的实际API密钥"
```

## 🔧 验证配置

### 1. 重新编译运行
1. 在Android Studio中重新编译项目
2. 运行应用到设备或模拟器
3. 选择身份（柠檬或西红柿）
4. 查看天气信息是否显示真实数据

### 2. 查看日志
在Android Studio的Logcat中搜索 "WeatherViewModel"，应该看到：
```
WeatherViewModel: 尝试获取天气数据，城市: Wuhan
WeatherViewModel: 成功获取天气数据，城市: Wuhan
WeatherViewModel: 天气数据获取成功: WeatherData(...)
```

### 3. 手动测试API
在浏览器中测试以下URL（替换YOUR_API_KEY为您的实际密钥）：
```
https://api.openweathermap.org/data/2.5/weather?q=Wuhan&appid=YOUR_API_KEY&units=metric&lang=zh_cn
```

预期响应：
```json
{
  "coord": {"lon": 114.2667, "lat": 30.5833},
  "weather": [{"id": 800, "main": "Clear", "description": "晴", "icon": "01d"}],
  "main": {"temp": 25.6, "feels_like": 26.2, "humidity": 65},
  "wind": {"speed": 3.2},
  "name": "Wuhan",
  "cod": 200
}
```

## 🚨 常见问题

### 问题1：API密钥无效
**症状**：显示 "API错误: 401" 或 "模拟天气数据"
**解决**：
- 确认API密钥已正确复制
- 等待1-2小时让新密钥激活
- 检查密钥是否包含多余的空格

### 问题2：网络请求失败
**症状**：显示 "网络请求失败"
**解决**：
- 检查网络连接
- 确认应用有网络权限
- 尝试使用其他网络

### 问题3：城市名称问题
**症状**：显示 "API错误: 404"
**解决**：
- 应用会自动尝试多种城市名称格式
- 如果仍有问题，可以手动修改城市名称

## 📱 应用功能说明

### 真实天气数据
- **柠檬身份**：显示武汉天气
- **西红柿身份**：显示合肥天气
- **数据包括**：温度、湿度、风速、天气描述、报告时间

### 智能天气映射
- **晴天**：clear, sunny, 晴
- **雨天**：rain, drizzle, shower, 雨, 雷
- **雪天**：snow, sleet, 雪
- **多云**：cloud, overcast, mist, fog, 云, 阴, 雾, 霾

### 季节判断
- **春季**：3-5月或温度15-25°C
- **夏季**：6-8月或温度≥25°C
- **秋季**：9-11月或温度5-15°C
- **冬季**：12-2月或温度<5°C

## 🔒 安全提醒
- 不要将API密钥提交到公开代码仓库
- 免费版有1000次/天的调用限制
- 建议在正式发布前测试API密钥有效性

## 📞 技术支持
如果遇到问题：
1. 查看Logcat日志获取详细错误信息
2. 访问 [OpenWeatherMap支持页面](https://openweathermap.org/support)
3. 检查API密钥状态和调用次数
