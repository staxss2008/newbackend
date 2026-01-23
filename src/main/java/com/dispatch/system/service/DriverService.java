package com.dispatch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.system.entity.Driver;

import java.util.List;

/**
 * 驾驶员服务接口
 */
public interface DriverService extends IService<Driver> {

    /**
     * 添加驾驶员
     */
    boolean addDriver(Driver driver);

    /**
     * 更新驾驶员信息
     */
    boolean updateDriver(Driver driver);

    /**
     * 删除驾驶员
     */
    boolean deleteDriver(Long id);

    /**
     * 获取所有在职驾驶员列表
     */
    List<Driver> getActiveDrivers();

    /**
     * 根据ID获取驾驶员信息
     */
    Driver getDriverById(Long id);

    /**
     * 计算每日工资(底薪/27)
     */
    void calculateDailyWage(Driver driver);

    /**
     * 批量更新驾驶员工资标准
     */
    boolean batchUpdateDriverWages(List<Driver> drivers);

    /**
     * 重新计算所有驾驶员的每日工资
     */
    boolean recalculateAllDailyWages();
}
