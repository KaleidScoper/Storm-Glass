# 和风天气API配置说明

## 🔑 获取API密钥

1. 访问 [和风天气官网](https://dev.qweather.com/)
2. 注册账号并登录
3. 创建应用，选择"免费开发版"
4. 获取API密钥（Key）

## ⚙️ 配置步骤

### 1. 替换API密钥
在 `app/src/main/java/com/stormglass/weather/viewmodel/WeatherViewModel.kt` 文件中：

```kotlin
// 将这一行：
private val API_KEY = "YOUR_API_KEY_HERE"

// 替换为您的实际API密钥：
private val API_KEY = "您的实际API密钥"
```

### 2. 验证配置
- 确保网络权限已添加
- 重新编译并运行应用
- 查看Logcat日志确认API调用成功

## 📱 功能说明

### 真实天气数据
- 武汉洪山区（柠檬身份）
- 合肥蜀山区（西红柿身份）
- 自动获取当前温度、湿度、风速、天气描述

### 模拟数据（API密钥未配置时）
- 基于时间和城市生成模拟数据
- 白天晴天、晚上多云、深夜雨天
- 不同城市显示不同温度

## 🚨 注意事项

1. **免费版限制**：和风天气免费版有API调用次数限制
2. **网络权限**：确保应用有网络访问权限
3. **API密钥安全**：不要将API密钥提交到公开代码仓库

## 🔧 故障排除

### 常见问题
- **"API密钥未设置"**：检查是否已替换API密钥
- **"网络请求失败"**：检查网络连接和权限
- **"API返回错误"**：检查API密钥是否有效

### 日志查看
在Android Studio的Logcat中搜索 "WeatherViewModel" 标签查看详细日志。

## 📞 技术支持

如果遇到问题，请：
1. 查看Logcat日志
2. 检查网络连接
3. 验证API密钥有效性
4. 联系和风天气技术支持
