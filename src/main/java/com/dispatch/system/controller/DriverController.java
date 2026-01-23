package com.dispatch.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.system.entity.Driver;
import com.dispatch.system.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 驾驶员管理控制器
 */
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * 添加驾驶员
     */
    @PostMapping
    public Result<Void> addDriver(@RequestBody Driver driver) {
        boolean success = driverService.addDriver(driver);
        return success ? Result.success() : Result.error("添加失败");
    }

    /**
     * 更新驾驶员信息
     */
    @PutMapping("/{id}")
    public Result<Void> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        driver.setId(id);
        boolean success = driverService.updateDriver(driver);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除驾驶员
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDriver(@PathVariable Long id) {
        boolean success = driverService.deleteDriver(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 获取驾驶员列表
     */
    @GetMapping
    public Result<Page<Driver>> getDriverList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {

        Page<Driver> page = new Page<>(current, size);
        Page<Driver> result = driverService.page(page);
        return Result.success(result);
    }

    /**
     * 获取所有在职驾驶员列表
     */
    @GetMapping("/active")
    public Result<List<Driver>> getActiveDrivers() {
        List<Driver> drivers = driverService.getActiveDrivers();
        return Result.success(drivers);
    }

    /**
     * 根据ID获取驾驶员信息
     */
    @GetMapping("/{id}")
    public Result<Driver> getDriverById(@PathVariable Long id) {
        Driver driver = driverService.getDriverById(id);
        return driver != null ? Result.success(driver) : Result.error("驾驶员不存在");
    }

    /**
     * 批量更新驾驶员工资标准
     */
    @PostMapping("/batch-update-wages")
    public Result<String> batchUpdateDriverWages(@RequestBody List<Driver> drivers) {
        boolean success = driverService.batchUpdateDriverWages(drivers);
        return success ? Result.success("批量更新成功") : Result.error("批量更新失败");
    }

    /**
     * 重新计算所有驾驶员的每日工资
     */
    @PostMapping("/recalculate-daily-wages")
    public Result<String> recalculateDailyWages() {
        boolean success = driverService.recalculateAllDailyWages();
        return success ? Result.success("重新计算成功") : Result.error("重新计算失败");
    }
}
