package com.dispatch.system.controller;

import com.dispatch.common.Result;
import com.dispatch.system.entity.MonthlyStatistics;
import com.dispatch.system.service.MonthlyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 月度统计控制器
 */
@RestController
@RequestMapping("/statistics")
public class MonthlyStatisticsController {

    @Autowired
    private MonthlyStatisticsService monthlyStatisticsService;

    /**
     * 计算月度统计
     */
    @PostMapping("/calculate")
    public Result<String> calculateMonthlyStatistics(@RequestBody Map<String, Object> params) {
        String yearMonth = (String) params.get("yearMonth");
        Long driverId = params.get("driverId") != null ? 
                Long.valueOf(params.get("driverId").toString()) : null;

        monthlyStatisticsService.calculateMonthlyStatistics(yearMonth, driverId);
        return Result.success("计算成功");
    }

    /**
     * 确认月度统计
     */
    @PostMapping("/confirm")
    public Result<String> confirmStatistics(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Long userId = Long.valueOf(params.get("userId").toString());

        monthlyStatisticsService.confirmStatistics(id, userId);
        return Result.success("确认成功");
    }

    /**
     * 审核月度统计
     */
    @PostMapping("/audit")
    public Result<String> auditStatistics(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Long userId = Long.valueOf(params.get("userId").toString());

        monthlyStatisticsService.auditStatistics(id, userId);
        return Result.success("审核成功");
    }

    /**
     * 获取月度统计列表
     */
    @GetMapping("/monthly")
    public Result<List<Map<String, Object>>> getMonthlyStatisticsList(
            @RequestParam(required = false) String yearMonth,
            @RequestParam(required = false) Long driverId) {

        List<Map<String, Object>> list = monthlyStatisticsService.getMonthlyStatisticsList(yearMonth, driverId);
        return Result.success(list);
    }

    /**
     * 获取月度统计详情
     */
    @GetMapping("/{id}")
    public Result<MonthlyStatistics> getStatisticsById(@PathVariable Long id) {
        MonthlyStatistics statistics = monthlyStatisticsService.getStatisticsById(id);
        return statistics != null ? Result.success(statistics) : Result.error("统计记录不存在");
    }
}
