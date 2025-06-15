import requests
import json
import logging

# --- 配置 ---
BASE_URL = "http://localhost:8080/api"
ADMIN_USER = {"username": "张三", "password": "123456"}
# TODO: 请确保此ID的电池在测试数据库中存在
BATTERY_ID_TO_UPDATE = 22

# --- 日志和辅助函数 ---
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

# --- 测试函数 ---
def test_admin_update_battery():
    log_section(f"测试场景: 管理员更新电池信息 (ID: {BATTERY_ID_TO_UPDATE})")
    session = get_authenticated_session(ADMIN_USER)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    url = f"{BASE_URL}/admin/batteries/{BATTERY_ID_TO_UPDATE}"
    payload = {
      "modelName": "DJI Mavic 3 Pro 智能飞行电池",
      "lifespanCycles": 350
    }
    
    print(f"[*] 准备向 {url} 发送PUT请求")
    print(f"[*] 请求体: \n{json.dumps(payload, indent=4, ensure_ascii=False)}")

    try:
        response = session.put(url, json=payload, timeout=10)
        log_section("分析响应")
        log_response(response)
        log_section("最终诊断")
        if response.status_code == 200:
            response_json = response.json()
            if response_json.get("modelName") == payload["modelName"] and response_json.get("lifespanCycles") == payload["lifespanCycles"]:
                print("✅ 测试通过: 成功更新电池信息，且响应体内容正确。")
            else:
                print("❌ 测试失败: 状态码为200，但响应的更新后数据不正确。")
        elif response.status_code == 404:
            print(f"⚠️  测试未通过，但符合预期: 未找到ID为 {BATTERY_ID_TO_UPDATE} 的电池 (404 Not Found)。请检查测试数据。")
        else:
            print(f"❌ 测试失败: 期望状态码为 200，但实际为 {response.status_code}。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

# --- 主执行块 ---
if __name__ == '__main__':
    test_admin_update_battery()