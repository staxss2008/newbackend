-- 初始化费用标准数据
-- 月工作总天数
INSERT INTO fee_standard (config_key, config_value, config_type, category, description, is_editable, sort_order, created_at, updated_at)
VALUES ('monthly_working_days', '27', 'INTEGER', 'work_time', '月工作总天数', 1, 1, NOW(), NOW())
AS new_data
ON DUPLICATE KEY UPDATE 
    config_value = new_data.config_value,
    description = new_data.description,
    updated_at = NOW();
