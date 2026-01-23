-- 初始化安全奖标准配置
INSERT INTO fee_standard (config_key, config_value, config_type, category, description, is_editable, sort_order, created_at, updated_at)
VALUES
('safety_bonus_standard', '200.00', 'DECIMAL', 'bonus', '安全奖标准', 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    config_value = VALUES(config_value),
    description = VALUES(description),
    updated_at = NOW();
