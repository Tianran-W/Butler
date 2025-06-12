import requests
import json
import logging
import time

BASE_URL = "http://localhost:8080/api"
ADMIN_CREDENTIALS = {"username": "张三", "password": "123456"}
USER_CREDENTIALS = {"username": "李四", "password": "123456"}
MATERIAL_ID_FOR_APPROVAL = 10 

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

def test_admin_get_approval_record():
    log_section("测试场景: 管理员获取待审批列表")
    admin_session, _ = get_authenticated_session(ADMIN_CREDENTIALS)
    user_session, user_info = get_authenticated_session(USER_CREDENTIALS)
    if not admin_session or not user_session:
        print("❌ 因登录失败，终止测试。")
        return

    log_section(f"准备步骤: 用户申请贵重物资 ID:{MATERIAL_ID_FOR_APPROVAL} 以生成待审批记录")
    borrow_url = f"{BASE_URL}/addNewBorrow"
    borrow_payload = {
        "materialId": MATERIAL_ID_FOR_APPROVAL,
        "userId": user_info['userId'],
        "isExpensive": 1,
        "usageProject": "Automated Approval List Test",
        "approvalReason": f"Test reason {time.time()}"
    }
    try:
        borrow_response = user_session.post(borrow_url, json=borrow_payload, timeout=10)
        if borrow_response.status_code != 200:
            print(f"⚠️  准备步骤失败: 无法为 ID:{MATERIAL_ID_FOR_APPROVAL} 创建审批请求。测试将继续，但可能无法找到记录。")
        else:
            print(f"✅ 准备步骤成功: 已为物资 ID:{MATERIAL_ID_FOR_APPROVAL} 创建审批请求")
    except requests.exceptions.RequestException as e:
        print(f"⚠️  准备步骤异常: {e}。测试将继续，但可能失败。")
    
    time.sleep(1)

    log_section("核心测试: 管理员获取审批列表")
    url = f"{BASE_URL}/admin/getApprovalRecord"
    print(f"[*] 准备向 {url} 发送GET请求")
    
    try:
        response = admin_session.get(url, timeout=10)
        log_section("分析响应")
        log_response(response)
        log_section("最终诊断")
        if response.status_code == 200 and isinstance(response.json(), list):
            print("✅ 测试通过: 服务器成功返回审批列表。")
        else:
            print(f"❌ 测试失败: 期望状态码为 200 且响应为列表，但实际状态码为 {response.status_code}。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

if __name__ == '__main__':
    test_admin_get_approval_record()