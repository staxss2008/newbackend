-- 修复当月公休日期配置
-- 将config_type设置为STRING，并将config_value设置为空
UPDATE fee_standard 
SET config_type = 'STRING', config_value = ''
WHERE config_key = 'current_month_rest_days';

-- 如果不存在该配置，则创建
INSERT INTO fee_standard (config_key, config_value, config_type, category, description, is_editable, sort_order, created_at, updated_at)
SELECT 'current_month_rest_days', '', 'STRING', 'work_time', '当月公休日期（逗号分隔的日期列表，格式：YYYY-MM-DD）', 1, 4, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM fee_standard WHERE config_key = 'current_month_rest_days'
);
