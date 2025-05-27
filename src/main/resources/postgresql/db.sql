-- 分类表
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    sort_order INTEGER DEFAULT 0,
    CONSTRAINT fk_parent_category FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);
CREATE INDEX idx_categories_parent_id ON categories(parent_id);
CREATE INDEX idx_categories_sort_order ON categories(sort_order);

-- 物资表
CREATE TABLE materials (
    id BIGSERIAL PRIMARY KEY,
    material_name VARCHAR(255) NOT NULL,
    category_id BIGINT,
    specifications JSONB, -- 使用JSONB存储动态规格
    description TEXT,
    CONSTRAINT fk_material_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);
CREATE INDEX idx_materials_category_id ON materials(category_id);

-- 分类扩展属性定义表
CREATE TABLE category_attributes (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    attribute_name VARCHAR(100) NOT NULL, -- 显示名称
    attribute_key VARCHAR(100) NOT NULL,  -- 存储键
    attribute_type VARCHAR(50) NOT NULL,  -- STRING, NUMBER, BOOLEAN, SELECT
    options TEXT,                         -- SELECT类型的选项，可以是JSON数组字符串
    is_required BOOLEAN DEFAULT FALSE,
    sort_order INTEGER DEFAULT 0,
    CONSTRAINT fk_attribute_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    UNIQUE (category_id, attribute_key) -- 同一分类下属性键唯一
);

-- 审批状态同步记录表
CREATE TABLE approval_status_sync (
    id BIGSERIAL PRIMARY KEY,
    approval_id VARCHAR(100) NOT NULL, -- 飞书或其他审批系统的审批ID
    status VARCHAR(50),
    sync_timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);
CREATE INDEX idx_approval_status_sync_approval_id ON approval_status_sync(approval_id);