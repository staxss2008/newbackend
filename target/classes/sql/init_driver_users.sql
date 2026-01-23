-- 为驾驶员创建普通用户账号
-- 用户名为驾驶员名字的拼音，密码为123456（使用BCrypt加密）
-- 角色为1（驾驶员），并关联到对应的驾驶员ID

-- 宿盼
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'supan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '宿盼', '13812345678', 1, 
       (SELECT id FROM driver WHERE name = '宿盼' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'supan');

-- 李岩
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'liyan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李岩', '13912345678', 1,
       (SELECT id FROM driver WHERE name = '李岩' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'liyan');

-- 薛涛
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'xuetao', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '薛涛', '13712345678', 1,
       (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'xuetao');

-- 刘学飞
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'liuxuefei', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '刘学飞', '13612345678', 1,
       (SELECT id FROM driver WHERE name = '刘学飞' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'liuxuefei');

-- 王宝国
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'wangbaoguo', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王宝国', '13512345678', 1,
       (SELECT id FROM driver WHERE name = '王宝国' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'wangbaoguo');

-- 江涛
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'jiangtao', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '江涛', '13412345678', 1,
       (SELECT id FROM driver WHERE name = '江涛' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'jiangtao');

-- 宋鹏
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'songpeng', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '宋鹏', '13312345678', 1,
       (SELECT id FROM driver WHERE name = '宋鹏' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'songpeng');

-- 芮金龙
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'ruijinlong', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '芮金龙', '13212345678', 1,
       (SELECT id FROM driver WHERE name = '芮金龙' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'ruijinlong');

-- 王晟源
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'wangshengyuan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王晟源', '13112345678', 1,
       (SELECT id FROM driver WHERE name = '王晟源' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'wangshengyuan');

-- 赵春峰
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'zhaochunfeng', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵春峰', '13012345678', 1,
       (SELECT id FROM driver WHERE name = '赵春峰' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'zhaochunfeng');

-- 王岩
INSERT INTO sys_user (username, password, real_name, phone, role, driver_id, status, created_at, updated_at)
SELECT 'wangyan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王岩', '18912345678', 1,
       (SELECT id FROM driver WHERE name = '王岩' LIMIT 1), 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'wangyan');
