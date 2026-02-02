package com.dispatch.system.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算常量类
 * 定义计算过程中常用的常量
 */
public class CalculationConstants {

    private CalculationConstants() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }

    /**
     * 数值常量
     */
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TWO = new BigDecimal("2");
    public static final BigDecimal THREE = new BigDecimal("3");
    public static final BigDecimal FOUR = new BigDecimal("4");
    public static final BigDecimal FIVE = new BigDecimal("5");
    public static final BigDecimal HUNDRED = new BigDecimal("100");
    public static final BigDecimal SIXTY = new BigDecimal("60");

    /**
     * 加班费阶梯常量
     */
    public static final BigDecimal OVERTIME_FEE_2_3_HOURS = new BigDecimal("40");
    public static final BigDecimal OVERTIME_FEE_3_4_HOURS = new BigDecimal("60");
    public static final BigDecimal OVERTIME_FEE_4_5_HOURS = new BigDecimal("80");
    public static final BigDecimal OVERTIME_FEE_5_PLUS_HOURS = new BigDecimal("100");

    /**
     * 计算精度常量
     */
    public static final int SCALE = 2;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * 时间常量（分钟）
     */
    public static final int MINUTES_PER_HOUR = 60;

    /**
     * 角色常量
     */
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_DRIVER = 2;
}
