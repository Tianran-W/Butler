import requests
import json
import logging

BASE_URL = "http://localhost:8080/api"
USER_CREDENTIALS = {"username": "李四", "password": "123456"}

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

def test_user_access_admin_route():
    log_section("测试场景: 普通用户访问管理员专属接口")
    session = get_authenticated_session(USER_CREDENTIALS)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    admin_url = f"{BASE_URL}/admin/materialAlerts"
    print(f"[*] 准备向管理员接口 {admin_url} 发送GET请求")
    
    try:
        response = session.get(admin_url, timeout=10)
        log_section("分析响应")
        log_response(response)
        log_section("最终诊断")
        if response.status_code == 403:
            print("✅ 测试通过: 服务器按预期拒绝了普通用户的访问，返回 403 Forbidden。")
        else:
            print(f"❌ 测试失败: 期望状态码为 403，但实际为 {response.status_code}。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

if __name__ == '__main__':
    test_user_access_admin_route()