# Release构建问题解决指南

## 🚨 问题描述
Debug版本正常运行，但Release版本无法正常显示天气数据。

## 🔍 可能的原因

### 1. 代码混淆问题
Release版本会进行代码混淆，可能影响网络请求和JSON解析。

### 2. 网络配置问题
Release版本可能有不同的网络安全策略。

### 3. 缓存问题
旧版本的数据可能被缓存，影响新版本运行。

### 4. API密钥问题
Release版本的签名可能影响API调用。

## 🛠️ 解决步骤

### 步骤1：清理旧版本
```bash
# 卸载旧版本
adb uninstall com.stormglass.weather

# 清理构建缓存
./gradlew clean
```

### 步骤2：检查ProGuard配置
在 `app/proguard-rules.pro` 中添加以下规则：
```proguard
# 保留网络相关类
-keep class com.stormglass.weather.api.** { *; }
-keep class com.stormglass.weather.model.** { *; }
-keep class com.stormglass.weather.viewmodel.** { *; }

# 保留Retrofit相关类
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# 保留OkHttp相关类
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# 保留Gson相关类
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
```

### 步骤3：检查网络安全配置
在 `app/src/main/res/xml/network_security_config.xml` 中添加：
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">api.openweathermap.org</domain>
    </domain-config>
</network-security-config>
```

然后在 `AndroidManifest.xml` 中引用：
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ...>
```

### 步骤4：重新构建Release版本
```bash
# 构建Release版本
./gradlew assembleRelease

# 安装新版本
adb install app/build/outputs/apk/release/app-release.apk
```

### 步骤5：测试和调试
1. 运行应用
2. 查看Logcat日志（过滤标签：WeatherViewModel）
3. 检查网络请求是否成功
4. 验证天气数据是否正确显示

## 🔧 调试技巧

### 1. 启用Release版本日志
在 `WeatherViewModel.kt` 中添加更多日志：
```kotlin
Log.d("WeatherViewModel", "Release版本测试 - 开始获取天气")
```

### 2. 检查网络连接
```kotlin
// 在fetchWeather方法开始处添加
Log.d("WeatherViewModel", "网络状态检查")
```

### 3. 验证API响应
```kotlin
// 在API响应处理中添加
Log.d("WeatherViewModel", "API响应: ${response.toString()}")
```

## 🚨 常见问题

### 问题1：网络请求失败
**解决**：检查网络安全配置和ProGuard规则

### 问题2：JSON解析失败
**解决**：确保Gson相关类没有被混淆

### 问题3：类找不到
**解决**：检查ProGuard规则是否完整

### 问题4：权限问题
**解决**：确保网络权限正确配置

## 📱 验证步骤

1. **清理旧版本**：完全卸载旧版本
2. **重新构建**：使用新的ProGuard配置
3. **安装测试**：安装Release版本
4. **功能验证**：测试天气获取功能
5. **日志检查**：查看Logcat确认问题

## 🎯 预期结果

修复后，Release版本应该能够：
- 正常启动应用
- 成功获取天气数据
- 正确显示天气信息
- 与Debug版本行为一致
