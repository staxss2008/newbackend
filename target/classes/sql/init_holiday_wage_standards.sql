-- 初始化公休日和节假日工资标准
INSERT INTO fee_standard (config_key, config_value, config_type, category, description, is_editable, sort_order, created_at, updated_at)
VALUES
('rest_day_wage', '0.00', 'DECIMAL', 'holiday', '公休日工资', 1, 1, NOW(), NOW()),
('holiday_wage', '0.00', 'DECIMAL', 'holiday', '节假日工资', 1, 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    config_value = VALUES(config_value),
    description = VALUES(description),
    updated_at = NOW();
