package com.dispatch.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.system.entity.DispatchRecord;
import com.dispatch.system.entity.FeeStandard;
import com.dispatch.system.mapper.DispatchRecordMapper;
import com.dispatch.system.mapper.FeeStandardMapper;
import com.dispatch.system.service.DispatchRecordService;
import com.dispatch.system.service.MonthlyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * 派车记录服务实现类
 */
@Service
public class DispatchRecordServiceImpl extends ServiceImpl<DispatchRecordMapper, DispatchRecord> implements DispatchRecordService {

    @Autowired
    private FeeStandardMapper feeStandardMapper;

    @Autowired
    private MonthlyStatisticsService monthlyStatisticsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDispatchRecord(DispatchRecord record) {
        // 计算各项费用
        calculateRecordAmount(record);
        return save(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDispatchRecord(DispatchRecord record) {
        // 重新计算各项费用
        calculateRecordAmount(record);
        return updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDispatchRecord(Long id) {
        return removeById(id);
    }

    @Override
    public DispatchRecord getDispatchRecordById(Long id) {
        return getById(id);
    }

    @Override
    public List<Map<String, Object>> getMonthlyDetailByDriver(Long driverId, Integer year, Integer month) {
        return baseMapper.getMonthlyDetailByDriver(driverId, year, month);
    }

    @Override
    public Map<String, Object> getMonthlyStatistics(Long driverId, Integer year, Integer month) {
        return baseMapper.getMonthlyStatistics(driverId, year, month);
    }

    @Override
    public void calculateRecordAmount(DispatchRecord record) {
        // 初始化总金额为0
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 计算行驶里程
        if (record.getStartMileage() != null && record.getEndMileage() != null) {
            BigDecimal totalMileage = record.getEndMileage().subtract(record.getStartMileage());
            record.setTotalMileage(totalMileage);

            // 计算公里数费用（公里补贴）
            FeeStandard mileageUnitPrice = feeStandardMapper.findByConfigKey("mileage_unit_price");
            if (mileageUnitPrice != null) {
                BigDecimal unitPrice = new BigDecimal(mileageUnitPrice.getConfigValue());
                BigDecimal mileageAmount = totalMileage.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
                // 将公里补贴存储到bridge_repair_fee字段
                record.setBridgeRepairFee(mileageAmount);
                // 公里补贴不加入totalAmount，因为公里补贴应该单独计算
            }
        }

        // 计算值班补贴
        if (record.getIsDuty() != null && record.getIsDuty() == 1) {
            FeeStandard dutyStandard = feeStandardMapper.findByConfigKey("duty_subsidy_amount");
            if (dutyStandard != null) {
                BigDecimal dutySubsidy = new BigDecimal(dutyStandard.getConfigValue());
                record.setDutySubsidy(dutySubsidy);
                totalAmount = totalAmount.add(dutySubsidy);
            }
        } else {
            record.setDutySubsidy(BigDecimal.ZERO);
        }

        // 根据出车时间和收车时间判断是否属于加班
        calculateOvertimeHours(record);

        // 计算加班费
        // 加班费分为两部分：上班前加班和下班后加班，每部分最高100元，每天最高200元
        // 加班费按照费用标准的阶梯规则执行：<2小时0元，2-3小时40元，3-4小时60元，4-5小时80元，>=5小时100元
        if (record.getDepartureTime() != null && record.getReturnTime() != null) {
            // 获取正常工作时间
            FeeStandard startTimeStandard = feeStandardMapper.findByConfigKey("work_start_time");
            FeeStandard endTimeStandard = feeStandardMapper.findByConfigKey("work_end_time");

            if (startTimeStandard != null && endTimeStandard != null) {
                try {
                    java.time.LocalTime departureTime = record.getDepartureTime();
                    java.time.LocalTime returnTime = record.getReturnTime();
                    java.time.LocalTime workStartTime = java.time.LocalTime.parse(startTimeStandard.getConfigValue());
                    java.time.LocalTime workEndTime = java.time.LocalTime.parse(endTimeStandard.getConfigValue());

                    // 计算上班前加班小时数（向下取整到小时）
                    BigDecimal earlyOvertimeHours = BigDecimal.ZERO;
                    if (departureTime.isBefore(workStartTime)) {
                        long earlyMinutes = java.time.Duration.between(departureTime, workStartTime).toMinutes();
                        // 向下取整到小时
                        earlyOvertimeHours = new BigDecimal(earlyMinutes / 60);
                    }

                    // 计算下班后加班小时数（向下取整到小时）
                    BigDecimal lateOvertimeHours = BigDecimal.ZERO;
                    if (returnTime.isAfter(workEndTime)) {
                        long lateMinutes = java.time.Duration.between(workEndTime, returnTime).toMinutes();
                        // 向下取整到小时
                        lateOvertimeHours = new BigDecimal(lateMinutes / 60);
                    }

                    // 计算上班前加班费：按照阶梯规则，最高100元
                    BigDecimal earlyOvertimeAmount = calculateOvertimeFeeByHours(earlyOvertimeHours);

                    // 计算下班后加班费：按照阶梯规则，最高100元
                    BigDecimal lateOvertimeAmount = calculateOvertimeFeeByHours(lateOvertimeHours);

                    // 总加班费 = 上班前加班费 + 下班后加班费
                    BigDecimal totalOvertimeAmount = earlyOvertimeAmount.add(lateOvertimeAmount);
                    record.setOvertimeAmount(totalOvertimeAmount);
                    totalAmount = totalAmount.add(totalOvertimeAmount);

                } catch (Exception e) {
                    System.err.println("计算加班费失败: " + e.getMessage());
                    e.printStackTrace();
                    record.setOvertimeAmount(BigDecimal.ZERO);
                }
            }
        }

        // 计算公休日工资
        // 前端已移除加班类型字段，公休日工资设置为0
        record.setRestDayWage(BigDecimal.ZERO);

        // 计算安全奖
        // 安全奖通常在月度统计时统一计算,这里可以设置为0
        record.setSafetyBonus(BigDecimal.ZERO);

        // 将安全奖加入totalAmount（虽然当前为0，但保持计算逻辑一致）
        totalAmount = totalAmount.add(record.getSafetyBonus());

        // 设置本次合计金额
        record.setTotalAmount(totalAmount);
    }

    @Override
    public boolean isDuty(String reason) {
        // 判断用车事由是否包含"值班"关键词
        return StrUtil.isNotBlank(reason) && reason.contains("值班");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recalculateAllAmounts() {
        // 查询所有派车记录
        List<DispatchRecord> records = list();

        // 重新计算每条记录的金额
        for (DispatchRecord record : records) {
            calculateRecordAmount(record);
            updateById(record);
        }

        // 重新计算月度统计
        monthlyStatisticsService.calculateMonthlyStatistics(
            YearMonth.now().toString(),
            null
        );
    }

    @Override
    public BigDecimal getLastEndMileage(Long vehicleId) {
        return baseMapper.getLastEndMileage(vehicleId);
    }

    /**
     * 根据出车时间和收车时间计算加班小时数
     * 如果出车时间早于上班时间或收车时间晚于下班时间，则计算加班小时数
     */
    private void calculateOvertimeHours(DispatchRecord record) {
        if (record.getDepartureTime() == null || record.getReturnTime() == null) {
            return;
        }

        // 获取正常工作时间
        FeeStandard startTimeStandard = feeStandardMapper.findByConfigKey("work_start_time");
        FeeStandard endTimeStandard = feeStandardMapper.findByConfigKey("work_end_time");

        if (startTimeStandard == null || endTimeStandard == null) {
            return;
        }

        String startTimeStr = startTimeStandard.getConfigValue();
        String endTimeStr = endTimeStandard.getConfigValue();

        try {
            // 获取出车时间和收车时间
            java.time.LocalTime departureTime = record.getDepartureTime();
            java.time.LocalTime returnTime = record.getReturnTime();

            // 解析工作时间配置
            java.time.LocalTime workStartTime = java.time.LocalTime.parse(startTimeStr);
            java.time.LocalTime workEndTime = java.time.LocalTime.parse(endTimeStr);

            if (departureTime == null || returnTime == null) {
                return;
            }

            BigDecimal earlyOvertimeHours = BigDecimal.ZERO;  // 上班前加班小时数
            BigDecimal lateOvertimeHours = BigDecimal.ZERO;   // 下班后加班小时数

            // 如果出车时间早于上班时间，计算早到加班时间
            if (departureTime.isBefore(workStartTime)) {
                long earlyMinutes = java.time.Duration.between(departureTime, workStartTime).toMinutes();
                earlyOvertimeHours = new BigDecimal(earlyMinutes).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
            }

            // 如果收车时间晚于下班时间，计算晚退加班时间
            if (returnTime.isAfter(workEndTime)) {
                long lateMinutes = java.time.Duration.between(workEndTime, returnTime).toMinutes();
                lateOvertimeHours = new BigDecimal(lateMinutes).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
            }

            // 设置总加班小时数（用于显示）
            BigDecimal totalOvertimeHours = earlyOvertimeHours.add(lateOvertimeHours);
            record.setOvertimeHours(totalOvertimeHours);

            // 如果有加班时间，设置加班类型为平时加班
            if (totalOvertimeHours.compareTo(BigDecimal.ZERO) > 0) {
                if (record.getOvertimeType() == null) {
                    record.setOvertimeType(0); // 默认为平时加班
                }
            }
        } catch (Exception e) {
            // 时间解析失败，不计算加班时间
            System.err.println("解析工作时间失败: " + e.getMessage());
        }
    }

    /**
     * 根据加班小时数计算加班费
     * 按照阶梯规则：<2小时0元，2-3小时40元，3-4小时60元，4-5小时80元，>=5小时100元
     * @param hours 加班小时数（向上取整）
     * @return 加班费
     */
    private BigDecimal calculateOvertimeFeeByHours(BigDecimal hours) {
        if (hours == null || hours.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // 向上取整到整数小时
        int hoursInt = hours.setScale(0, RoundingMode.UP).intValue();

        // 按照阶梯规则计算加班费
        if (hoursInt < 2) {
            return BigDecimal.ZERO;
        } else if (hoursInt >= 2 && hoursInt < 3) {
            return new BigDecimal("40");
        } else if (hoursInt >= 3 && hoursInt < 4) {
            return new BigDecimal("60");
        } else if (hoursInt >= 4 && hoursInt < 5) {
            return new BigDecimal("80");
        } else {
            // >= 5小时
            return new BigDecimal("100");
        }
    }
}
