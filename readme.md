# 物资管理系统后端项目

## 一、项目概述
本项目是一个物资管理系统的后端服务，基于Spring Boot和MyBatis-Plus构建，使用PostgreSQL作为数据库。系统提供了物资借用、归还、审批、分类管理等功能，适用于企业或组织内部的物资管理场景。

## 二、功能模块
1. **物资分类管理**：包括获取物资分布、新建物资类别和物资入库操作。
2. **物资借用管理**：支持新建借用记录、通过用户ID搜索相关借用信息。
3. **物资归还管理**：提供物资归还功能，更新相关记录和物资状态。
4. **审批管理**：处理物资借用的审批操作，更新审批状态和物资状态，并插入使用记录。
5. **物资信息获取**：获取所有物资信息。

## 三、技术栈
1. **后端框架**：Spring Boot
2. **数据库**：PostgreSQL
3. **ORM框架**：MyBatis-Plus
4. **其他**：Lombok、Jackson

## 四、项目结构
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           ├── controller：控制器层，处理HTTP请求
│   │           ├── dto：数据传输对象，用于前后端数据交互
│   │           ├── entity：实体类，对应数据库表结构
│   │           ├── ExceptionHandler：全局异常处理
│   │           ├── mapper：数据访问层，与数据库交互
│   │           ├── service：服务层，处理业务逻辑
│   │           │   └── impl：服务层实现类
│   │           ├── vo：视图对象，用于封装返回给前端的数据
│   │           └── ERPApplication.java：项目启动类
│   └── resources
│       ├── mapper：MyBatis XML映射文件
│       ├── postgresql：PostgreSQL数据库脚本
│       └── application.properties：项目配置文件
└── test
    └── java
        └── com
            └── example：测试类
```

## 五、后端服务端环境搭建
### 1. 安装Java和Maven
Java 17
Maven 使用IDEA内嵌的maven

### 2. 安装PostgreSQL
下载并安装PostgreSQL数据库，创建名为`erp_db`的数据库，并设置用户名和密码。

### 3. 配置数据库连接
在`src/main/resources/application.properties`文件中，配置数据库连接信息：
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/erp_db
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 4. 初始化数据库
执行`src/main/resources/postgresql/db.sql`脚本，创建数据库表并插入初始数据。

## 六、项目启动
### 1. 克隆项目
```bash
git clone https://github.com/Tianran-W/Butler.git
```

### 2. 编译项目
```bash
mvn clean
mvn build
```

### 3. 启动项目
```bash
mvn spring-boot:run
```

项目启动后，会监听`8080`端口。

## 七、接口文档
### 1. 物资分类管理接口
- **获取物资分布**
    - **URL**：`/api/admin/materialsCategories`
    - **方法**：`GET`
    - **返回**：物资分类列表

- **新建物资类别**
    - **URL**：`/api/admin/materialsNewCategories`
    - **方法**：`POST`
    - **请求体**：`CategoryDTO`
    - **返回**：无

- **物资入库**
    - **URL**：`/api/admin/addNewMaterials`
    - **方法**：`POST`
    - **请求体**：`Material`
    - **返回**：无

### 2. 物资借用管理接口
- **新建借用记录**
    - **URL**：`/api/addNewBorrow`
    - **方法**：`POST`
    - **请求体**：`BorrowDTO`
    - **返回**：无

- **通过用户ID搜索相关借用**
    - **URL**：`/api/findBorrowingByUserId`
    - **方法**：`POST`
    - **请求体**：`UserBorrowingQueryDTO`
    - **返回**：物资信息列表

### 3. 物资归还管理接口
- **提交归还**
    - **URL**：`/api/return`
    - **方法**：`POST`
    - **请求体**：`ReturnDTO`
    - **返回**：无

### 4. 审批管理接口
- **审批操作**
    - **URL**：`/api/admin/ApprovalResult`
    - **方法**：`POST`
    - **请求体**：`ApprovalResultDTO`
    - **返回**：无

### 5. 物资信息获取接口
- **获取所有物资**
    - **URL**：`/api/getAllMaterial`
    - **方法**：`GET`
    - **返回**：物资信息列表

---
# 第二轮迭代
## 一、新增功能模块
*   **用户权限管理**
    *   基于现有`tb_role`和`tb_user`表实现角色权限控制
    *   BCrypt存储密码
    *   用户表增加email字段
*   **大模型物资推荐**
    *   基于历史使用记录和大模型分析生成物资推荐方案（使用OKHTTP向 OPENAI 发送请求）
*   **物资预警系统**
    *   实现库存预警和归还提醒功能
*   **物资报废报销流程**
    *   新增物资报废和报销流程管理
*   **借还照片管理**
    *   新增借出/归还照片上传功能
*   **电池状态登记**
    *   新增电池状态记录功能
*   **SN码管理**
    *   完善SN码录入和查询功能

## 二、数据库表设计
```sql
CREATE TABLE tb_image (
    image_id SERIAL PRIMARY KEY,
    record_type VARCHAR(20) NOT NULL CHECK (record_type IN ('borrow', 'return', 'scrap')),
    record_id INTEGER NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    upload_time TIMESTAMP DEFAULT NOW()
);

CREATE TABLE tb_battery_info (
    material_id INTEGER PRIMARY KEY REFERENCES tb_material(material_id) ON DELETE CASCADE,
    lifespan_cycles INTEGER NOT NULL,
    current_cycles INTEGER NOT NULL DEFAULT 0
);
```
## 二、接口迭代设计

### 用户权限管理接口
*   **用户登录**
    *   URL：`/api/login`
    *   方法：`POST`
    *   请求体：
        ```json
        {
            "username": "string",
            "password": "string"
        }
        ```
    *   返回：
        ```json
        {
            "message": "登录成功",
            "userId": 1,
            "username": "张三",
            "role": "admin" // 或 "user"
        }
        ```
        *   失败时，HTTP状态码 `401 Unauthorized`。
        *   响应体：
        ```json
        {
            "error": "用户名或密码无效"
        }
        ```
    *   **备注**：登录成功后，客户端（如浏览器）会自动在后续请求中携带Session Cookie。

*   **获取用户角色**
    *   URL：`/api/user/role`
    *   方法：`GET`
    *   **请求头**：无特定认证请求头 (Session Cookie 会自动由客户端发送)
    *   返回：
        *   成功时，HTTP状态码 `200 OK`。
        *   响应体：
        ```json
        {
            "role": "admin" // 或 "user"
        }
        ```
        *   如果用户未登录（Session无效或不存在），返回 HTTP状态码 `401 Unauthorized`。

*   **用户登出**
    *   URL：`/api/logout`
    *   方法：`POST`
    *   **请求头**：无特定认证请求头 (Session Cookie 会自动由客户端发送)
    *   请求体：无
    *   返回：
        *   成功时，HTTP状态码 `200 OK`。服务器将销毁当前用户的Session。
        *   响应体示例：
        ```json
        {
            "message": "登出成功"
        }
        ```

*   **修改密码**
    *   URL：`/api/user/password`
    *   方法：`PUT` (或 `POST`)
    *   **请求头**：无特定认证请求头
    *   请求体：
        ```json
        {
            "currentPassword": "string", // 当前密码
            "newPassword": "string"      // 新密码
        }
        ```
    *   返回：
        *   成功时，HTTP状态码 `200 OK`。
        *   响应体示例：
        ```json
        {
            "message": "密码修改成功"
        }
        ```
        *   失败时（例如，当前密码错误，用户未登录等），HTTP状态码 `400 Bad Request` 或 `401 Unauthorized`。
        *   响应体示例 (当前密码错误)：
        ```json
        {
            "error": "当前密码不正确"
        }
        ```
        *   响应体示例 (未登录)：
        ```json
        {
            "error": "用户未登录或会话已过期"
        }
        ```
*   **用户注册**
    *   URL：`/api/register`
    *   方法：`POST`
    *   请求体：
        ```json
        {
          "username": "new_user",
          "email": "new_user@example.com",
          "password": "a_strong_password_123"
        }
        ```
    *   返回（成功）：
        ```json
        {
          "success": true,
          "message": "注册成功"
        }
        ```
    *   返回（失败 - 用户名已存在）：
        ```json
        {
            "status": 400,
            "code": "INVALID_PARAMETER",
            "message": "用户名 'new_user' 已存在"
        }
        ```
    *   返回（失败 - 其他）：
        ```json
        {
          "error": "邮箱 'new_user@example.com' 已被注册"
        }
        ```

### 大模型物资推荐接口
*   **获取推荐物资列表**
    *   URL：`/api/recommendMaterials`
    *   方法：`POST`
    *   请求体：
        ```json
        {
            "projectType": "机器人竞赛",
            "participantCount": 10
        }
        ```
    *   返回：
        ```json
        [{
            "materialId": 1,
            "materialName": "工业相机",
            "recommendReason": "历史同类项目使用率90%",
            "avgUsage": 2.5
        }]
        ```
### 物资预警接口
*   **库存预警列表**
    *   URL：`/api/admin/materialAlerts`
    *   方法：`GET`
    *   返回：
        ```json
        [{
            "materialId": 3,
            "materialName": "步进电机",
            "currentQuantity": 2,
            "alertThreshold": 5
        }]
        ```
*   **归还提醒**
    *   URL：`/api/returnReminders`
    *   方法：`GET`
    *   返回：
        ```json
        [{
            "materialId": 1,
            "materialName": "工业相机",
            "borrower": "张三",
            "dueDate": "2025-06-15"
        }]
        ```
### 物资报废报销接口
*   **提交报废申请**
    *   URL：`/api/material/scrap`
    *   方法：`POST`
    *   请求体：
        ```json
        {
            "materialId": 5,
            "reason": "电池膨胀无法使用",
        }
        ```
*   **关联报销信息**
    *   URL：`/api/reimbursement/link`
    *   方法：`POST`
    *   请求体：
        ```json
        {
            "materialId": 5,
            "reimbursementId": "BX202306001"
        }
        ```
### 图片服务接口
*   **上传图片**
    *   URL：`/api/uploadImage`
    *   方法：`POST`
    *   描述：上传一张图片，并将其与一个具体的记录（如借用、归还、报废等）关联。
    *   请求类型：`multipart/form-data`
    *   请求参数：
        *   `file`: (必需) 图片文件
        *   `recordType`: (必需) 关联的记录类型，字符串，可选值为 `borrow`, `return`, `scrap`
        *   `recordId`: (必需) 关联的记录ID，整数
    *   返回：
        ```json
        {
          "imageId": 123,
          "imagePath": "/borrow/2024-06-12/a1b2c3d4-e5f6-7890-a1b2-c3d4e5f67890.jpg"
        }
        ```

*   **下载/查看图片**
    *   URL：`/api/images/{imageId}`
    *   方法：`GET`
    *   描述：根据图片ID获取图片文件。浏览器可以直接显示，工具会下载文件。
    *   返回：图片文件本身，响应头 `Content-Type` 根据文件类型动态设置。

*   **查询关联记录的图片列表**
    *   URL：`/api/images/record/{recordType}/{recordId}`
    *   方法：`GET`
    *   权限：已认证用户
    *   描述：根据记录类型和记录ID，查询所有关联的图片元数据。
    *   返回：
        ```json
        [
          {
            "imageId": 123,
            "recordType": "borrow",
            "recordId": 55,
            "imagePath": "/borrow/2024-06-12/a1b2c3d4-e5f6-7890-a1b2-c3d4e5f67890.jpg",
            "uploadTime": "2024-06-12T10:30:00"
          },
          {
            "imageId": 124,
            "recordType": "borrow",
            "recordId": 55,
            "imagePath": "/borrow/2024-06-12/f6e5d4c3-b2a1-0987-6f5e-4d3c2b1a0987.png",
            "uploadTime": "2024-06-12T10:31:15"
          }
        ]
        ```

*   **删除图片**
    *   URL：`/api/images/{imageId}`
    *   方法：`DELETE`
    *   权限：已认证用户
    *   描述：根据图片ID从服务器文件系统和数据库中删除指定的图片。
    *   返回：成功时返回 `204 No Content`，无响应体。


### 电池管理接口

*   **新增电池**
    *   URL：`/api/admin/batteries`
    *   方法：`POST`
    *   权限：`admin`
    *   描述：管理员新增一个电池资产。电池创建后，状态默认为“在库可借”，当前循环次数为0。
    *   请求体 (JSON):
        ```json
        {
          "modelName": "DJI Tello 智能飞行电池",
          "snCode": "BATT-SN-20240612001",
          "lifespanCycles": 150
        }
        ```
    *   成功响应 (201 Created):
        ```json
        {
          "batteryId": 1,
          "modelName": "DJI Tello 智能飞行电池",
          "snCode": "BATT-SN-20240612001",
          "status": "在库可借",
          "lifespanCycles": 150,
          "currentCycles": 0
        }
        ```

*   **获取所有电池列表**
    *   URL：`/api/batteries`
    *   方法：`GET`
    *   权限：已认证用户
    *   描述：获取所有未被报废的电池列表。
    *   成功响应 (200 OK):
        ```json
        [
          {
            "batteryId": 1,
            "modelName": "DJI Tello 智能飞行电池",
            "snCode": "BATT-SN-20240612001",
            "status": "在库可借",
            "lifespanCycles": 150,
            "currentCycles": 10
          },
          {
            "batteryId": 2,
            "modelName": "DJI Mavic 3 Pro 电池",
            "snCode": "BATT-SN-20240611005",
            "status": "已借出",
            "lifespanCycles": 300,
            "currentCycles": 55
          }
        ]
        ```

*   **获取单个电池详情**
    *   URL：`/api/batteries/{batteryId}`
    *   方法：`GET`
    *   权限：已认证用户
    *   描述：根据电池ID查询其详细信息。
    *   URL参数：
        *   `batteryId`: (必需) 电池的唯一ID，整数。
    *   成功响应 (200 OK):
        ```json
        {
          "batteryId": 1,
          "modelName": "DJI Tello 智能飞行电池",
          "snCode": "BATT-SN-20240612001",
          "status": "在库可借",
          "lifespanCycles": 150,
          "currentCycles": 10
        }
        ```
    *   失败响应 (404 Not Found): 如果提供的`batteryId`不存在。

*   **更新电池信息**
    *   URL：`/api/admin/batteries/{batteryId}`
    *   方法：`PUT`
    *   权限：`admin`
    *   描述：管理员更新一个已存在电池的非关键信息，如型号名称、设计寿命等。
    *   URL参数：
        *   `batteryId`: (必需) 待更新电池的ID，整数。
    *   请求体 (JSON):
        ```json
        {
          "modelName": "DJI Tello 智能飞行电池 (增强版)",
          "lifespanCycles": 200
        }
        ```
    *   成功响应 (200 OK):
        ```json
        {
          "batteryId": 1,
          "modelName": "DJI Tello 智能飞行电池 (增强版)",
          "snCode": "BATT-SN-20240612001",
          "status": "在库可借",
          "lifespanCycles": 200,
          "currentCycles": 10
        }
        ```

*   **报废电池**
    *   URL：`/api/admin/batteries/{batteryId}`
    *   方法：`DELETE`
    *   权限：`admin`
    *   描述：管理员将指定ID的电池状态标记为“已报废”。这是一个逻辑删除，记录仍然保留在数据库中。
    *   URL参数：
        *   `batteryId`: (必需) 待报废电池的ID，整数。
    *   成功响应 (204 No Content): 无响应体。

### 电池状态接口

*   **提交电池状态**
    *   URL：`/api/batteryStatus`
    *   方法：`POST`
    *   权限：已认证用户
    *   描述：用户在使用电池后，提交其当前的状态信息，如电量、健康度。
    *   请求体 (JSON):
        ```json
        {
          "batteryId": 1,
          "batteryLevel": 88,
          "batteryHealth": "良好"
        }
        ```
    *   成功响应 (200 OK): 无响应体。

*   **查询电池历史状态**
    *   URL：`/api/batteryHistory/{batteryId}`
    *   方法：`GET`
    *   权限：已认证用户
    *   描述：根据电池ID，查询该电池的所有历史状态记录，按时间倒序排列。
    *   URL参数：
        *   `batteryId`: (必需) 电池的ID，整数。
    *   成功响应 (200 OK):
        ```json
        [
          {
            "statusId": 5,
            "batteryId": 1,
            "batteryLevel": 88,
            "batteryHealth": "良好",
            "recordTime": "2024-06-12T15:45:00"
          },
          {
            "statusId": 2,
            "batteryId": 1,
            "batteryLevel": 95,
            "batteryHealth": "良好",
            "recordTime": "2024-06-11T10:20:00"
          }
        ]
        ```

### SN码管理接口
*   **SN码查询**
    *   URL：`/api/material/sn/{snCode}`
    *   方法：`GET`
    *   返回：物资详情（含SN码）

### 权限测试矩阵
| 用户角色       | 管理员接口 | 普通用户接口 |
| :------------- | :--------- | :----------- |
| 管理员         | ✓          | ✓            |
| 普通用户       | ✗          | ✓            |
| 未认证用户     | ✗          | ✗            |




