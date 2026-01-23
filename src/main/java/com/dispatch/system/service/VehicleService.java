package com.dispatch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.system.entity.Vehicle;

import java.util.List;

/**
 * 车辆服务接口
 */
public interface VehicleService extends IService<Vehicle> {

    /**
     * 添加车辆
     */
    boolean addVehicle(Vehicle vehicle);

    /**
     * 更新车辆信息
     */
    boolean updateVehicle(Vehicle vehicle);

    /**
     * 删除车辆
     */
    boolean deleteVehicle(Long id);

    /**
     * 获取所有可用车辆列表
     */
    List<Vehicle> getAvailableVehicles();

    /**
     * 根据ID获取车辆信息
     */
    Vehicle getVehicleById(Long id);

    /**
     * 根据车牌号获取车辆信息
     */
    Vehicle getVehicleByPlateNumber(String plateNumber);
}
