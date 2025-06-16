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
    
    content_type = response.headers.get('Content-Type', '')
    if 'json' in content_type:
        print("[*] 响应体 (JSON):")
        try:
            response_json = response.json()
            print(json.dumps(response_json, indent=4, ensure_ascii=False))
        except json.JSONDecodeError:
            print(f"    (响应体不是有效的JSON格式，打印原始文本)\n    {response.text}")
    elif response.status_code == 204:
        print("[*] 响应体: (无内容)")
    elif response.content:
        print(f"[*] 响应体 (二进制内容, 大小: {len(response.content)} 字节):")
    else:
        print("[*] 响应体: (无内容)")


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

def test_download_and_delete_image():
    log_section("测试场景: 下载并删除图片")
    session = get_authenticated_session(USER_CREDENTIALS)
    if not session:
        print("❌ 因登录失败，终止测试。")
        return

    # 步骤 1: 上传一张临时图片用于后续操作
    log_section("步骤 1: 上传一张临时图片")
    upload_url = f"{BASE_URL}/uploadImage"
    dummy_image_bytes = b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x06\x00\x00\x00\x1f\x15\xc4\x89\x00\x00\x00\nIDATx\x9cc\x00\x01\x00\x00\x05\x00\x01\r\n-\xb4\x00\x00\x00\x00IEND\xaeB`\x82'
    files = {'file': ('temp_for_delete.png', io.BytesIO(dummy_image_bytes), 'image/png')}
    data = {'recordType': 'scrap', 'recordId': '103'}
    
    image_id_to_process = None
    try:
        upload_resp = session.post(upload_url, files=files, data=data, timeout=15)
        if upload_resp.status_code == 200:
            image_id_to_process = upload_resp.json().get('imageId')
            print(f"✅ 步骤1成功: 已上传临时图片，ID: {image_id_to_process}")
        else:
            print("❌ 步骤1失败: 上传临时图片失败，测试终止。")
            log_response(upload_resp)
            return
    except requests.exceptions.RequestException as e:
        print(f"❌ 步骤1异常: {e}")
        return

    # 步骤 2: 下载该图片
    log_section(f"步骤 2: 下载图片 (ID: {image_id_to_process})")
    download_url = f"{BASE_URL}/images/{image_id_to_process}"
    print(f"[*] 准备向 {download_url} 发送GET请求")
    try:
        response = session.get(download_url, timeout=10)
        log_response(response)
        if response.status_code == 200 and response.content == dummy_image_bytes:
            print("✅ 步骤2成功: 成功下载图片，且内容与上传时一致。")
        else:
            print("❌ 步骤2失败: 下载请求失败或内容不匹配。")
            # 即使下载失败，也尝试继续删除，以清理数据
    except requests.exceptions.RequestException as e:
        print(f"❌ 步骤2异常: {e}")

    # 步骤 3: 删除该图片
    log_section(f"步骤 3: 删除图片 (ID: {image_id_to_process})")
    delete_url = f"{BASE_URL}/images/{image_id_to_process}"
    print(f"[*] 准备向 {delete_url} 发送DELETE请求")
    try:
        response = session.delete(delete_url, timeout=10)
        log_section("分析删除响应")
        log_response(response)
        log_section("最终诊断")
        if response.status_code == 204:
            print("✅ 测试通过: 服务器成功删除图片并返回 204 No Content。")
        else:
            print(f"❌ 测试失败: 期望状态码为 204，但实际为 {response.status_code}。")
    except requests.exceptions.RequestException as e:
        log_section("请求异常")
        print(f"❌ 请求过程中发生错误: {e}")


if __name__ == '__main__':
    test_download_and_delete_image()