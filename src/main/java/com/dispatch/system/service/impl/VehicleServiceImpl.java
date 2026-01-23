package com.dispatch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.system.entity.Vehicle;
import com.dispatch.system.mapper.VehicleMapper;
import com.dispatch.system.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆服务实现类
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addVehicle(Vehicle vehicle) {
        return save(vehicle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVehicle(Vehicle vehicle) {
        return updateById(vehicle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVehicle(Long id) {
        return removeById(id);
    }

    @Override
    public List<Vehicle> getAvailableVehicles() {
        LambdaQueryWrapper<Vehicle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Vehicle::getStatus, 1);
        return list(queryWrapper);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return getById(id);
    }

    @Override
    public Vehicle getVehicleByPlateNumber(String plateNumber) {
        LambdaQueryWrapper<Vehicle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Vehicle::getPlateNumber, plateNumber);
        return getOne(queryWrapper);
    }
}
