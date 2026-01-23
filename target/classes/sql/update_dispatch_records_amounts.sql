-- 更新派车记录的加班时间和加班费等数据
-- 根据费用标准计算加班费、公里补贴等

-- 1. 更新加班小时数
UPDATE dispatch_record dr
SET 
    overtime_hours = (
        CASE 
            WHEN departure_time IS NOT NULL AND return_time IS NOT NULL THEN
                -- 上班前加班小时数
                CASE 
                    WHEN STR_TO_DATE(departure_time, '%H:%i:%s') < STR_TO_DATE(
                        (SELECT config_value FROM fee_standard WHERE config_key = 'work_start_time'), 
                        '%H:%i:%s'
                    ) THEN
                        FLOOR(TIMESTAMPDIFF(MINUTE, 
                            STR_TO_DATE(departure_time, '%H:%i:%s'), 
                            STR_TO_DATE((SELECT config_value FROM fee_standard WHERE config_key = 'work_start_time'), '%H:%i:%s')
                        ) / 60)
                    ELSE 0
                END +
                -- 下班后加班小时数
                CASE 
                    WHEN STR_TO_DATE(return_time, '%H:%i:%s') > STR_TO_DATE(
                        (SELECT config_value FROM fee_standard WHERE config_key = 'work_end_time'), 
                        '%H:%i:%s'
                    ) THEN
                        FLOOR(TIMESTAMPDIFF(MINUTE, 
                            STR_TO_DATE((SELECT config_value FROM fee_standard WHERE config_key = 'work_end_time'), '%H:%i:%s'), 
                            STR_TO_DATE(return_time, '%H:%i:%s')
                        ) / 60)
                    ELSE 0
                END
            ELSE 0
        END
)
WHERE departure_time IS NOT NULL AND return_time IS NOT NULL;

-- 2. 更新加班费（按照阶梯规则：<2小时0元，2-3小时40元，3-4小时60元，4-5小时80元，>=5小时100元）
UPDATE dispatch_record dr
SET overtime_amount = (
    CASE 
        WHEN overtime_hours < 2 THEN 0
        WHEN overtime_hours >= 2 AND overtime_hours < 3 THEN 40
        WHEN overtime_hours >= 3 AND overtime_hours < 4 THEN 60
        WHEN overtime_hours >= 4 AND overtime_hours < 5 THEN 80
        ELSE 100
    END
)
WHERE overtime_hours IS NOT NULL;

-- 3. 更新公里补贴（公里数 × 公里单价）
UPDATE dispatch_record dr
SET bridge_repair_fee = (
    total_mileage * (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'mileage_unit_price')
)
WHERE total_mileage IS NOT NULL;

-- 4. 更新值班补贴
UPDATE dispatch_record dr
SET duty_subsidy = (
    CASE 
        WHEN is_duty = 1 THEN (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'duty_subsidy_amount')
        ELSE 0
    END
);

-- 5. 更新本次合计金额（不包含公里补贴，因为公里补贴应该单独计算）
UPDATE dispatch_record dr
SET total_amount = COALESCE(overtime_amount, 0) + COALESCE(duty_subsidy, 0) + COALESCE(rest_day_wage, 0) + COALESCE(safety_bonus, 0);
