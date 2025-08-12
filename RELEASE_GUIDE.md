# Storm Glass 发布指南

## 生成发布版APK的完整步骤

### 1. 创建签名密钥

#### 方法一：通过Android Studio图形界面
1. 菜单栏：`Build` → `Generate Signed Bundle / APK`
2. 选择 `APK`
3. 点击 `Create new` 创建新的密钥库
4. 填写密钥信息：
   ```
   Key store path: 选择保存位置（如：stormglass.jks）
   Password: 设置密钥库密码
   Alias: 密钥别名（如：stormglass）
   Validity: 有效期（建议25年以上）
   Certificate: 填写您的信息
   ```

#### 方法二：通过命令行
```bash
keytool -genkey -v -keystore stormglass.jks -keyalg RSA -keysize 2048 -validity 10000 -alias stormglass
```

### 2. 配置签名信息

1. 将生成的 `stormglass.jks` 文件复制到 `app/` 目录下
2. 修改 `app/build.gradle` 中的签名配置：
   ```gradle
   signingConfigs {
       release {
           storeFile file("stormglass.jks")
           storePassword "您的密钥库密码"
           keyAlias "stormglass"
           keyPassword "您的密钥密码"
       }
   }
   ```

### 3. 生成发布版APK

#### 方法一：通过Android Studio
1. 菜单栏：`Build` → `Generate Signed Bundle / APK`
2. 选择 `APK`
3. 选择已创建的密钥库
4. 选择 `release` 构建类型
5. 点击 `Finish` 开始构建

#### 方法二：通过命令行
```bash
# 在项目根目录执行
./gradlew assembleRelease
```

### 4. 找到生成的APK

生成的APK文件位于：
```
app/build/outputs/apk/release/app-release.apk
```

### 5. 测试发布版APK

1. 卸载调试版本
2. 安装发布版APK
3. 测试所有功能是否正常
4. 检查天气API是否正常工作

## 发布注意事项

### 安全考虑
- **不要将密钥文件上传到Git仓库**
- **妥善保管密钥密码**
- **定期备份密钥文件**

### 版本管理
- 每次发布前增加 `versionCode` 和 `versionName`
- 记录每次发布的变更内容
- 保存每个版本的APK文件

### 分发方式
1. **直接分享APK文件**
2. **上传到网盘分享链接**
3. **使用应用分发平台**
4. **通过邮件附件发送**

### 安装说明
告知用户：
1. 需要开启"未知来源"应用安装权限
2. 最低Android版本要求：6.0 (API 24)
3. 需要网络权限获取天气数据
4. 首次使用需要选择身份

## 常见问题

### Q: 生成的APK文件很大怎么办？
A: 启用代码混淆和资源压缩：
```gradle
release {
    minifyEnabled true
    shrinkResources true
}
```

### Q: 如何减小APK体积？
A: 
1. 使用WebP格式图片
2. 移除未使用的资源
3. 启用代码混淆
4. 使用App Bundle格式

### Q: 用户安装时提示"解析包时出现问题"？
A: 检查：
1. 目标设备Android版本是否满足要求
2. APK文件是否完整
3. 签名是否正确

## 发布检查清单

- [ ] 签名密钥已配置
- [ ] 版本号已更新
- [ ] 所有功能已测试
- [ ] 天气API正常工作
- [ ] 发布版APK已生成
- [ ] APK已测试安装
- [ ] 发布说明已准备
