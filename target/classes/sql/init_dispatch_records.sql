-- 初始化派车记录数据
-- 注意：需要先执行init_drivers.sql和init_vehicles.sql

INSERT INTO dispatch_record (
    record_date, 
    driver_id, 
    vehicle_id, 
    departure_time, 
    return_time, 
    start_mileage, 
    end_mileage, 
    total_mileage, 
    bridge_repair_fee, 
    reason, 
    passengers, 
    created_at, 
    updated_at
) 
SELECT 
    '2026-01-01' AS record_date,
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1) AS driver_id,
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1) AS vehicle_id,
    '10:00:00' AS departure_time,
    '17:00:00' AS return_time,
    500363.00 AS start_mileage,
    500532.00 AS end_mileage,
    169.00 AS total_mileage,
    16.90 AS bridge_repair_fee,
    '唐海' AS reason,
    '付宏佳' AS passengers,
    NOW() AS created_at,
    NOW() AS updated_at
UNION ALL
SELECT 
    '2026-01-02',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀B3T63Y' LIMIT 1),
    '10:00:00',
    '15:30:00',
    209100.00,
    209325.00,
    225.00,
    22.50,
    '唐山 保养',
    '',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-03',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '21:00:00',
    500532.00,
    500815.00,
    283.00,
    28.30,
    '唐山 送客人',
    '',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-04',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '12:00:00',
    500815.00,
    500942.00,
    127.00,
    12.70,
    '唐海',
    '付宏佳',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-05',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '11:00:00',
    500942.00,
    501192.00,
    250.00,
    25.00,
    '唐山',
    '路宇博',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-06',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '14:00:00',
    '21:00:00',
    501192.00,
    501461.00,
    269.00,
    26.90,
    '唐山',
    '李来友',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-08',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '14:00:00',
    '19:20:00',
    501461.00,
    501544.00,
    83.00,
    8.30,
    '唐海',
    '付宏佳',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-09',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '11:00:00',
    501544.00,
    501783.00,
    239.00,
    23.90,
    '集团',
    '付宏佳',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-10',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '14:00:00',
    '21:00:00',
    501783.00,
    501816.00,
    33.00,
    3.30,
    '三加',
    '王敬轩',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-11',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '11:00:00',
    501816.00,
    502018.00,
    202.00,
    20.20,
    '集团 送工资',
    '',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-12',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '14:00:00',
    502018.00,
    502085.00,
    67.00,
    6.70,
    '大学城',
    '徐璐',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-13',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '21:00:00',
    502085.00,
    502286.00,
    201.00,
    20.10,
    '集团 送工资',
    '',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-15',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '10:00:00',
    '16:00:00',
    502286.00,
    502380.00,
    94.00,
    9.40,
    '唐海、大金',
    '王猛',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-16',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '10:00:00',
    '17:00:00',
    502380.00,
    502450.00,
    70.00,
    7.00,
    '三加',
    '王俊玲',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-17',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '13:00:00',
    '21:00:00',
    502450.00,
    502652.00,
    202.00,
    20.20,
    '集团',
    '魏部长',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-18',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '10:00:00',
    '15:00:00',
    502652.00,
    502717.00,
    65.00,
    6.50,
    '大学城、三加',
    '吴雪梅',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-19',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '09:00:00',
    '16:30:00',
    502717.00,
    502784.00,
    67.00,
    6.70,
    '三加',
    '梁佳',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-22',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '11:00:00',
    502800.00,
    502868.00,
    68.00,
    6.80,
    '三加',
    '邸天月',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-23',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '19:19:00',
    502868.00,
    503240.00,
    372.00,
    37.20,
    '富安 盘点',
    '',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-24',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BE08Y9' LIMIT 1),
    '08:00:00',
    '21:40:00',
    338573.00,
    339103.00,
    530.00,
    53.00,
    '营口',
    '魏部长',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-25',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BE08Y9' LIMIT 1),
    '08:00:00',
    '21:00:00',
    339103.00,
    339203.00,
    100.00,
    10.00,
    '营口',
    '魏部长',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-26',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BE08Y9' LIMIT 1),
    '08:00:00',
    '15:00:00',
    339203.00,
    339740.00,
    537.00,
    53.70,
    '营口',
    '魏部长',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-27',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '19:05:00',
    503522.00,
    503749.00,
    227.00,
    22.70,
    '集团、唐海 送资料',
    '',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-29',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '22:20:00',
    503749.00,
    503902.00,
    153.00,
    15.30,
    '唐海、建筑',
    '付宏佳',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-30',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '13:30:00',
    '19:30:00',
    503902.00,
    504074.00,
    172.00,
    17.20,
    '三加、建筑 送资料',
    '',
    NOW(),
    NOW()
UNION ALL
SELECT 
    '2026-01-31',
    (SELECT id FROM driver WHERE name = '薛涛' LIMIT 1),
    (SELECT id FROM vehicle WHERE plate_number = '冀BK47X5' LIMIT 1),
    '08:00:00',
    '11:00:00',
    504074.00,
    504307.00,
    233.00,
    23.30,
    '唐山',
    '郑部长',
    NOW(),
    NOW();
