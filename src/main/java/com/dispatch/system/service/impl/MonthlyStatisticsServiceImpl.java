package com.dispatch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.system.entity.*;
import com.dispatch.system.mapper.*;
import com.dispatch.system.service.MonthlyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 月度统计服务实现类
 */
@Service
public class MonthlyStatisticsServiceImpl extends ServiceImpl<MonthlyStatisticsMapper, MonthlyStatistics> implements MonthlyStatisticsService {

    @Autowired
    private DispatchRecordMapper dispatchRecordMapper;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private FeeStandardMapper feeStandardMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateMonthlyStatistics(String yearMonth, Long driverId) {
        // 解析年月
        YearMonth ym = YearMonth.parse(yearMonth);
        int year = ym.getYear();
        int month = ym.getMonthValue();

        // 获取需要计算的驾驶员列表
        List<Driver> drivers;
        if (driverId != null) {
            Driver driver = driverMapper.selectById(driverId);
            drivers = driver != null ? List.of(driver) : List.of();
        } else {
            // 获取所有在职驾驶员
            LambdaQueryWrapper<Driver> driverWrapper = new LambdaQueryWrapper<>();
            driverWrapper.eq(Driver::getStatus, 1);
            drivers = driverMapper.selectList(driverWrapper);
        }

        // 获取费用标准
        FeeStandard mileageUnitPriceStandard = feeStandardMapper.findByConfigKey("mileage_unit_price");
        FeeStandard mileageMaxAmountStandard = feeStandardMapper.findByConfigKey("mileage_max_amount");
        FeeStandard safetyBonusStandard = feeStandardMapper.findByConfigKey("safety_bonus_standard");
        FeeStandard workDaysStandard = feeStandardMapper.findByConfigKey("monthly_working_days");

        BigDecimal mileageUnitPrice = mileageUnitPriceStandard != null ? 
                new BigDecimal(mileageUnitPriceStandard.getConfigValue()) : new BigDecimal("0.1");
        BigDecimal mileageMaxAmount = mileageMaxAmountStandard != null ? 
                new BigDecimal(mileageMaxAmountStandard.getConfigValue()) : new BigDecimal("500");
        BigDecimal safetyBonus = safetyBonusStandard != null ? 
                new BigDecimal(safetyBonusStandard.getConfigValue()) : new BigDecimal("200");
        int workDays = workDaysStandard != null ?
                Integer.parseInt(workDaysStandard.getConfigValue()) : 22;

        // 为每个驾驶员计算月度统计
        for (Driver driver : drivers) {
            // 查询该驾驶员本月的统计数据
            Map<String, Object> stats = dispatchRecordMapper.getMonthlyStatistics(driver.getId(), year, month);

            // 创建或更新月度统计记录
            LambdaQueryWrapper<MonthlyStatistics> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MonthlyStatistics::getYearMonth, yearMonth)
                    .eq(MonthlyStatistics::getDriverId, driver.getId());
            MonthlyStatistics existStats = getOne(queryWrapper);

            MonthlyStatistics monthlyStats = existStats != null ? existStats : new MonthlyStatistics();
            monthlyStats.setYearMonth(yearMonth);
            monthlyStats.setDriverId(driver.getId());

            // 设置统计数据
            if (stats != null) {
                BigDecimal totalMileage = (BigDecimal) stats.get("total_mileage");
                if (totalMileage == null) totalMileage = BigDecimal.ZERO;

                // 使用查询返回的公里补贴和实际金额
                BigDecimal mileageSubsidy = convertToBigDecimal(stats.get("mileage_subsidy"));
                if (mileageSubsidy == null || mileageSubsidy.compareTo(BigDecimal.ZERO) == 0) {
                    mileageSubsidy = totalMileage.multiply(mileageUnitPrice);
                }

                BigDecimal actualMileageAmount = convertToBigDecimal(stats.get("actual_mileage_amount"));
                if (actualMileageAmount == null || actualMileageAmount.compareTo(BigDecimal.ZERO) == 0) {
                    actualMileageAmount = mileageSubsidy.min(mileageMaxAmount);
                }

                monthlyStats.setTotalMileage(totalMileage);
                monthlyStats.setMileageUnitPrice(mileageUnitPrice);
                monthlyStats.setMileageSubsidy(mileageSubsidy);
                monthlyStats.setActualMileageAmount(actualMileageAmount);

                monthlyStats.setOvertimeAmount(convertToBigDecimal(stats.get("total_overtime_amount")));
                if (monthlyStats.getOvertimeAmount() == null) {
                    monthlyStats.setOvertimeAmount(BigDecimal.ZERO);
                }

                monthlyStats.setDutySubsidy(convertToBigDecimal(stats.get("total_duty_subsidy")));
                if (monthlyStats.getDutySubsidy() == null) {
                    monthlyStats.setDutySubsidy(BigDecimal.ZERO);
                }

                // 计算公休日工资（根据实际出勤天数与标准工作天数的差值计算）
                int actualWorkDays = 0;
                try {
                    // 查询实际出勤天数
                    actualWorkDays = dispatchRecordMapper.getActualWorkDays(driver.getId(), year, month);
                } catch (Exception e) {
                    actualWorkDays = 0;
                }
                
                BigDecimal restDayWageTotal = BigDecimal.ZERO;
                if (actualWorkDays == 0) {
                    // 没有出车记录，不扣除请假工资
                    restDayWageTotal = BigDecimal.ZERO;
                } else if (actualWorkDays > workDays) {
                    // 实际出勤天数 > 标准工作天数：发放公休日工资
                    int overtimeDays = actualWorkDays - workDays;
                    FeeStandard restDayWageStandard = feeStandardMapper.findByConfigKey("rest_day_wage");
                    BigDecimal restDayWage = restDayWageStandard != null ?
                            new BigDecimal(restDayWageStandard.getConfigValue()) : BigDecimal.ZERO;
                    restDayWageTotal = new BigDecimal(overtimeDays).multiply(restDayWage);
                } else if (actualWorkDays < workDays) {
                    // 实际出勤天数 < 标准工作天数：扣除请假工资
                    int leaveDays = workDays - actualWorkDays;
                    BigDecimal dailyWage = driver.getDailyWage() != null ?
                            driver.getDailyWage() : BigDecimal.ZERO;
                    restDayWageTotal = new BigDecimal(leaveDays).multiply(dailyWage).negate();
                }
                monthlyStats.setRestDayWageTotal(restDayWageTotal);

                // 计算安全奖（根据实际出勤天数计算）
                try {
                    // 查询实际出勤天数
                    actualWorkDays = dispatchRecordMapper.getActualWorkDays(driver.getId(), year, month);
                } catch (Exception e) {
                    actualWorkDays = 0;
                }
                
                // 计算实际安全奖（按实际出勤天数计算）
                BigDecimal calculatedSafetyBonus = new BigDecimal(actualWorkDays)
                        .multiply(safetyBonus)
                        .divide(new BigDecimal(workDays), 2, RoundingMode.HALF_UP);
                
                // 确保不超过安全奖标准
                if (calculatedSafetyBonus.compareTo(safetyBonus) > 0) {
                    calculatedSafetyBonus = safetyBonus;
                }
                monthlyStats.setSafetyBonusTotal(calculatedSafetyBonus);

                // 设置底薪
                BigDecimal baseSalary = driver.getBaseSalary() != null ?
                        driver.getBaseSalary() : BigDecimal.ZERO;
                monthlyStats.setBaseSalary(baseSalary);

                // 手动计算合计金额（包含公休日工资和底薪）
                BigDecimal totalAmount = actualMileageAmount
                        .add(monthlyStats.getOvertimeAmount())
                        .add(monthlyStats.getDutySubsidy())
                        .add(monthlyStats.getRestDayWageTotal())
                        .add(monthlyStats.getSafetyBonusTotal())
                        .add(monthlyStats.getBaseSalary());

                monthlyStats.setTotalAmount(totalAmount);
            } else {
                // 没有数据的情况
                monthlyStats.setTotalMileage(BigDecimal.ZERO);
                monthlyStats.setMileageUnitPrice(mileageUnitPrice);
                monthlyStats.setMileageSubsidy(BigDecimal.ZERO);
                monthlyStats.setActualMileageAmount(BigDecimal.ZERO);
                monthlyStats.setOvertimeAmount(BigDecimal.ZERO);
                monthlyStats.setDutySubsidy(BigDecimal.ZERO);
                monthlyStats.setLegalHolidayAmount(BigDecimal.ZERO);
                monthlyStats.setRestDayWageTotal(BigDecimal.ZERO);
                monthlyStats.setSafetyBonusTotal(BigDecimal.ZERO);
                monthlyStats.setBaseSalary(driver.getBaseSalary() != null ?
                        driver.getBaseSalary() : BigDecimal.ZERO);
                // 没有数据的情况，合计金额只包含底薪
                monthlyStats.setTotalAmount(monthlyStats.getBaseSalary());
            }

            // 保存或更新统计记录
            if (existStats != null) {
                updateById(monthlyStats);
            } else {
                monthlyStats.setStatus(1); // 草稿状态
                save(monthlyStats);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmStatistics(Long id, Long userId) {
        MonthlyStatistics stats = getById(id);
        if (stats == null) {
            throw new RuntimeException("统计记录不存在");
        }

        if (stats.getStatus() != 1) {
            throw new RuntimeException("只有草稿状态的统计才能确认");
        }

        stats.setStatus(2); // 已确认
        stats.setConfirmedBy(userId);
        stats.setConfirmedAt(LocalDateTime.now());
        updateById(stats);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditStatistics(Long id, Long userId) {
        MonthlyStatistics stats = getById(id);
        if (stats == null) {
            throw new RuntimeException("统计记录不存在");
        }

        if (stats.getStatus() != 2) {
            throw new RuntimeException("只有已确认状态的统计才能审核");
        }

        stats.setStatus(3); // 已审核
        stats.setAuditedBy(userId);
        stats.setAuditedAt(LocalDateTime.now());
        updateById(stats);
    }

    @Override
    public List<Map<String, Object>> getMonthlyStatisticsList(String yearMonth, Long driverId) {
        // 构建查询条件
        LambdaQueryWrapper<MonthlyStatistics> queryWrapper = new LambdaQueryWrapper<>();
        if (yearMonth != null) {
            queryWrapper.eq(MonthlyStatistics::getYearMonth, yearMonth);
        }
        if (driverId != null) {
            queryWrapper.eq(MonthlyStatistics::getDriverId, driverId);
        }

        // 查询统计记录
        List<MonthlyStatistics> statisticsList = list(queryWrapper);

        // 构建返回数据
        Map<Long, Driver> driverMap = new HashMap<>();
        for (MonthlyStatistics stats : statisticsList) {
            if (!driverMap.containsKey(stats.getDriverId())) {
                Driver driver = driverMapper.selectById(stats.getDriverId());
                driverMap.put(stats.getDriverId(), driver);
            }
        }

        // 转换为返回格式
        return statisticsList.stream().map(stats -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", stats.getId());
            map.put("yearMonth", stats.getYearMonth());
            map.put("driverId", stats.getDriverId());

            Driver driver = driverMap.get(stats.getDriverId());
            map.put("driverName", driver != null ? driver.getName() : "");

            map.put("totalMileage", stats.getTotalMileage());
            map.put("mileageUnitPrice", stats.getMileageUnitPrice());
            map.put("mileageSubsidy", stats.getMileageSubsidy());
            map.put("actualMileageAmount", stats.getActualMileageAmount());
            map.put("overtimeAmount", stats.getOvertimeAmount());
            map.put("dutySubsidy", stats.getDutySubsidy());
            map.put("legalHolidayAmount", stats.getLegalHolidayAmount());
            map.put("restDayWageTotal", stats.getRestDayWageTotal());
            map.put("safetyBonusTotal", stats.getSafetyBonusTotal());
            map.put("baseSalary", driver != null ? driver.getBaseSalary() : BigDecimal.ZERO);
            map.put("totalAmount", stats.getTotalAmount());
            map.put("status", stats.getStatus());
            map.put("confirmedBy", stats.getConfirmedBy());
            map.put("confirmedAt", stats.getConfirmedAt());
            map.put("auditedBy", stats.getAuditedBy());
            map.put("auditedAt", stats.getAuditedAt());

            return map;
        }).toList();
    }

    @Override
    public MonthlyStatistics getStatisticsById(Long id) {
        return getById(id);
    }

    /**
     * 将对象转换为 BigDecimal
     */
    private BigDecimal convertToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        if (value instanceof Double) {
            return new BigDecimal(value.toString());
        }
        if (value instanceof String) {
            return new BigDecimal((String) value);
        }
        return null;
    }
}
