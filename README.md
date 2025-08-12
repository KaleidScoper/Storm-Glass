# Storm Glass - 风暴瓶

一个Android天气应用，通过水果角色和风暴瓶展示天气信息。

## 功能说明

### 身份选择
- 柠檬身份：显示西红柿，获取武汉天气
- 西红柿身份：显示柠檬，获取合肥天气

### 天气显示
- 实时天气数据（温度、湿度、风速、天气描述）
- 支持晴天、雨天、阴天、雪天四种状态
- 春夏秋冬季节背景自动切换

### 界面特点
- 可折叠功能栏（左上角汉堡菜单）
- 毛玻璃效果设计
- 表情切换功能

## 使用方法

1. 选择身份（柠檬或西红柿）
2. 应用自动获取对应城市天气
3. 使用测试按钮体验不同天气和季节
4. 点击表情按钮切换表情

## 技术实现

- MVVM架构
- Retrofit网络请求
- ConstraintLayout布局
- LiveData数据绑定

## 开发环境

- Android Studio
- Kotlin
- 最低支持：Android 6.0 (API 24)

## 项目结构

```
app/src/main/
├── java/com/stormglass/weather/
│   ├── MainActivity.kt          # 主界面逻辑
│   ├── api/WeatherApiService.kt # 天气API接口
│   ├── model/WeatherData.kt     # 天气数据模型
│   └── viewmodel/WeatherViewModel.kt # 数据管理
└── res/
    ├── layout/activity_main.xml # 主界面布局
    ├── drawable/                 # 图片资源
    └── values/                   # 颜色和字符串
```

## 注意事项

- 需要配置天气API密钥
- 目前使用和风天气API服务
- 网络请求失败时会显示模拟数据
