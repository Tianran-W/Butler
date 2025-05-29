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

-- 创建物资表
CREATE TABLE tb_material
(
    material_id   SERIAL PRIMARY KEY,
    material_name VARCHAR(100) NOT NULL,
    category_id   INTEGER REFERENCES tb_material_category (category_id),
    is_expensive  SMALLINT              DEFAULT 0 CHECK (is_expensive IN (0, 1)),
    sn_code       VARCHAR(50),
    quantity      INTEGER      NOT NULL DEFAULT 0,
    usage_limit   DATE,
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