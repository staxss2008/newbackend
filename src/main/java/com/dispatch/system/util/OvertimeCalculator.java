package com.dispatch.system.util;

import com.dispatch.system.constant.CalculationConstants;
import com.dispatch.system.exception.OvertimeCalculationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

/**
 * 加班计算工具类
 * 提供加班小时数和加班费的计算功能
 */
public class OvertimeCalculator {

    private static final Logger log = LoggerFactory.getLogger(OvertimeCalculator.class);

    /**
     * 计算上班前加班小时数
     * @param departureTime 出车时间
     * @param workStartTime 上班时间
     * @return 加班小时数（向下取整）
     */
    public static BigDecimal calculateEarlyOvertimeHours(LocalTime departureTime, LocalTime workStartTime) {
        if (departureTime == null || workStartTime == null) {
            return CalculationConstants.ZERO;
        }

        if (!departureTime.isBefore(workStartTime)) {
            return CalculationConstants.ZERO;
        }

        try {
            long earlyMinutes = Duration.between(departureTime, workStartTime).toMinutes();
            return new BigDecimal(earlyMinutes)
                    .divide(CalculationConstants.SIXTY, CalculationConstants.SCALE, CalculationConstants.ROUNDING_MODE);
        } catch (Exception e) {
            log.error("计算上班前加班时间失败: departureTime={}, workStartTime={}", departureTime, workStartTime, e);
            throw new OvertimeCalculationException("计算上班前加班时间失败", e);
        }
    }

    /**
     * 计算下班后加班小时数
     * @param returnTime 收车时间
     * @param workEndTime 下班时间
     * @return 加班小时数（向下取整）
     */
    public static BigDecimal calculateLateOvertimeHours(LocalTime returnTime, LocalTime workEndTime) {
        if (returnTime == null || workEndTime == null) {
            return CalculationConstants.ZERO;
        }

        if (!returnTime.isAfter(workEndTime)) {
            return CalculationConstants.ZERO;
        }

        try {
            long lateMinutes = Duration.between(workEndTime, returnTime).toMinutes();
            return new BigDecimal(lateMinutes)
                    .divide(CalculationConstants.SIXTY, CalculationConstants.SCALE, CalculationConstants.ROUNDING_MODE);
        } catch (Exception e) {
            log.error("计算下班后加班时间失败: returnTime={}, workEndTime={}", returnTime, workEndTime, e);
            throw new OvertimeCalculationException("计算下班后加班时间失败", e);
        }
    }

    /**
     * 根据加班小时数计算加班费
     * 按照阶梯规则：<2小时0元，2-3小时40元，3-4小时60元，4-5小时80元，>=5小时100元
     * @param hours 加班小时数（向上取整）
     * @return 加班费
     */
    public static BigDecimal calculateOvertimeFeeByHours(BigDecimal hours) {
        if (hours == null || hours.compareTo(CalculationConstants.ZERO) <= 0) {
            return CalculationConstants.ZERO;
        }

        // 向上取整到整数小时
        int hoursInt = hours.setScale(0, CalculationConstants.ROUNDING_MODE).intValue();

        // 按照阶梯规则计算加班费
        if (hoursInt < 2) {
            return CalculationConstants.ZERO;
        } else if (hoursInt >= 2 && hoursInt < 3) {
            return CalculationConstants.OVERTIME_FEE_2_3_HOURS;
        } else if (hoursInt >= 3 && hoursInt < 4) {
            return CalculationConstants.OVERTIME_FEE_3_4_HOURS;
        } else if (hoursInt >= 4 && hoursInt < 5) {
            return CalculationConstants.OVERTIME_FEE_4_5_HOURS;
        } else {
            // >= 5小时
            return CalculationConstants.OVERTIME_FEE_5_PLUS_HOURS;
        }
    }
}
