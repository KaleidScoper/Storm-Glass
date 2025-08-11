# 风暴瓶天气APP 🍋🍅

一个浪漫的天气应用，为情侣设计的专属风暴瓶界面。

## 功能特点

- 🎭 **身份选择**: 选择你的身份（柠檬🍋 或 西红柿🍅）
- 🌪️ **风暴瓶界面**: 精美的程序绘制风暴瓶，显示天气变化
- 🌤️ **实时天气**: 获取武汉洪山区和合肥蜀山区的实时天气信息
- 🎨 **卡通角色**: 可爱的柠檬和西红柿角色，观察瓶子内的天气变化
- 🌙 **暗色主题**: 专为夜间使用设计的黑白蓝配色方案
- 📊 **智能信息栏**: 顶部可展开/收起的天气信息栏
- 🎮 **天气测试**: 底部天气切换按钮，可手动测试不同天气效果
- 🖼️ **图片素材**: 支持自定义图片素材，表情、身体、腿部分离设计
- ⏰ **时间显示**: 实时显示当前时间
- 📱 **响应式设计**: 适配各种Android设备屏幕

## 技术架构

- **语言**: Kotlin
- **架构**: MVVM + Repository Pattern
- **网络**: Retrofit2 + OkHttp3
- **UI**: 自定义View + ConstraintLayout
- **动画**: ValueAnimator + 程序绘制
- **天气API**: 和风天气API (qweather.com) - 中国本土服务，更稳定

## 安装说明

### 方法1: 通过Android Studio构建

1. 克隆项目到本地
2. 在Android Studio中打开项目
3. 在`WeatherRepository.kt`中替换`YOUR_WEATHER_API_KEY`为实际的API密钥
4. 连接Android设备或启动模拟器
5. 点击运行按钮构建并安装

### 方法2: 直接安装APK

1. 下载项目中的`app-release.apk`文件
2. 在Android设备上启用"未知来源"应用安装
3. 点击APK文件进行安装

### 方法3: 通过ADB命令行安装

1. 构建项目生成APK
2. 使用ADB连接设备
3. 执行命令：`adb install app-release.apk`

## 使用说明

1. 启动应用后，选择你的身份（柠檬🍋 或 西红柿🍅）
2. 应用将显示风暴瓶界面
3. 瓶子内显示对方城市的天气（柠檬显示武汉，西红柿显示合肥）
4. 瓶子外坐着你选择的角色，观察天气变化
5. 底部显示详细的天气信息

## 天气API配置

本应用使用和风天气API的免费服务，这是中国本土的天气服务，在国产手机上更稳定。你需要：

1. 访问 [和风天气开发平台](https://dev.qweather.com/)
2. 注册免费账户
3. 获取API密钥
4. 在`WeatherRepository.kt`中替换`YOUR_QWEATHER_API_KEY`为实际的API密钥

**注意**: 和风天气API在中国大陆访问更稳定，特别适合OPPO、vivo等国产手机。

## 项目结构

```
app/src/main/
├── java/com/stormglass/weatherbottle/
│   ├── MainActivity.kt              # 主活动
│   ├── api/                         # API接口
│   ├── data/                        # 数据模型
│   ├── repository/                  # 数据仓库
│   └── views/                       # 自定义视图
├── res/                             # 资源文件
│   ├── drawable/                    # 图形资源
│   ├── layout/                      # 布局文件
│   ├── values/                      # 值资源
│   └── xml/                         # XML配置
└── AndroidManifest.xml              # 应用清单
```

## 开发环境要求

- Android Studio Arctic Fox或更高版本
- Android SDK 24+
- Kotlin 1.9.10+
- Gradle 8.2+

## 许可证

本项目仅供学习和个人使用。

## 联系方式

如有问题或建议，请通过GitHub Issues联系。

---

**注意**: 这是一个浪漫的礼物应用，请确保在安装前了解其功能和使用方法。
