-- 更新月工作天数配置键名
-- 删除旧的work_days配置（如果存在）
DELETE FROM fee_standard WHERE config_key = 'work_days';

-- 确保使用正确的monthly_working_days配置
INSERT INTO fee_standard (config_key, config_value, config_type, category, description, is_editable, sort_order, created_at, updated_at)
VALUES ('monthly_working_days', '27', 'INTEGER', 'work_time', '月工作总天数', 1, 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    config_value = VALUES(config_value),
    description = VALUES(description),
    updated_at = NOW();
