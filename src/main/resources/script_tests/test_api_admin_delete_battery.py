import requests
import json
import logging
import time

# --- 配置 ---
BASE_URL = "http://localhost:8080/api"
ADMIN_USER = {"username": "张三", "password": "123456"}

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
    except (json.JSONDecodeError, AttributeError, TypeError):
        # 204 No Content 没有响应体，会触发异常
        if response.status_code == 204:
            print("    (状态码为 204 No Content，无响应体)")
        else:
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
def test_admin_delete_battery_workflow():
    log_section("测试场景: 管理员删除(报废)电池的完整流程")
    session = get_authenticated_session(ADMIN_USER)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    # --- 步骤 1: 创建一个用于删除测试的电池 ---
    log_section("步骤 1: 创建一个用于删除的临时电池")
    add_url = f"{BASE_URL}/admin/batteries"
    add_payload = {
        "modelName": "临时待删除电池",
        "snCode": f"DELETE-ME-{int(time.time())}",
        "lifespanCycles": 10,
        "isExpensive": 0
    }
    print(f"[*] 准备向 {add_url} 发送POST请求")
    try:
        add_response = session.post(add_url, json=add_payload, timeout=10)
        if add_response.status_code != 201:
            print("❌ 步骤1失败: 创建临时电池失败，测试终止。")
            log_response(add_response)
            return
        
        created_battery = add_response.json()
        battery_id_to_delete = created_battery.get("materialId")
        print(f"✅ 步骤1成功: 成功创建临时电池，ID: {battery_id_to_delete}")
    except requests.exceptions.RequestException as e:
        print(f"❌ 步骤1异常: {e}")
        return

    # --- 步骤 2: 删除刚刚创建的电池 ---
    log_section(f"步骤 2: 删除(报废)刚刚创建的电池 (ID: {battery_id_to_delete})")
    delete_url = f"{BASE_URL}/admin/batteries/{battery_id_to_delete}"
    print(f"[*] 准备向 {delete_url} 发送DELETE请求")

    try:
        delete_response = session.delete(delete_url, timeout=10)
        log_section("分析删除操作的响应")
        log_response(delete_response)
        log_section("最终诊断")
        
        if delete_response.status_code == 204:
            print("✅ 测试通过: 服务器成功删除电池并按预期返回 204 No Content。")
        else:
            print(f"❌ 测试失败: 期望状态码为 204，但实际为 {delete_response.status_code}。")
            
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

# --- 主执行块 ---
if __name__ == '__main__':
    test_admin_delete_battery_workflow()