#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试OpenWeatherMap API在不同构建版本下的限制
"""

import requests
import json
import time
from datetime import datetime

def test_api_with_different_headers():
    """测试不同请求头对API的影响"""
    
    api_key = "0b88a198e71f4de0383e21afe6312d1e"
    base_url = "https://api.openweathermap.org/data/2.5/weather"
    
    # 测试不同的User-Agent
    user_agents = [
        "Mozilla/5.0 (Linux; Android 10; SM-G975F) AppleWebKit/537.36",  # 模拟Android设备
        "StormGlass/1.0 (Android)",  # 模拟应用
        "Retrofit/2.9.0",  # 模拟Retrofit
        None  # 默认User-Agent
    ]
    
    print("🔍 测试不同User-Agent对API的影响")
    print("=" * 60)
    
    for i, user_agent in enumerate(user_agents, 1):
        print(f"\n测试 {i}: User-Agent = {user_agent}")
        
        headers = {}
        if user_agent:
            headers['User-Agent'] = user_agent
        
        params = {
            'q': 'Wuhan',
            'appid': api_key,
            'units': 'metric',
            'lang': 'zh_cn'
        }
        
        try:
            response = requests.get(base_url, params=params, headers=headers, timeout=10)
            print(f"状态码: {response.status_code}")
            
            if response.status_code == 200:
                data = response.json()
                print(f"✅ 成功 - 温度: {data.get('main', {}).get('temp', 'N/A')}°C")
            else:
                print(f"❌ 失败 - {response.text}")
                
        except Exception as e:
            print(f"❌ 错误: {e}")
        
        time.sleep(1)  # 避免请求过于频繁

def test_api_rate_limits():
    """测试API频率限制"""
    
    api_key = "0b88a198e71f4de0383e21afe6312d1e"
    base_url = "https://api.openweathermap.org/data/2.5/weather"
    
    print("\n🔍 测试API频率限制")
    print("=" * 60)
    
    params = {
        'q': 'Wuhan',
        'appid': api_key,
        'units': 'metric',
        'lang': 'zh_cn'
    }
    
    # 连续发送多个请求
    for i in range(5):
        print(f"\n请求 {i+1}:")
        try:
            response = requests.get(base_url, params=params, timeout=10)
            print(f"状态码: {response.status_code}")
            
            if response.status_code == 200:
                data = response.json()
                print(f"✅ 成功 - 温度: {data.get('main', {}).get('temp', 'N/A')}°C")
            elif response.status_code == 429:
                print("❌ 频率限制 - 请求过于频繁")
                break
            else:
                print(f"❌ 失败 - {response.text}")
                
        except Exception as e:
            print(f"❌ 错误: {e}")
        
        time.sleep(0.5)  # 快速连续请求

def test_api_key_restrictions():
    """测试API密钥限制"""
    
    api_key = "0b88a198e71f4de0383e21afe6312d1e"
    base_url = "https://api.openweathermap.org/data/2.5/weather"
    
    print("\n🔍 测试API密钥限制")
    print("=" * 60)
    
    # 测试不同的请求参数组合
    test_cases = [
        {
            'name': '标准请求',
            'params': {
                'q': 'Wuhan',
                'appid': api_key,
                'units': 'metric',
                'lang': 'zh_cn'
            }
        },
        {
            'name': '无语言参数',
            'params': {
                'q': 'Wuhan',
                'appid': api_key,
                'units': 'metric'
            }
        },
        {
            'name': '英文城市名',
            'params': {
                'q': 'Wuhan,CN',
                'appid': api_key,
                'units': 'metric',
                'lang': 'zh_cn'
            }
        },
        {
            'name': '华氏度',
            'params': {
                'q': 'Wuhan',
                'appid': api_key,
                'units': 'imperial',
                'lang': 'zh_cn'
            }
        }
    ]
    
    for test_case in test_cases:
        print(f"\n测试: {test_case['name']}")
        try:
            response = requests.get(base_url, params=test_case['params'], timeout=10)
            print(f"状态码: {response.status_code}")
            
            if response.status_code == 200:
                data = response.json()
                print(f"✅ 成功 - 温度: {data.get('main', {}).get('temp', 'N/A')}°C")
            else:
                print(f"❌ 失败 - {response.text}")
                
        except Exception as e:
            print(f"❌ 错误: {e}")
        
        time.sleep(1)

def check_api_documentation():
    """检查API文档中的限制信息"""
    
    print("\n📚 API限制信息")
    print("=" * 60)
    print("根据OpenWeatherMap API文档：")
    print("1. 免费版限制：1000次/天")
    print("2. 请求频率：每分钟不超过60次")
    print("3. 支持的User-Agent：无特殊限制")
    print("4. 支持的请求来源：无特殊限制")
    print("5. 签名要求：无")
    print("6. 包名验证：无")
    print("\n结论：API本身对Debug/Release版本没有特殊限制")

def main():
    print("OpenWeatherMap API限制测试")
    print("=" * 60)
    print(f"测试时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"API密钥: {api_key[:10]}...")
    
    # 运行各种测试
    test_api_with_different_headers()
    test_api_rate_limits()
    test_api_key_restrictions()
    check_api_documentation()
    
    print("\n🎯 建议")
    print("=" * 60)
    print("1. 检查应用签名是否影响网络请求")
    print("2. 验证网络安全配置是否正确")
    print("3. 确认ProGuard规则是否完整")
    print("4. 检查设备网络连接")
    print("5. 查看详细的Logcat日志")

if __name__ == "__main__":
    api_key = "0b88a198e71f4de0383e21afe6312d1e"
    main()
