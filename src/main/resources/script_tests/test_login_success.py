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

def test_successful_login():
    log_section("测试场景: 用户成功登录")
    url = f"{BASE_URL}/login"
    payload = ADMIN_USER

    print(f"[*] 准备向 {url} 发送POST请求")
    print(f"[*] 请求体 (Payload): \n{json.dumps(payload, indent=4, ensure_ascii=False)}")

    try:
        with requests.Session() as session:
            session.headers.update({'Content-Type': 'application/json; charset=utf-8'})
            response = session.post(url, json=payload, timeout=10)
            
            log_section("分析响应")
            log_response(response)

            log_section("最终诊断")
            if response.status_code == 200 and "userId" in response.json():
                print("✅ 测试通过: 成功登录，并返回了包含 userId 的用户信息。")
            else:
                print("❌ 测试失败: 响应状态码不是200或响应体中缺少'userId'。")

    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")
        print("  请检查后端服务是否在 http://localhost:8080 正常运行。")

if __name__ == '__main__':
    test_successful_login()