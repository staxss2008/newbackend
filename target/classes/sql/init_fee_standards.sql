-- 初始化费用标准数据
-- 工作时间配置
INSERT INTO fee_standard (config_key, config_value, config_type, category, description, is_editable, sort_order, created_at, updated_at)
VALUES 
('work_start_time', '08:00', 'TIME', 'work_time', '上班时间', 1, 1, NOW(), NOW()),
('work_end_time', '17:00', 'TIME', 'work_time', '下班时间', 1, 2, NOW(), NOW()),
('monthly_working_days', '27', 'INTEGER', 'work_time', '月工作总天数', 1, 3, NOW(), NOW()),
('mileage_unit_price', '0.10', 'DECIMAL', 'mileage', '公里单价', 1, 1, NOW(), NOW()),
('duty_subsidy_amount', '100.00', 'DECIMAL', 'duty', '值班补贴', 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    config_value = VALUES(config_value),
    description = VALUES(description),
    updated_at = NOW();
