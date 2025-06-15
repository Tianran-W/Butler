import requests
import json
import logging

# --- 配置 ---
BASE_URL = "http://localhost:8080/api"
NORMAL_USER = {"username": "李四", "password": "123456"}
# TODO: 请确保此ID的电池在测试数据库中存在
BATTERY_ID_TO_TEST = 22 

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
def test_get_single_battery_by_id():
    log_section(f"测试场景: 用户查询单个电池详情 (ID: {BATTERY_ID_TO_TEST})")
    session = get_authenticated_session(NORMAL_USER)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    url = f"{BASE_URL}/batteries/{BATTERY_ID_TO_TEST}"
    print(f"[*] 准备向 {url} 发送GET请求")

    try:
        response = session.get(url, timeout=10)
        log_section("分析响应")
        log_response(response)
        log_section("最终诊断")
        if response.status_code == 200 and response.json().get("materialId") == BATTERY_ID_TO_TEST:
            print("✅ 测试通过: 成功获取到指定ID的电池详情。")
        elif response.status_code == 404:
            print(f"⚠️  测试未通过，但符合预期: 未找到ID为 {BATTERY_ID_TO_TEST} 的电池 (404 Not Found)。请检查测试数据。")
        else:
            print(f"❌ 测试失败: 期望状态码为 200，但实际为 {response.status_code}。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

# --- 主执行块 ---
if __name__ == '__main__':
    test_get_single_battery_by_id()