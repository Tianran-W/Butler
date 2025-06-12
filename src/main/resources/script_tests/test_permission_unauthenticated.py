import requests
import json
import logging

BASE_URL = "http://localhost:8080/api"

logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logging.getLogger("urllib3").setLevel(logging.INFO)

def log_section(title):
    print("\n" + "=" * 25 + f" {title} " + "=" * 25)

def log_response(response):
    print(f"[*] 状态码: {response.status_code}")
    print("[*] 响应头:")
    for key, value in response.headers.items():
        print(f"    {key}: {value}")
    print("[*] 响应体:")
    try:
        response_json = response.json()
        print(json.dumps(response_json, indent=4, ensure_ascii=False))
    except json.JSONDecodeError:
        print("    (响应体不是有效的JSON格式，打印原始文本)")
        print(f"    {response.text}")

def test_unauthenticated_access():
    log_section("测试场景: 未认证(未登录)用户访问需要认证的API")
    
    url = f"{BASE_URL}/user/role"
    print(f"[*] 准备向受保护的URL {url} 发送GET请求")
    
    try:
        with requests.Session() as unauthenticated_session:
            response = unauthenticated_session.get(url, timeout=10)
            
            log_section("分析响应")
            log_response(response)
            
            log_section("最终诊断")
            if response.status_code == 401:
                print("✅ 测试通过: 服务器按预期拒绝了未认证的访问，返回 401。")
            else:
                print(f"❌ 测试失败: 期望状态码为 401，实际为 {response.status_code}。")

    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

if __name__ == '__main__':
    test_unauthenticated_access()