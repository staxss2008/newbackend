package com.dispatch.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dispatch.system.entity.DispatchRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 派车记录Mapper接口
 */
@Mapper
public interface DispatchRecordMapper extends BaseMapper<DispatchRecord> {

    /**
     * 查询驾驶员月度详细记录
     */
    @Select("SELECT " +
            "dr.id, dr.record_date as recordDate, dr.driver_id as driverId, dr.vehicle_id as vehicleId, " +
            "dr.use_unit as useUnit, dr.passengers, dr.reason, dr.departure_place as departurePlace, " +
            "dr.leader, dr.approver, " +
            "IFNULL(TIME_FORMAT(dr.departure_time, '%H:%i'), '') as departureTime, " +
            "IFNULL(TIME_FORMAT(dr.return_time, '%H:%i'), '') as returnTime, " +
            "IFNULL(dr.start_mileage, 0) as startMileage, " +
            "IFNULL(dr.end_mileage, 0) as endMileage, " +
            "IFNULL(dr.total_mileage, 0) as totalMileage, " +
            "IFNULL(dr.fuel_fee, 0) as fuelFee, " +
            "IFNULL(dr.bridge_repair_fee, 0) as bridgeRepairFee, " +
            "IFNULL(dr.overtime_hours, 0) as overtimeHours, " +
            "IFNULL(dr.overtime_type, 0) as overtimeType, " +
            "IFNULL(dr.overtime_amount, 0) as overtimeAmount, " +
            "IFNULL(dr.is_duty, 0) as isDuty, " +
            "IFNULL(dr.duty_subsidy, 0) as dutySubsidy, " +
            "IFNULL(dr.safety_bonus, 0) as safetyBonus, " +
            "IFNULL(dr.rest_day_wage, 0) as restDayWage, " +
            "IFNULL(dr.total_amount, 0) as totalAmount, " +
            "IFNULL(dr.remark, '') as remark, " +
            "IFNULL(d.name, '') as driverName, " +
            "IFNULL(v.plate_number, '') as plateNumber, " +
            "CASE " +
            "  WHEN dr.departure_time IS NOT NULL AND dr.departure_time < " +
            "    STR_TO_DATE((SELECT config_value FROM fee_standard WHERE config_key = 'work_start_time'), '%H:%i') THEN " +
            "    FLOOR(TIMESTAMPDIFF(MINUTE, " +
            "      CONCAT(dr.record_date, ' ', dr.departure_time), " +
            "      CONCAT(dr.record_date, ' ', (SELECT config_value FROM fee_standard WHERE config_key = 'work_start_time'))) / 60) " +
            "  ELSE 0 " +
            "END as earlyOvertimeHours, " +
            "CASE " +
            "  WHEN dr.return_time IS NOT NULL AND dr.return_time > " +
            "    STR_TO_DATE((SELECT config_value FROM fee_standard WHERE config_key = 'work_end_time'), '%H:%i') THEN " +
            "    FLOOR(TIMESTAMPDIFF(MINUTE, " +
            "      CONCAT(dr.record_date, ' ', (SELECT config_value FROM fee_standard WHERE config_key = 'work_end_time')), " +
            "      CONCAT(dr.record_date, ' ', dr.return_time)) / 60) " +
            "  ELSE 0 " +
            "END as lateOvertimeHours, " +
            "CASE " +
            "  WHEN EXISTS (SELECT 1 FROM holiday_config WHERE holiday_date = dr.record_date) THEN " +
            "    (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'holiday_wage') " +
            "  ELSE 0 " +
            "END as holidayWage " +
            "FROM dispatch_record dr " +
            "LEFT JOIN driver d ON dr.driver_id = d.id " +
            "LEFT JOIN vehicle v ON dr.vehicle_id = v.id " +
            "WHERE dr.driver_id = #{driverId} " +
            "AND YEAR(dr.record_date) = #{year} " +
            "AND MONTH(dr.record_date) = #{month} " +
            "ORDER BY dr.record_date DESC, dr.departure_time DESC")
    List<Map<String, Object>> getMonthlyDetailByDriver(@Param("driverId") Long driverId,
                                                         @Param("year") Integer year,
                                                         @Param("month") Integer month);

    /**
     * 统计驾驶员月度数据
     */
    @Select("SELECT " +
            "COUNT(*) as total_count, " +
            "IFNULL(SUM(total_mileage), 0) as total_mileage, " +
            "IFNULL(SUM(total_mileage), 0) * (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'mileage_unit_price') as mileage_subsidy, " +
            "LEAST(IFNULL(SUM(total_mileage), 0) * (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'mileage_unit_price'), " +
            "     (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'mileage_max_amount')) as actual_mileage_amount, " +
            "IFNULL(SUM(overtime_amount), 0) as total_overtime_amount, " +
            "IFNULL(SUM(duty_subsidy), 0) as total_duty_subsidy, " +
            "CASE " +
            "  WHEN (SELECT COUNT(DISTINCT dr.record_date) FROM dispatch_record dr WHERE dr.driver_id = #{driverId} " +
            "      AND YEAR(dr.record_date) = #{year} AND MONTH(dr.record_date) = #{month}) > " +
            "      (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'monthly_working_days') " +
            "  THEN ((SELECT COUNT(DISTINCT dr.record_date) FROM dispatch_record dr WHERE dr.driver_id = #{driverId} " +
            "      AND YEAR(dr.record_date) = #{year} AND MONTH(dr.record_date) = #{month}) - " +
            "      (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'monthly_working_days')) * " +
            "      (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'rest_day_wage') " +
            "  WHEN (SELECT COUNT(DISTINCT dr.record_date) FROM dispatch_record dr WHERE dr.driver_id = #{driverId} " +
            "      AND YEAR(dr.record_date) = #{year} AND MONTH(dr.record_date) = #{month}) < " +
            "      (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'monthly_working_days') " +
            "  THEN -((SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'monthly_working_days') - " +
            "      (SELECT COUNT(DISTINCT dr.record_date) FROM dispatch_record dr WHERE dr.driver_id = #{driverId} " +
            "      AND YEAR(dr.record_date) = #{year} AND MONTH(dr.record_date) = #{month})) * " +
            "      (SELECT d.daily_wage FROM driver d WHERE d.id = #{driverId}) " +
            "  ELSE 0 " +
            "END as total_rest_day_wage, " +
            "IFNULL(SUM(safety_bonus), 0) as total_safety_bonus, " +
            "LEAST(IFNULL(SUM(total_mileage), 0) * (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'mileage_unit_price'), " +
            "     (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'mileage_max_amount')) + " +
            "IFNULL(SUM(overtime_amount), 0) + " +
            "IFNULL(SUM(duty_subsidy), 0) + " +
            "LEAST(" +
            "    (SELECT COUNT(DISTINCT dr.record_date) FROM dispatch_record dr WHERE dr.driver_id = #{driverId} " +
            "        AND YEAR(dr.record_date) = #{year} AND MONTH(dr.record_date) = #{month}) * " +
            "        (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'safety_bonus_standard') / " +
            "        (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'monthly_working_days'), " +
            "    (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'safety_bonus_standard')) + " +
            "(SELECT SUM(CASE WHEN EXISTS (SELECT 1 FROM holiday_config WHERE holiday_date = dr.record_date) " +
            "    THEN (SELECT CAST(config_value AS DECIMAL(10,2)) FROM fee_standard WHERE config_key = 'holiday_wage') " +
            "    ELSE 0 END) FROM dispatch_record dr WHERE dr.driver_id = #{driverId} " +
            "    AND YEAR(dr.record_date) = #{year} AND MONTH(dr.record_date) = #{month}) as total_amount " +
            "FROM dispatch_record " +
            "WHERE driver_id = #{driverId} " +
            "AND YEAR(record_date) = #{year} " +
            "AND MONTH(record_date) = #{month}")
    Map<String, Object> getMonthlyStatistics(@Param("driverId") Long driverId,
                                             @Param("year") Integer year,
                                             @Param("month") Integer month);

    /**
     * 获取车辆上一次的收车公里数
     */
    @Select("SELECT end_mileage " +
            "FROM dispatch_record " +
            "WHERE vehicle_id = #{vehicleId} " +
            "ORDER BY record_date DESC, return_time DESC " +
            "LIMIT 1")
    BigDecimal getLastEndMileage(@Param("vehicleId") Long vehicleId);

    /**
     * 获取驾驶员实际出勤天数
     */
    @Select("SELECT COUNT(DISTINCT record_date) " +
            "FROM dispatch_record " +
            "WHERE driver_id = #{driverId} " +
            "AND YEAR(record_date) = #{year} " +
            "AND MONTH(record_date) = #{month}")
    int getActualWorkDays(@Param("driverId") Long driverId, 
                        @Param("year") Integer year, 
                        @Param("month") Integer month);
}
