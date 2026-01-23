package com.dispatch.system.controller;

import com.dispatch.common.Result;
import com.dispatch.system.entity.FeeStandard;
import com.dispatch.system.service.FeeStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 费用标准控制器
 */
@RestController
@RequestMapping("/fee-standard")
public class FeeStandardController {

    @Autowired
    private FeeStandardService feeStandardService;

    /**
     * 获取所有费用标准
     */
    @GetMapping
    public Result<List<FeeStandard>> getAllFeeStandards() {
        List<FeeStandard> standards = feeStandardService.getAllFeeStandards();
        return Result.success(standards);
    }

    /**
     * 根据分类获取费用标准
     */
    @GetMapping("/category/{category}")
    public Result<List<FeeStandard>> getFeeStandardsByCategory(@PathVariable String category) {
        List<FeeStandard> standards = feeStandardService.getFeeStandardsByCategory(category);
        return Result.success(standards);
    }

    /**
     * 根据配置键获取费用标准
     */
    @GetMapping("/key/{configKey}")
    public Result<FeeStandard> getFeeStandardByKey(@PathVariable String configKey) {
        FeeStandard standard = feeStandardService.getFeeStandardByKey(configKey);
        return standard != null ? Result.success(standard) : Result.error("费用标准不存在");
    }

    /**
     * 更新费用标准
     */
    @PutMapping
    public Result<Void> updateFeeStandard(@RequestBody FeeStandard feeStandard) {
        boolean success = feeStandardService.updateFeeStandard(feeStandard);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 导入费用标准(CSV格式)
     */
    @PostMapping("/import")
    public Result<String> importFeeStandardsFromCSV(@RequestBody String csvContent) {
        feeStandardService.importFeeStandardsFromCSV(csvContent);
        return Result.success("导入成功");
    }

    /**
     * 获取费用标准配置Map
     */
    @GetMapping("/config")
    public Result<Map<String, Object>> getFeeStandardConfig() {
        Map<String, Object> config = feeStandardService.getFeeStandardConfig();
        return Result.success(config);
    }

    /**
     * 初始化公休节假日工资标准
     */
    @PostMapping("/init-holiday-wage")
    public Result<String> initHolidayWageStandards() {
        feeStandardService.initHolidayWageStandards();
        return Result.success("初始化成功");
    }
}
