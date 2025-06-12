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

def test_password_change_workflow():
    session = get_authenticated_session(ADMIN_USER)
    if not session:
        print("❌ 因登录失败，终止密码修改测试。")
        return

    url = f"{BASE_URL}/user/password"
    new_password = "newpassword123"
    
    log_section("步骤1: 修改为新密码")
    payload_to_new = {"currentPassword": ADMIN_USER["password"], "newPassword": new_password}
    print(f"[*] 准备向 {url} 发送PUT请求")
    print(f"[*] 请求体: {json.dumps(payload_to_new)}")
    
    try:
        response_to_new = session.put(url, json=payload_to_new, timeout=10)
        log_response(response_to_new)
        if response_to_new.status_code != 200:
            print("❌ 修改为新密码失败，测试终止。")
            return
        print("✅ 成功修改为新密码。")
    except requests.exceptions.RequestException as e:
        print(f"❌ 修改为新密码时发生请求异常: {e}")
        return

    log_section("步骤2: 恢复为原密码")
    payload_to_revert = {"currentPassword": new_password, "newPassword": ADMIN_USER["password"]}
    print(f"[*] 准备向 {url} 发送PUT请求")
    print(f"[*] 请求体: {json.dumps(payload_to_revert)}")

    try:
        response_to_revert = session.put(url, json=payload_to_revert, timeout=10)
        log_response(response_to_revert)
        if response_to_revert.status_code != 200:
            print("❌ 恢复为原密码失败。")
            return
        print("✅ 成功恢复为原密码。")
    except requests.exceptions.RequestException as e:
        print(f"❌ 恢复为原密码时发生请求异常: {e}")
        return
        
    log_section("最终诊断")
    print("✅ 密码修改与恢复全流程测试完成。")

if __name__ == '__main__':
    test_password_change_workflow()