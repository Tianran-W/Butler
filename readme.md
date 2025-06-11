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

## 二、数据库图片表设计
```sql
CREATE TABLE tb_image (
    image_id SERIAL PRIMARY KEY,
    record_type VARCHAR(20) NOT NULL CHECK (record_type IN ('borrow', 'return', 'scrap')),
    record_id INTEGER NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    upload_time TIMESTAMP DEFAULT NOW()
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
### 借还照片管理接口
*   **上传借还照片并直接关联记录**
    *   URL：`/api/uploadImage`
    *   方法：`POST`
    *   请求体 ：
        ```json
        {
            "file": "<MultipartFile>", 
            "recordType": "string",    // 'borrow', 'return', 'scrap'
            "recordId": "integer"
        }
        ```
    *   返回：
        ```json
        {
            "imageId": 101,
            "imagePath": "/uploads/borrow/20230601/abc.jpg" 
        }
        ```

### 电池状态接口
*   **提交电池状态**
    *   URL：`/api/batteryStatus`
    *   方法：`POST`
    *   请求体：
        ```json
        {
            "materialId": 8,
            "batteryLevel": 80,
            "batteryHealth": "良好"
        }
        ```
*   **查询电池历史状态**
    *   URL：`/api/batteryHistory/{materialId}`
    *   方法：`GET`
    *   返回：
        ```json
        [{
            "recordTime": "2023-05-01 10:30",
            "batteryLevel": 85,
            "batteryHealth": "良好"
        }]
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

