package com.dispatch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.system.entity.Driver;
import com.dispatch.system.mapper.DriverMapper;
import com.dispatch.system.service.DriverService;
import com.dispatch.system.service.FeeStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 驾驶员服务实现类
 */
@Service
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements DriverService {

    @Autowired
    private FeeStandardService feeStandardService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDriver(Driver driver) {
        // 计算每日工资
        calculateDailyWage(driver);
        return save(driver);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDriver(Driver driver) {
        // 如果底薪发生变化,重新计算每日工资
        Driver existDriver = getById(driver.getId());
        if (existDriver != null && existDriver.getBaseSalary().compareTo(driver.getBaseSalary()) != 0) {
            calculateDailyWage(driver);
        }
        return updateById(driver);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDriver(Long id) {
        return removeById(id);
    }

    @Override
    public List<Driver> getActiveDrivers() {
        LambdaQueryWrapper<Driver> queryWrapper = new LambdaQueryWrapper<>();
        // 查询status为1或null的驾驶员（null默认视为在职）
        queryWrapper.and(wrapper -> wrapper.eq(Driver::getStatus, 1).or().isNull(Driver::getStatus));
        return list(queryWrapper);
    }

    @Override
    public Driver getDriverById(Long id) {
        return getById(id);
    }

    @Override
    public void calculateDailyWage(Driver driver) {
        if (driver.getBaseSalary() != null) {
            // 从费用标准表中获取月工作总天数
            Integer monthlyWorkingDays = feeStandardService.getMonthlyWorkingDays();
            if (monthlyWorkingDays == null || monthlyWorkingDays <= 0) {
                throw new RuntimeException("月工作天数配置无效: " + monthlyWorkingDays);
            }
            // 每日工资 = 底薪 / 月工作总天数,保留2位小数
            BigDecimal dailyWage = driver.getBaseSalary()
                    .divide(new BigDecimal(monthlyWorkingDays), 2, RoundingMode.HALF_UP);
            driver.setDailyWage(dailyWage);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateDriverWages(List<Driver> drivers) {
        for (Driver driver : drivers) {
            // 根据姓名查找驾驶员
            LambdaQueryWrapper<Driver> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Driver::getName, driver.getName());
            Driver existDriver = getOne(queryWrapper);
            
            if (existDriver != null) {
                // 更新底薪和每日工资
                existDriver.setBaseSalary(driver.getBaseSalary());
                existDriver.setDailyWage(driver.getDailyWage());
                updateById(existDriver);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recalculateAllDailyWages() {
        try {
            // 获取所有驾驶员
            List<Driver> drivers = list();
            BigDecimal avgDailyWage = null;
            int count = 0;

            for (Driver driver : drivers) {
                if (driver.getBaseSalary() != null) {
                    // 重新计算每日工资
                    calculateDailyWage(driver);
                    // 更新到数据库
                    updateById(driver);

                    // 累加每日工资用于计算平均值
                    if (avgDailyWage == null) {
                        avgDailyWage = driver.getDailyWage();
                    } else {
                        avgDailyWage = avgDailyWage.add(driver.getDailyWage());
                    }
                    count++;
                }
            }

            // 计算平均每日工资并更新公休日和节假日工资标准
            if (count > 0 && avgDailyWage != null) {
                avgDailyWage = avgDailyWage.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
                feeStandardService.calculateAndUpdateWageStandards(avgDailyWage);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("重新计算每日工资失败: " + e.getMessage());
        }
    }
}
