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

def test_get_llm_recommendations():
    log_section("测试场景: 获取基于大模型的物资推荐列表")
    session = get_authenticated_session(USER_CREDENTIALS)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    url = f"{BASE_URL}/recommendMaterials"
    payload = {"projectType": "机器人对抗赛", "participantCount": 5}
    print(f"[*] 准备向 {url} 发送POST请求")
    print(f"[*] 请求体: \n{json.dumps(payload, indent=4, ensure_ascii=False)}")
    
    try:
        # 关键修改1: 增加超时时间。
        # 后端调用大模型API可能耗时较长，需要给足等待时间。90秒是一个比较安全的值。
        response = session.post(url, json=payload, timeout=90)
        log_section("分析响应")
        log_response(response)
        log_section("最终诊断")

        if response.status_code != 200:
            print(f"❌ 测试失败: 期望状态码为 200，但实际为 {response.status_code}。")
            return

        recommendations = response.json()
        
        # 关键修改2: 增强验证逻辑。
        if not isinstance(recommendations, list):
            print(f"❌ 测试失败: 响应体不是一个JSON列表。实际类型为: {type(recommendations)}")
            return
        
        # 如果大模型返回空列表，也是一个有效的成功场景。
        if not recommendations:
            print("✅ 测试通过: 服务器成功返回一个空的推荐列表，这是一个有效的响应。")
            return

        # 校验列表内每个元素的结构是否符合预期。
        print(f"[*] 收到 {len(recommendations)} 条推荐，正在校验其结构...")
        all_items_valid = True
        required_keys = {'materialId', 'materialName', 'recommendReason', 'avgUsage'}
        for i, item in enumerate(recommendations):
            if not isinstance(item, dict) or not required_keys.issubset(item.keys()):
                print(f"❌ 校验失败: 列表中的第 {i+1} 项结构不正确或缺少必要的键。")
                all_items_valid = False
                break
        
        if all_items_valid:
            print("✅ 测试通过: 服务器成功返回了推荐列表，且所有条目结构均正确。")
        else:
            print("❌ 测试失败: 推荐列表中的条目结构不符合预期。")

    except requests.exceptions.Timeout:
        print(f"❌ 请求超时: 在90秒内未收到服务器响应。请检查后端服务日志，确认是否成功调用大模型API，或大模型响应过慢。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

if __name__ == '__main__':
    test_get_llm_recommendations()