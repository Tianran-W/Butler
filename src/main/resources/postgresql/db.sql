-- 创建角色表（先创建，因为用户表需要引用它）
CREATE TABLE tb_role
(
    role_id     SERIAL PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL,
    permissions TEXT
);

-- 创建用户表
CREATE TABLE tb_user
(
    user_id    SERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role_id    INTEGER REFERENCES tb_role (role_id),
    department VARCHAR(50)
);

-- 创建物资分类表
CREATE TABLE tb_material_category
(
    category_id   SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL
);
ALTER TABLE tb_material_category ADD CONSTRAINT unique_category_name UNIQUE (category_name);

-- 创建物资表
CREATE TABLE tb_material
(
    material_id   SERIAL PRIMARY KEY,
    material_name VARCHAR(100) NOT NULL,
    category_id   INTEGER REFERENCES tb_material_category (category_id),
    is_expensive  SMALLINT              DEFAULT 0 CHECK (is_expensive IN (0, 1)),
    sn_code       VARCHAR(50),
    quantity      INTEGER      NOT NULL DEFAULT 0,
    usage_limit   INTEGER,
    status        VARCHAR(20)  NOT NULL
);

-- 创建物资使用记录表
CREATE TABLE tb_material_usage_record
(
    record_id     SERIAL PRIMARY KEY,
    material_id   INTEGER REFERENCES tb_material (material_id),
    user_id       INTEGER REFERENCES tb_user (user_id),
    borrow_time   TIMESTAMP NOT NULL,
    return_time   TIMESTAMP,
    usage_project VARCHAR(100)
);

-- 创建审批表
CREATE TABLE tb_approval
(
    approval_id     SERIAL PRIMARY KEY,
    material_id     INTEGER REFERENCES tb_material (material_id),
    user_id         INTEGER REFERENCES tb_user (user_id),
    approval_reason TEXT        NOT NULL,
    approval_status VARCHAR(20) NOT NULL,
    approval_time   TIMESTAMP
);

-- 创建电池状态表
CREATE TABLE tb_battery_status
(
    status_id     SERIAL PRIMARY KEY,
    material_id   INTEGER REFERENCES tb_material (material_id),
    battery_level INTEGER CHECK (battery_level BETWEEN 0 AND 100),
    battery_health VARCHAR(20),
    record_time TIMESTAMP NOT NULL
);

-- 创建报销关联表
CREATE TABLE tb_reimbursement_relation
(
    relation_id      SERIAL PRIMARY KEY,
    material_id      INTEGER REFERENCES tb_material (material_id),
    reimbursement_id VARCHAR(50) NOT NULL
);

-- 生成用户名单
INSERT INTO tb_user (username, password, department)
VALUES ('张三', 'e10adc3949ba59abbe56e057f20f883e', '机械组'), -- 密码123456的MD5
       ('李四', 'e10adc3949ba59abbe56e057f20f883e', '视觉组'),
       ('王五', 'e10adc3949ba59abbe56e057f20f883e', '后勤组'),
       ('赵六', 'e10adc3949ba59abbe56e057f20f883e', '雷达组'),
       ('孙七', 'e10adc3949ba59abbe56e057f20f883e', '视觉组');

-- 插入分类：视觉、电控、硬件、机械
INSERT INTO tb_material_category (category_name)
VALUES ('视觉'),
       ('电控'),
       ('硬件'),
       ('机械');

-- 插入语句
INSERT INTO tb_material (material_name, category_id, is_expensive, sn_code, quantity, usage_limit, status)
VALUES ('工业相机', 1, 1, 'SN-CAM-001', 5, 15, '审批中'),
       ('伺服电机', 2, 1, 'SN-MOT-002', 3, NULL, '已借出'),
       ('电阻套件', 3, 0, 'SN-RES-003', 20, 21, '在库可借'),
       ('机械臂关节', 4, 1, 'SN-ARM-004', 2, Null, '在库可借');

-- 插入语句
INSERT INTO tb_material_usage_record (material_id, user_id, borrow_time, return_time, usage_project)
VALUES (2, 2, '2025-05-15 14:30:00', '2025-05-18 17:00:00', '视觉检测项目');

-- 插入语句
INSERT INTO tb_approval(material_id, user_id, approval_reason, approval_status, approval_time)
VALUES
    (1, 1, '借用工业相机用于测试', '待审批', NULL);