package com.dispatch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.system.entity.MonthlyStatistics;

import java.util.List;
import java.util.Map;

/**
 * 月度统计服务接口
 */
public interface MonthlyStatisticsService extends IService<MonthlyStatistics> {

    /**
     * 计算月度统计
     * @param yearMonth 年月(YYYY-MM)
     * @param driverId 驾驶员ID,为null时计算所有驾驶员
     */
    void calculateMonthlyStatistics(String yearMonth, Long driverId);

    /**
     * 确认月度统计
     * @param id 统计ID
     * @param userId 确认人ID
     */
    void confirmStatistics(Long id, Long userId);

    /**
     * 审核月度统计
     * @param id 统计ID
     * @param userId 审核人ID
     */
    void auditStatistics(Long id, Long userId);

    /**
     * 获取月度统计列表
     * @param yearMonth 年月
     * @param driverId 驾驶员ID(可选)
     */
    List<Map<String, Object>> getMonthlyStatisticsList(String yearMonth, Long driverId);

    /**
     * 获取月度统计详情
     * @param id 统计ID
     */
    MonthlyStatistics getStatisticsById(Long id);
}
