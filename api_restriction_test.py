#!/usr/bin/env python3
import requests
import time

def test_api_restrictions():
    """测试API在不同情况下的限制"""
    
    api_key = "0b88a198e71f4de0383e21afe6312d1e"
    base_url = "https://api.openweathermap.org/data/2.5/weather"
    
    print("🔍 测试OpenWeatherMap API限制")
    print("=" * 50)
    
    # 测试1：标准请求
    print("\n1. 标准请求测试")
    params = {'q': 'Wuhan', 'appid': api_key, 'units': 'metric', 'lang': 'zh_cn'}
    response = requests.get(base_url, params=params, timeout=10)
    print(f"状态码: {response.status_code}")
    if response.status_code == 200:
        data = response.json()
        print(f"✅ 成功 - 温度: {data.get('main', {}).get('temp', 'N/A')}°C")
    
    # 测试2：不同User-Agent
    print("\n2. 不同User-Agent测试")
    headers = {'User-Agent': 'StormGlass/1.0 (Android)'}
    response = requests.get(base_url, params=params, headers=headers, timeout=10)
    print(f"状态码: {response.status_code}")
    
    # 测试3：频率限制
    print("\n3. 频率限制测试")
    for i in range(3):
        response = requests.get(base_url, params=params, timeout=10)
        print(f"请求 {i+1}: {response.status_code}")
        time.sleep(0.5)

if __name__ == "__main__":
    test_api_restrictions()
