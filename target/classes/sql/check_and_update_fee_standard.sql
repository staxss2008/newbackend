-- 检查并更新费用标准
-- 查看当前的费用标准数据
SELECT * FROM fee_standard WHERE config_key = 'mileage_unit_price';

-- 更新公里单价为0.1元
UPDATE fee_standard 
SET config_value = '0.1', 
    updated_at = NOW() 
WHERE config_key = 'mileage_unit_price';

-- 验证更新结果
SELECT * FROM fee_standard WHERE config_key = 'mileage_unit_price';
