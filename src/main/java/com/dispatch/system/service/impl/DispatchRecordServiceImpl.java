package com.dispatch.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.system.cache.FeeStandardCache;
import com.dispatch.system.constant.CalculationConstants;
import com.dispatch.system.entity.DispatchRecord;
import com.dispatch.system.exception.OvertimeCalculationException;
import com.dispatch.system.mapper.DispatchRecordMapper;
import com.dispatch.system.service.DispatchRecordService;
import com.dispatch.system.service.MonthlyStatisticsService;
import com.dispatch.system.util.OvertimeCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * 派车记录服务实现类
 */
@Service
public class DispatchRecordServiceImpl extends ServiceImpl<DispatchRecordMapper, DispatchRecord> implements DispatchRecordService {

    private static final Logger log = LoggerFactory.getLogger(DispatchRecordServiceImpl.class);

    @Autowired
    private FeeStandardCache feeStandardCache;

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
        log.info("getMonthlyStatistics - 调用 Mapper: driverId={}, year={}, month={}", driverId, year, month);
        Map<String, Object> result = baseMapper.getMonthlyStatistics(driverId, year, month);
        log.info("getMonthlyStatistics - Mapper 返回结果: {}", result);
        return result;
    }

    @Override
    public void calculateRecordAmount(DispatchRecord record) {
        // 初始化总金额为0
        BigDecimal totalAmount = CalculationConstants.ZERO;

        // 计算行驶里程
        if (record.getStartMileage() != null && record.getEndMileage() != null) {
            BigDecimal totalMileage = record.getEndMileage().subtract(record.getStartMileage());
            record.setTotalMileage(totalMileage);

            // 计算公里数费用（公里补贴）
            String mileageUnitPriceStr = feeStandardCache.getValue("mileage_unit_price");
            if (mileageUnitPriceStr != null) {
                BigDecimal unitPrice = new BigDecimal(mileageUnitPriceStr);
                BigDecimal mileageAmount = totalMileage.multiply(unitPrice)
                        .setScale(CalculationConstants.SCALE, CalculationConstants.ROUNDING_MODE);
                // 将公里补贴存储到bridge_repair_fee字段
                record.setBridgeRepairFee(mileageAmount);
                // 公里补贴不加入totalAmount，因为公里补贴应该单独计算
            }
        }

        // 计算值班补贴
        if (record.getIsDuty() != null && record.getIsDuty() == 1) {
            String dutySubsidyStr = feeStandardCache.getValue("duty_subsidy_amount");
            if (dutySubsidyStr != null) {
                BigDecimal dutySubsidy = new BigDecimal(dutySubsidyStr);
                record.setDutySubsidy(dutySubsidy);
                totalAmount = totalAmount.add(dutySubsidy);
            }
        } else {
            record.setDutySubsidy(CalculationConstants.ZERO);
        }

        // 根据出车时间和收车时间判断是否属于加班
        calculateOvertimeHours(record);

        // 计算加班费
        // 加班费分为两部分：上班前加班和下班后加班，每部分最高100元，每天最高200元
        // 加班费按照费用标准的阶梯规则执行：<2小时0元，2-3小时40元，3-4小时60元，4-5小时80元，>=5小时100元
        if (record.getDepartureTime() != null && record.getReturnTime() != null) {
            // 获取正常工作时间
            String startTimeStr = feeStandardCache.getValue("work_start_time");
            String endTimeStr = feeStandardCache.getValue("work_end_time");

            if (startTimeStr != null && endTimeStr != null) {
                try {
                    LocalTime departureTime = record.getDepartureTime();
                    LocalTime returnTime = record.getReturnTime();
                    LocalTime workStartTime = LocalTime.parse(startTimeStr);
                    LocalTime workEndTime = LocalTime.parse(endTimeStr);

                    // 计算上班前加班小时数
                    BigDecimal earlyOvertimeHours = OvertimeCalculator.calculateEarlyOvertimeHours(
                            departureTime, workStartTime);

                    // 计算下班后加班小时数
                    BigDecimal lateOvertimeHours = OvertimeCalculator.calculateLateOvertimeHours(
                            returnTime, workEndTime);

                    // 计算上班前加班费：按照阶梯规则，最高100元
                    BigDecimal earlyOvertimeAmount = OvertimeCalculator.calculateOvertimeFeeByHours(earlyOvertimeHours);

                    // 计算下班后加班费：按照阶梯规则，最高100元
                    BigDecimal lateOvertimeAmount = OvertimeCalculator.calculateOvertimeFeeByHours(lateOvertimeHours);

                    // 总加班费 = 上班前加班费 + 下班后加班费
                    BigDecimal totalOvertimeAmount = earlyOvertimeAmount.add(lateOvertimeAmount);
                    record.setOvertimeAmount(totalOvertimeAmount);
                    totalAmount = totalAmount.add(totalOvertimeAmount);

                } catch (OvertimeCalculationException e) {
                    log.error("计算加班费失败: recordId={}", record.getId(), e);
                    record.setOvertimeAmount(CalculationConstants.ZERO);
                } catch (Exception e) {
                    log.error("解析工作时间失败: recordId={}", record.getId(), e);
                    record.setOvertimeAmount(CalculationConstants.ZERO);
                }
            }
        }

        // 计算公休日工资
        // 前端已移除加班类型字段，公休日工资设置为0
        record.setRestDayWage(CalculationConstants.ZERO);

        // 计算安全奖
        // 安全奖通常在月度统计时统一计算,这里可以设置为0
        record.setSafetyBonus(CalculationConstants.ZERO);

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
        String startTimeStr = feeStandardCache.getValue("work_start_time");
        String endTimeStr = feeStandardCache.getValue("work_end_time");

        if (startTimeStr == null || endTimeStr == null) {
            return;
        }

        try {
            // 获取出车时间和收车时间
            LocalTime departureTime = record.getDepartureTime();
            LocalTime returnTime = record.getReturnTime();

            // 解析工作时间配置
            LocalTime workStartTime = LocalTime.parse(startTimeStr);
            LocalTime workEndTime = LocalTime.parse(endTimeStr);

            // 计算上班前加班小时数
            BigDecimal earlyOvertimeHours = OvertimeCalculator.calculateEarlyOvertimeHours(
                    departureTime, workStartTime);

            // 计算下班后加班小时数
            BigDecimal lateOvertimeHours = OvertimeCalculator.calculateLateOvertimeHours(
                    returnTime, workEndTime);

            // 设置总加班小时数（用于显示）
            BigDecimal totalOvertimeHours = earlyOvertimeHours.add(lateOvertimeHours);
            record.setOvertimeHours(totalOvertimeHours);

            // 如果有加班时间，设置加班类型为平时加班
            if (totalOvertimeHours.compareTo(CalculationConstants.ZERO) > 0) {
                if (record.getOvertimeType() == null) {
                    record.setOvertimeType(0); // 默认为平时加班
                }
            }
        } catch (OvertimeCalculationException e) {
            log.error("计算加班小时数失败: recordId={}", record.getId(), e);
        } catch (Exception e) {
            log.error("解析工作时间失败: recordId={}", record.getId(), e);
        }
    }

    /**
     * 根据加班小时数计算加班费
     * 按照阶梯规则：<2小时0元，2-3小时40元，3-4小时60元，4-5小时80元，>=5小时100元
     * @param hours 加班小时数（向上取整）
     * @return 加班费
     * @deprecated 使用 OvertimeCalculator.calculateOvertimeFeeByHours 替代
     */
    @Deprecated
    private BigDecimal calculateOvertimeFeeByHours(BigDecimal hours) {
        return OvertimeCalculator.calculateOvertimeFeeByHours(hours);
    }
}
