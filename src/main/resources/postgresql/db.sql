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

-- 视觉类物资
INSERT INTO tb_material (material_name, category_id, is_expensive, sn_code, quantity, usage_limit, status)
VALUES
    ('工业相机MV-CA013-10GM', 1, 1, 'VIS-2025001', 3, 10, '已借出'),
    ('鱼眼镜头180°', 1, 0, 'VIS-2025002', 5, 7, '在库可借'),
    ('视觉光源LED条形', 1, 0, 'VIS-2025003', 8, 7, '在库可借'),
    ('图像采集卡PCIe', 1, 1, 'VIS-2025004', 2, 15, '在库可借'),
    ('标定板12*8', 1, 0, 'VIS-2025005', 4, 7, '在库可借');

-- 电控类物资
INSERT INTO tb_material (material_name, category_id, is_expensive, sn_code, quantity, usage_limit, status)
VALUES
    ('STM32F4开发板', 2, 0, 'ELE-2025006', 10, 7, '在库可借'),
    ('Arduino Mega2560', 2, 0, 'ELE-2025007', 15, 7, '在库可借'),
    ('步进电机驱动TB6600', 2, 0, 'ELE-2025008', 8, 10, '已借出'),
    ('舵机MG996R', 2, 0, 'ELE-2025009', 20, 7, '在库可借'),
    ('PLC西门子S7-1200', 2, 1, 'ELE-2025010', 2, 14, '已借出');

-- 硬件类物资
INSERT INTO tb_material (material_name, category_id, is_expensive, sn_code, quantity, usage_limit, status)
VALUES
    ('树莓派4B 8GB', 3, 1, 'HAR-2025011', 5, 14, '在库可借'),
    ('2.4G无线模块', 3, 0, 'HAR-2025012', 12, 7, '在库可借'),
    ('超声波传感器HC-SR04', 3, 0, 'HAR-2025013', 15, 7, '在库可借'),
    ('红外避障传感器', 3, 0, 'HAR-2025014', 10, 7, '在库可借'),
    ('激光测距仪VL53L0X', 3, 1, 'HAR-2025015', 3, 10, '已借出');

-- 机械类物资
INSERT INTO tb_material (material_name, category_id, is_expensive, sn_code, quantity, usage_limit, status)
VALUES
    ('铝合金型材4040', 4, 0, 'MEC-2025016', 20, 14, '在库可借'),
    ('舵机支架套件', 4, 0, 'MEC-2025017', 15, 7, '在库可借'),
    ('直流减速电机', 4, 0, 'MEC-2025018', 10, 10, '已借出'),
    ('联轴器弹性', 4, 0, 'MEC-2025019', 30, 7, '在库可借'),
    ('机械臂套件6DOF', 4, 1, 'MEC-2025020', 2, 15, '在库可借');