-- 初始化车辆数据
INSERT INTO vehicle (plate_number, vehicle_type, brand, model, current_mileage, status, created_at, updated_at) VALUES
('冀BK47X5', '轿车', '大众', '帕萨特', 504307.00, 1, NOW(), NOW()),
('冀B3T63Y', '轿车', '大众', '帕萨特', 209325.00, 1, NOW(), NOW()),
('冀BE08Y9', '轿车', '大众', '帕萨特', 339740.00, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    current_mileage = VALUES(current_mileage),
    updated_at = NOW();
