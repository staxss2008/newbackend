package com.dispatch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.system.entity.FeeStandard;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 费用标准服务接口
 */
public interface FeeStandardService extends IService<FeeStandard> {

    /**
     * 获取所有费用标准
     */
    List<FeeStandard> getAllFeeStandards();

    /**
     * 根据分类获取费用标准
     */
    List<FeeStandard> getFeeStandardsByCategory(String category);

    /**
     * 根据配置键获取费用标准
     */
    FeeStandard getFeeStandardByKey(String configKey);

    /**
     * 更新费用标准
     */
    boolean updateFeeStandard(FeeStandard feeStandard);

    /**
     * 导入费用标准(CSV格式)
     */
    void importFeeStandardsFromCSV(String csvContent);

    /**
     * 获取费用标准配置Map
     */
    Map<String, Object> getFeeStandardConfig();

    /**
     * 初始化公休节假日工资标准
     */
    void initHolidayWageStandards();

    /**
     * 获取月工作总天数
     */
    Integer getMonthlyWorkingDays();

    /**
     * 根据每日工资计算并更新公休日和节假日工资标准
     * 公休日工资 = 每日工资
     * 节假日工资 = 每日工资 * 2
     */
    void calculateAndUpdateWageStandards(BigDecimal dailyWage);
}
