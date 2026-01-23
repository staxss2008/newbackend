-- 初始化当月公休日期配置
INSERT INTO fee_standard (config_key, config_value, config_type, category, description, is_editable, sort_order, created_at, updated_at)
VALUES
('current_month_rest_days', '', 'STRING', 'work_time', '当月公休日期（逗号分隔的日期列表，格式：YYYY-MM-DD）', 1, 4, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    config_value = VALUES(config_value),
    description = VALUES(description),
    updated_at = NOW();
