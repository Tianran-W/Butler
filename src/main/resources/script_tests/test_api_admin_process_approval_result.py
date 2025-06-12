import requests
import json
import logging
import time

BASE_URL = "http://localhost:8080/api"
ADMIN_USER = {"username": "张三", "password": "123456"}
NORMAL_USER = {"username": "李四", "password": "123456"}

EXPENSIVE_MATERIAL_ID = 10 

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

def test_full_approval_process():
    log_section("测试场景: 完整的借用审批流程")
    
    admin_session, _ = get_authenticated_session(ADMIN_USER)
    user_session, user_info = get_authenticated_session(NORMAL_USER)
    
    if not admin_session or not user_session:
        print("❌ 因登录失败，终止审批流程测试。")
        return

    log_section("步骤 1: 普通用户申请借用贵重物资")
    borrow_url = f"{BASE_URL}/addNewBorrow"
    borrow_payload = {
        "materialId": EXPENSIVE_MATERIAL_ID,
        "userId": user_info['userId'],
        "isExpensive": 1,
        "usageProject": "自动化审批流程测试项目",
        "approvalReason": f"需要测试，时间戳: {time.time()}"
    }
    print(f"[*] 准备向 {borrow_url} 发送POST请求")
    print(f"[*] 请求体: \n{json.dumps(borrow_payload, indent=4, ensure_ascii=False)}")
    try:
        borrow_response = user_session.post(borrow_url, json=borrow_payload, timeout=10)
        log_response(borrow_response)
        if borrow_response.status_code != 200:
            print("❌ 步骤1失败: 提交借用申请失败。")
            return
    except requests.exceptions.RequestException as e:
        print(f"❌ 步骤1异常: {e}")
        return

    print("⏳ 等待1秒确保记录已写入数据库...")
    time.sleep(1)

    log_section("步骤 2: 管理员获取待审批列表并找到该申请")
    approval_list_url = f"{BASE_URL}/admin/getApprovalRecord"
    approval_info = None
    try:
        list_response = admin_session.get(approval_list_url, timeout=10)
        log_response(list_response)
        if list_response.status_code != 200:
            print("❌ 步骤2失败: 获取审批列表失败。")
            return
        
        records = list_response.json()
        for record in records:
            if (record.get('materialId') == EXPENSIVE_MATERIAL_ID and 
                record.get('userId') == user_info['userId'] and 
                record.get('approvalStatus') == '待审批'):
                approval_info = record
                print(f"✅ 成功在列表中找到待审批记录, ID: {approval_info.get('approvalId')}")
                break
        
        if not approval_info:
            print("❌ 步骤2失败: 未在审批列表中找到刚刚提交的申请。")
            return
            
    except (requests.exceptions.RequestException, json.JSONDecodeError) as e:
        print(f"❌ 步骤2异常: {e}")
        return

    log_section("步骤 3: 管理员处理该审批请求 (同意)")
    process_url = f"{BASE_URL}/admin/ApprovalResult"
    process_payload = {
        "approvalId": approval_info['approvalId'],
        "materialId": approval_info['materialId'],
        "userId": approval_info['userId'],
        "approvalResult": True,
        "approvalReason": "同意借用，自动化测试"
    }
    print(f"[*] 准备向 {process_url} 发送POST请求")
    print(f"[*] 请求体: \n{json.dumps(process_payload, indent=4, ensure_ascii=False)}")
    try:
        process_response = admin_session.post(process_url, json=process_payload, timeout=10)
        log_response(process_response)
        if process_response.status_code != 200:
            print("❌ 步骤3失败: 处理审批请求失败。")
            return
    except requests.exceptions.RequestException as e:
        print(f"❌ 步骤3异常: {e}")
        return

    log_section("最终诊断")
    print("✅ 完整的申请与审批流程测试成功。")

if __name__ == '__main__':
    test_full_approval_process()