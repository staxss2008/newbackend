package com.dispatch.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.system.entity.Vehicle;
import com.dispatch.system.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆管理控制器
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    /**
     * 添加车辆
     */
    @PostMapping
    public Result<String> addVehicle(@RequestBody Vehicle vehicle) {
        boolean success = vehicleService.addVehicle(vehicle);
        return success ? Result.success("添加成功") : Result.error("添加失败");
    }

    /**
     * 更新车辆信息
     */
    @PutMapping("/{id}")
    public Result<Void> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        vehicle.setId(id);
        boolean success = vehicleService.updateVehicle(vehicle);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除车辆
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteVehicle(@PathVariable Long id) {
        boolean success = vehicleService.deleteVehicle(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 获取车辆列表
     */
    @GetMapping
    public Result<Page<Vehicle>> getVehicleList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) Integer status) {

        Page<Vehicle> page = new Page<>(current, size);
        Page<Vehicle> result = vehicleService.page(page);
        return Result.success(result);
    }

    /**
     * 获取所有可用车辆列表
     */
    @GetMapping("/available")
    public Result<List<Vehicle>> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();
        return Result.success(vehicles);
    }

    /**
     * 根据ID获取车辆信息
     */
    @GetMapping("/{id}")
    public Result<Vehicle> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return vehicle != null ? Result.success(vehicle) : Result.error("车辆不存在");
    }
}
