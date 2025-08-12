#!/bin/bash

echo "🔧 开始修复Release版本问题..."

# 1. 清理构建缓存
echo "📦 清理构建缓存..."
./gradlew clean

# 2. 卸载旧版本（如果存在）
echo "🗑️ 卸载旧版本..."
adb uninstall com.stormglass.weather 2>/dev/null || echo "应用未安装或已卸载"

# 3. 构建Release版本
echo "🏗️ 构建Release版本..."
./gradlew assembleRelease

# 4. 检查构建是否成功
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    echo "✅ Release版本构建成功"
    
    # 5. 安装新版本
    echo "📱 安装Release版本..."
    adb install app/build/outputs/apk/release/app-release.apk
    
    echo ""
    echo "🎉 修复完成！"
    echo ""
    echo "📋 下一步操作："
    echo "1. 在设备上运行应用"
    echo "2. 选择身份（柠檬或西红柿）"
    echo "3. 查看Logcat日志（过滤标签：WeatherViewModel）"
    echo "4. 验证天气数据是否正确显示"
    echo ""
    echo "🔍 如果仍有问题，请检查Logcat中的错误信息"
else
    echo "❌ Release版本构建失败"
    echo "请检查构建错误信息"
fi
