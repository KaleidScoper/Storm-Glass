#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
OpenWeatherMap API测试脚本
用于验证API密钥是否有效，以及获取天气数据是否正常
"""

import requests
import json
import sys

def test_openweather_api(api_key, city="Wuhan"):
    """
    测试OpenWeatherMap API
    
    Args:
        api_key (str): API密钥
        city (str): 城市名称
    
    Returns:
        bool: 是否成功
    """
    
    # API基础URL
    base_url = "https://api.openweathermap.org/data/2.5/weather"
    
    # 请求参数
    params = {
        'q': city,
        'appid': api_key,
        'units': 'metric',  # 摄氏度
        'lang': 'zh_cn'     # 中文
    }
    
    try:
        print(f"正在测试城市: {city}")
        print(f"API URL: {base_url}")
        print(f"参数: {params}")
        print("-" * 50)
        
        # 发送请求
        response = requests.get(base_url, params=params, timeout=10)
        
        print(f"响应状态码: {response.status_code}")
        print(f"响应头: {dict(response.headers)}")
        print("-" * 50)
        
        if response.status_code == 200:
            data = response.json()
            print("✅ API测试成功！")
            print(f"城市: {data.get('name', 'N/A')}")
            print(f"温度: {data.get('main', {}).get('temp', 'N/A')}°C")
            print(f"湿度: {data.get('main', {}).get('humidity', 'N/A')}%")
            print(f"风速: {data.get('wind', {}).get('speed', 'N/A')} m/s")
            print(f"天气描述: {data.get('weather', [{}])[0].get('description', 'N/A')}")
            print(f"天气类型: {data.get('weather', [{}])[0].get('main', 'N/A')}")
            return True
        else:
            print(f"❌ API测试失败！")
            print(f"错误代码: {response.status_code}")
            try:
                error_data = response.json()
                print(f"错误信息: {error_data}")
            except:
                print(f"错误内容: {response.text}")
            return False
            
    except requests.exceptions.Timeout:
        print("❌ 请求超时")
        return False
    except requests.exceptions.ConnectionError:
        print("❌ 网络连接错误")
        return False
    except Exception as e:
        print(f"❌ 未知错误: {e}")
        return False

def test_multiple_cities(api_key):
    """测试多个城市"""
    cities = ["Wuhan", "Hefei", "Beijing", "Shanghai"]
    
    print("=" * 60)
    print("多城市API测试")
    print("=" * 60)
    
    success_count = 0
    for city in cities:
        print(f"\n测试城市: {city}")
        if test_openweather_api(api_key, city):
            success_count += 1
        print("-" * 30)
    
    print(f"\n测试结果: {success_count}/{len(cities)} 个城市成功")
    return success_count == len(cities)

def main():
    # 当前使用的API密钥
    current_api_key = "0b88a198e71f4de0383e21afe6312d1e"
    
    print("OpenWeatherMap API测试工具")
    print("=" * 60)
    print(f"当前API密钥: {current_api_key}")
    print("=" * 60)
    
    # 测试单个城市
    print("\n1. 测试单个城市 (Wuhan)")
    success = test_openweather_api(current_api_key, "Wuhan")
    
    if success:
        print("\n2. 测试多个城市")
        test_multiple_cities(current_api_key)
    else:
        print("\n❌ 单个城市测试失败，跳过多城市测试")
        print("\n建议:")
        print("1. 检查API密钥是否正确")
        print("2. 确认网络连接正常")
        print("3. 等待1-2小时让新API密钥激活")
        print("4. 访问 https://openweathermap.org/api 获取新的API密钥")

if __name__ == "__main__":
    main()
