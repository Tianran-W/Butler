import requests
import json
import logging
import time

BASE_URL = "http://localhost:8080/api"
USER_CREDENTIALS = {"username": "李四", "password": "123456"}
MATERIAL_ID_TO_RETURN = 7 

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
        user_info = response.json()
        return session, user_info
    except requests.exceptions.RequestException as e:
        print(f"❌ 登录失败: {user_credentials.get('username')}. 错误: {e}")
        return None, None

def test_user_return():
    log_section("测试场景: 用户提交归还")
    session, user_info = get_authenticated_session(USER_CREDENTIALS)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    log_section(f"准备步骤: 借用物资 ID:{MATERIAL_ID_TO_RETURN} 以便后续归还")
    borrow_url = f"{BASE_URL}/addNewBorrow"
    borrow_payload = {
        "materialId": MATERIAL_ID_TO_RETURN,
        "userId": user_info['userId'],
        "isExpensive": 0,
        "usageProject": "Automated Return Test"
    }
    try:
        borrow_response = session.post(borrow_url, json=borrow_payload, timeout=10)
        if borrow_response.status_code != 200:
            print(f"⚠️  准备步骤失败: 无法借用ID为 {MATERIAL_ID_TO_RETURN} 的物资。可能该物资已被借出。测试将继续，但可能失败。")
        else:
            print(f"✅ 准备步骤成功: 已借用物资 ID:{MATERIAL_ID_TO_RETURN}")
    except requests.exceptions.RequestException as e:
        print(f"⚠️  准备步骤异常: {e}。测试将继续，但可能失败。")
    
    time.sleep(1)

    log_section("核心测试: 归还物资")
    return_url = f"{BASE_URL}/return"
    return_payload = {"userId": user_info['userId'], "materialId": MATERIAL_ID_TO_RETURN}
    print(f"[*] 准备向 {return_url} 发送POST请求")
    print(f"[*] 请求体: \n{json.dumps(return_payload, indent=4, ensure_ascii=False)}")
    
    try:
        response = session.post(return_url, json=return_payload, timeout=10)
        log_section("分析响应")
        log_response(response)
        log_section("最终诊断")
        if response.status_code == 200:
            print("✅ 测试通过: 服务器成功处理归还请求。")
        else:
            print(f"❌ 测试失败: 期望状态码为 200，但实际为 {response.status_code}。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

if __name__ == '__main__':
    test_user_return()