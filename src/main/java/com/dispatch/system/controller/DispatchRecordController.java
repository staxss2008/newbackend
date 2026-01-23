package com.dispatch.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.JwtUtil;
import com.dispatch.common.Result;
import com.dispatch.system.entity.DispatchRecord;
import com.dispatch.system.entity.SysUser;
import com.dispatch.system.service.DispatchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 派车记录控制器
 */
@RestController
@RequestMapping("/dispatch")
public class DispatchRecordController {

    @Autowired
    private DispatchRecordService dispatchRecordService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private com.dispatch.system.mapper.SysUserMapper sysUserMapper;

    /**
     * 获取当前登录用户
     */
    private SysUser getCurrentUser(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            if (userId != null) {
                return sysUserMapper.selectById(userId);
            }
        }
        return null;
    }

    /**
     * 添加派车记录
     */
    @PostMapping
    public Result<Void> addDispatchRecord(@RequestBody DispatchRecord record) {
        boolean success = dispatchRecordService.addDispatchRecord(record);
        return success ? Result.success() : Result.error("添加失败");
    }

    /**
     * 更新派车记录
     */
    @PutMapping("/{id}")
    public Result<Void> updateDispatchRecord(@PathVariable Long id, @RequestBody DispatchRecord record) {
        record.setId(id);
        boolean success = dispatchRecordService.updateDispatchRecord(record);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除派车记录
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDispatchRecord(@PathVariable Long id) {
        boolean success = dispatchRecordService.deleteDispatchRecord(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 获取派车记录详情
     */
    @GetMapping("/{id}")
    public Result<DispatchRecord> getDispatchRecordById(@PathVariable Long id) {
        DispatchRecord record = dispatchRecordService.getDispatchRecordById(id);
        return record != null ? Result.success(record) : Result.error("记录不存在");
    }

    /**
     * 查询派车记录列表
     */
    @GetMapping
    public Result<Page<DispatchRecord>> getDispatchRecordList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long driverId,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        Page<DispatchRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<DispatchRecord> queryWrapper = new LambdaQueryWrapper<>();

        if (driverId != null) {
            queryWrapper.eq(DispatchRecord::getDriverId, driverId);
        }
        if (vehicleId != null) {
            queryWrapper.eq(DispatchRecord::getVehicleId, vehicleId);
        }
        if (startDate != null) {
            queryWrapper.ge(DispatchRecord::getRecordDate, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(DispatchRecord::getRecordDate, endDate);
        }

        queryWrapper.orderByDesc(DispatchRecord::getRecordDate, DispatchRecord::getCreatedAt);

        Page<DispatchRecord> result = dispatchRecordService.page(page, queryWrapper);
        return Result.success(result);
    }

    /**
     * 获取驾驶员月度详细记录
     */
    @GetMapping("/monthly-detail")
    public Result<List<Map<String, Object>>> getMonthlyDetail(
            @RequestParam Long driverId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // 获取当前登录用户
        SysUser currentUser = getCurrentUser(token);

        // 如果是驾驶员，只能查看自己的记录
        if (currentUser != null && currentUser.getRole() == 1 && !currentUser.getDriverId().equals(driverId)) {
            return Result.error("无权查看其他驾驶员的记录");
        }

        List<Map<String, Object>> detailList = dispatchRecordService.getMonthlyDetailByDriver(driverId, year, month);
        return Result.success(detailList);
    }

    /**
     * 获取驾驶员月度统计数据
     */
    @GetMapping("/monthly-statistics")
    public Result<Map<String, Object>> getMonthlyStatistics(
            @RequestParam Long driverId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // 获取当前登录用户
        SysUser currentUser = getCurrentUser(token);

        // 如果是驾驶员，只能查看自己的记录
        if (currentUser != null && currentUser.getRole() == 1 && !currentUser.getDriverId().equals(driverId)) {
            return Result.error("无权查看其他驾驶员的记录");
        }

        Map<String, Object> statistics = dispatchRecordService.getMonthlyStatistics(driverId, year, month);
        return Result.success(statistics);
    }

    /**
     * 重新计算所有派车记录的金额
     */
    @PostMapping("/recalculate-amounts")
    public Result<Void> recalculateAllAmounts() {
        dispatchRecordService.recalculateAllAmounts();
        return Result.success();
    }

    /**
     * 获取车辆上一次的收车公里数
     */
    @GetMapping("/last-end-mileage")
    public Result<BigDecimal> getLastEndMileage(@RequestParam Long vehicleId) {
        BigDecimal lastEndMileage = dispatchRecordService.getLastEndMileage(vehicleId);
        return Result.success(lastEndMileage);
    }
}
