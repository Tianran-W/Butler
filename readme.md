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

2025/6/10提交：第一轮迭代分支commit重命名：这是第一轮迭代的分支的最后一次提交（封）