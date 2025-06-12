import requests
import json
import logging

BASE_URL = "http://localhost:8080/api"
ADMIN_USER = {"username": "张三", "password": "123456"}

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

def get_authenticated_session(user_credentials):
    log_section(f"准备登录: {user_credentials.get('username')}")
    session = requests.Session()
    session.headers.update({'Content-Type': 'application/json; charset=utf-8', 'Accept': 'application/json'})
    login_url = f"{BASE_URL}/login"
    try:
        response = session.post(login_url, json=user_credentials, timeout=10)
        response.raise_for_status()
        print(f"✅ 登录成功: {user_credentials.get('username')}")
        return session
    except requests.exceptions.RequestException as e:
        print(f"❌ 登录失败: {user_credentials.get('username')}. 错误: {e}")
        return None

def test_admin_fetch_categories():
    log_section("测试场景: 管理员获取物资分类列表")
    admin_session = get_authenticated_session(ADMIN_USER)
    if not admin_session:
        print("❌ 因登录失败，终止测试。")
        return

    url = f"{BASE_URL}/admin/materialsCategories"
    print(f"[*] 准备向 {url} 发送GET请求")
    
    try:
        response = admin_session.get(url, timeout=10)
        
        log_section("分析响应")
        log_response(response)
        
        log_section("最终诊断")
        if response.status_code == 200 and isinstance(response.json(), list):
            print("✅ 测试通过: 成功获取物资分类列表，且格式为JSON数组。")
        else:
            print("❌ 测试失败: 状态码不为200或响应格式不正确。")
            
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

if __name__ == '__main__':
    test_admin_fetch_categories()