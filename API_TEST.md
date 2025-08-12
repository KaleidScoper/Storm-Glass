# API配置验证指南

## 🔍 问题排查步骤

### 1. 检查API密钥配置
确认 `WeatherViewModel.kt` 中的API密钥已正确设置：
```kotlin
private val API_KEY = "27ca5c2719a243148a7318f89455baa2"
```

### 2. 查看Logcat日志
在Android Studio中：
1. 打开 **Logcat** 窗口
2. 在搜索框中输入：`WeatherViewModel`
3. 运行应用并选择身份
4. 查看日志输出

### 3. 预期日志内容
**成功情况**：
```
WeatherViewModel: 正在获取城市 wuhan 的天气数据...
WeatherViewModel: 成功获取天气数据: WeatherData(...)
```

**失败情况**：
```
WeatherViewModel: API返回错误: [错误代码]
WeatherViewModel: 网络请求失败: [错误信息]
```

### 4. 常见问题及解决方案

#### 问题1：显示"模拟天气数据"
**可能原因**：
- API密钥无效或过期
- 网络请求失败
- API返回错误

**解决方案**：
1. 检查API密钥是否正确
2. 确认网络连接正常
3. 查看Logcat中的具体错误信息

#### 问题2：网络请求失败
**可能原因**：
- 网络权限问题
- 网络连接不稳定
- API服务不可用

**解决方案**：
1. 确认应用有网络权限
2. 检查网络连接
3. 尝试重新运行应用

### 5. 手动测试API
可以使用浏览器或Postman测试API：
```
https://devapi.qweather.com/v7/weather/now?location=101200101&key=27ca5c2719a243148a7318f89455baa2
```

### 6. 重置和重试
如果问题持续：
1. 清理应用数据
2. 重新编译运行
3. 检查API密钥是否在有效期内

## 📱 应用内测试步骤

1. **启动应用**
2. **选择身份**（柠檬或西红柿）
3. **查看天气详情**：
   - 应该显示真实天气数据
   - 不应该显示"模拟天气数据"
4. **点击测试按钮**：
   - 天气按钮应该刷新真实数据
   - 季节按钮应该只改变瓶底

## 🚨 紧急解决方案

如果API持续失败，可以临时启用模拟数据：
```kotlin
// 在WeatherViewModel.kt中临时注释掉API调用
// val response = weatherApiService.getCurrentWeather(location, API_KEY)
// 直接使用模拟数据
useSimulatedWeatherData(city)
```
