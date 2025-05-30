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
VALUES ('工业相机', 1, 1, 'SN-CAM-001', 5, '2026-12-31', '可用'),
       ('伺服电机', 2, 1, 'SN-MOT-002', 3, NULL, '借出'),
       ('电阻套件', 3, 0, 'SN-RES-003', 20, '2025-10-01', '可用'),
       ('机械臂关节', 4, 1, 'SN-ARM-004', 2, NULL, '送修');

-- 插入语句
INSERT INTO tb_material_usage_record (material_id, user_id, borrow_time, return_time, usage_project)
VALUES (1, 1, '2025-05-20 09:00:00', NULL, '机械臂研发项目'),
       (2, 2, '2025-05-15 14:30:00', '2025-05-18 17:00:00', '视觉检测项目');

-- 插入语句
INSERT INTO tb_approval(material_id, user_id, approval_reason, approval_status, approval_time)
    VALUES
    (1, 1, '借用工业相机用于测试', '待审批', NULL),
    (3, 3, '领取电阻套件用于维修', '通过', '2025-05-20 10:00:00');