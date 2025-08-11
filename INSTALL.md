# 风暴瓶APP安装指南 📱

## 方法1: 通过Android Studio构建安装（推荐）

### 准备工作
1. 下载并安装 [Android Studio](https://developer.android.com/studio)
2. 确保你的电脑有至少8GB内存和10GB可用磁盘空间
3. 下载并安装Java Development Kit (JDK) 8或更高版本

### 详细步骤

#### 步骤1: 获取天气API密钥
1. 访问 [和风天气开发平台](https://dev.qweather.com/)
2. 点击"注册"并填写相关信息
3. 验证邮箱并完成注册
4. 登录后创建应用，获取API密钥
5. 复制API密钥（格式类似：`1234567890abcdef1234567890abcdef`）

#### 步骤2: 配置项目
1. 在Android Studio中打开项目
2. 找到文件：`app/src/main/java/com/stormglass/weatherbottle/repository/WeatherRepository.kt`
3. 将第18行的`YOUR_QWEATHER_API_KEY`替换为你刚才获取的API密钥
4. 保存文件

**优势**: 和风天气API在中国大陆访问更稳定，特别适合OPPO、vivo、华为、小米等国产手机。

#### 步骤3: 连接Android设备
1. 在Android设备上启用"开发者选项"
   - 进入"设置" → "关于手机"
   - 连续点击"版本号"7次
   - 返回设置，找到"开发者选项"
   - 启用"USB调试"
2. 用USB线连接手机和电脑
3. 在手机上允许USB调试
4. 在Android Studio中，你应该能看到你的设备出现在设备列表中

#### 步骤4: 构建和安装
1. 在Android Studio中点击绿色的"运行"按钮（▶️）
2. 选择你的Android设备
3. 点击"确定"
4. 等待构建完成，应用将自动安装到你的手机上

### 故障排除
- 如果构建失败，检查网络连接和API密钥是否正确
- 如果设备不显示，检查USB调试是否启用
- 如果安装失败，检查手机是否有足够存储空间

## 方法2: 直接安装APK文件

### 准备工作
1. 确保你的Android设备有足够存储空间（至少50MB）
2. 启用"未知来源"应用安装权限

### 详细步骤

#### 步骤1: 启用未知来源安装
1. 进入"设置" → "安全"（或"隐私"）
2. 找到"未知来源"或"安装未知应用"
3. 启用该选项

#### 步骤2: 下载和安装
1. 将APK文件传输到你的Android设备
2. 使用文件管理器找到APK文件
3. 点击APK文件
4. 按照提示完成安装

## 方法3: 通过ADB命令行安装

### 准备工作
1. 安装Android SDK Platform Tools
2. 启用USB调试（同方法1的步骤3）

### 详细步骤

#### 步骤1: 安装ADB工具
1. 下载 [Platform Tools](https://developer.android.com/studio/releases/platform-tools)
2. 解压到本地文件夹
3. 将文件夹路径添加到系统PATH环境变量

#### 步骤2: 连接设备
1. 用USB连接手机和电脑
2. 打开命令行工具（Windows用CMD，Mac/Linux用Terminal）
3. 运行命令：`adb devices`
4. 确认你的设备已连接

#### 步骤3: 安装APK
1. 将APK文件放在ADB工具同一目录
2. 运行命令：`adb install app-release.apk`
3. 等待安装完成

## 首次使用说明

1. 启动应用后，你会看到身份选择界面
2. 选择你的身份（柠檬🍋 或 西红柿🍅）
3. 应用将显示风暴瓶界面
4. 瓶子内显示对方城市的天气信息
5. 瓶子外坐着你选择的角色

## 注意事项

⚠️ **重要提醒**：
- 确保网络连接正常，否则无法获取天气信息
- 首次使用需要配置正确的API密钥
- 应用需要位置权限来获取天气信息
- 建议在WiFi环境下使用，避免消耗移动数据

## 技术支持

如果遇到问题：
1. 检查网络连接
2. 确认API密钥配置正确
3. 重启应用
4. 检查Android版本兼容性（需要Android 7.0+）

---

**祝你和女朋友使用愉快！** 💕
