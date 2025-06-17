import requests
import json
import logging
import io

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
    login_url = f"{BASE_URL}/login"
    try:
        headers = {'Content-Type': 'application/json; charset=utf-8'}
        response = session.post(login_url, json=user_credentials, headers=headers, timeout=10)
        response.raise_for_status()
        print(f"✅ 登录成功: {user_credentials.get('username')}")
        return session
    except requests.exceptions.RequestException as e:
        print(f"❌ 登录失败: {user_credentials.get('username')}. 错误: {e}")
        return None

def test_upload_image():
    log_section("测试场景: 上传图片")
    session = get_authenticated_session(USER_CREDENTIALS)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    url = f"{BASE_URL}/uploadImage"
    dummy_image_bytes = b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x06\x00\x00\x00\x1f\x15\xc4\x89\x00\x00\x00\nIDATx\x9cc\x00\x01\x00\x00\x05\x00\x01\r\n-\xb4\x00\x00\x00\x00IEND\xaeB`\x82'
    files = {'file': ('test_upload.png', io.BytesIO(dummy_image_bytes), 'image/png')}
    data = {'recordType': 'borrow', 'recordId': '1000000000000000'}

    print(f"[*] 准备向 {url} 发送POST (multipart/form-data) 请求")
    print(f"[*] 表单数据: {data}")

    try:
        response = session.post(url, files=files, data=data, timeout=15)
        log_section("分析响应")
        log_response(response)
        log_section("最终诊断")
        if response.status_code == 200 and "imageId" in response.json() and "imagePath" in response.json():
            print("✅ 测试通过: 服务器成功处理图片上传并返回了包含'imageId'和'imagePath'的响应。")
        else:
            print("❌ 测试失败: 状态码不为200或响应体中缺少必要字段。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")

if __name__ == '__main__':
    test_upload_image()