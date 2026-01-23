package com.dispatch.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.system.entity.FeeStandard;
import com.dispatch.system.mapper.FeeStandardMapper;
import com.dispatch.system.service.FeeStandardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用标准服务实现类
 */
@Service
public class FeeStandardServiceImpl extends ServiceImpl<FeeStandardMapper, FeeStandard> implements FeeStandardService {

    @Override
    public List<FeeStandard> getAllFeeStandards() {
        return list();
    }

    /**
     * 修复当月公休日期配置
     * 将config_type设置为STRING，并将config_value设置为空
     */
    @Transactional
    public void fixRestDaysStandard() {
        FeeStandard restDaysStandard = getFeeStandardByKey("current_month_rest_days");
        if (restDaysStandard != null) {
            // 如果config_type不是STRING，则修改为STRING
            if (!"STRING".equals(restDaysStandard.getConfigType())) {
                restDaysStandard.setConfigType("STRING");
            }
            // 如果config_value是数字类型，则设置为空字符串
            if (restDaysStandard.getConfigValue() != null && restDaysStandard.getConfigValue().matches("^[0-9]+$")) {
                restDaysStandard.setConfigValue("");
            }
            updateById(restDaysStandard);
        } else {
            // 如果不存在该配置，则创建
            restDaysStandard = new FeeStandard();
            restDaysStandard.setConfigKey("current_month_rest_days");
            restDaysStandard.setConfigValue("");
            restDaysStandard.setConfigType("STRING");
            restDaysStandard.setCategory("work_time");
            restDaysStandard.setDescription("当月公休日期（逗号分隔的日期列表，格式：YYYY-MM-DD）");
            restDaysStandard.setIsEditable(1);
            restDaysStandard.setSortOrder(4);
            save(restDaysStandard);
        }
    }

    @Override
    public List<FeeStandard> getFeeStandardsByCategory(String category) {
        return baseMapper.findByCategory(category);
    }

    @Override
    public FeeStandard getFeeStandardByKey(String configKey) {
        return baseMapper.findByConfigKey(configKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFeeStandard(FeeStandard feeStandard) {
        FeeStandard existStandard = getFeeStandardByKey(feeStandard.getConfigKey());

        if (existStandard == null) {
            // 如果不存在，则创建新的费用标准
            // 根据configKey设置正确的configType
            if ("current_month_rest_days".equals(feeStandard.getConfigKey())) {
                feeStandard.setConfigType("STRING");
                feeStandard.setCategory("work_time");
                feeStandard.setDescription("当月公休日期（逗号分隔的日期列表，格式：YYYY-MM-DD）");
            } else {
                feeStandard.setConfigType("INTEGER");
                feeStandard.setCategory("work_time");
                feeStandard.setDescription("月工作总天数");
            }
            feeStandard.setIsEditable(1);
            return save(feeStandard);
        }

        // 检查是否可编辑
        if (existStandard.getIsEditable() == 0) {
            throw new RuntimeException("该费用标准不允许编辑");
        }

        feeStandard.setId(existStandard.getId());
        return updateById(feeStandard);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importFeeStandardsFromCSV(String csvContent) {
        if (StrUtil.isBlank(csvContent)) {
            throw new RuntimeException("CSV内容不能为空");
        }

        // 按行分割CSV内容
        String[] lines = csvContent.split("\r?\n");

        // 跳过标题行,从第二行开始处理
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (StrUtil.isBlank(line)) {
                continue;
            }

            // 按逗号分割字段
            String[] fields = line.split(",");
            if (fields.length < 5) {
                continue;
            }

            // 解析字段
            String configKey = fields[0].trim();
            String configValue = fields[1].trim();
            String configType = fields[2].trim();
            String category = fields[3].trim();
            String description = fields.length > 4 ? fields[4].trim() : "";

            // 查找是否已存在
            FeeStandard existStandard = getFeeStandardByKey(configKey);

            FeeStandard feeStandard = new FeeStandard();
            feeStandard.setConfigKey(configKey);
            feeStandard.setConfigValue(configValue);
            feeStandard.setConfigType(configType);
            feeStandard.setCategory(category);
            feeStandard.setDescription(description);
            feeStandard.setIsEditable(1);

            if (existStandard != null) {
                // 更新
                feeStandard.setId(existStandard.getId());
                updateById(feeStandard);
            } else {
                // 新增
                save(feeStandard);
            }
        }
    }

    @Override
    public Map<String, Object> getFeeStandardConfig() {
        Map<String, Object> config = new HashMap<>();

        List<FeeStandard> standards = getAllFeeStandards();
        for (FeeStandard standard : standards) {
            String configKey = standard.getConfigKey();
            String configType = standard.getConfigType();
            String configValue = standard.getConfigValue();

            // 根据类型转换值
            Object value;
            switch (configType) {
                case "DECIMAL":
                    value = new BigDecimal(configValue);
                    break;
                case "INTEGER":
                    value = Integer.parseInt(configValue);
                    break;
                case "JSON":
                    value = configValue; // JSON字符串,需要前端解析
                    break;
                default:
                    value = configValue;
            }

            config.put(configKey, value);
        }

        return config;
    }

    /**
     * 初始化公休节假日工资标准
     */
    @Transactional(rollbackFor = Exception.class)
    public void initHolidayWageStandards() {
        // 公休日工资标准
        FeeStandard restDayWage = getFeeStandardByKey("rest_day_wage");
        if (restDayWage == null) {
            restDayWage = new FeeStandard();
            restDayWage.setConfigKey("rest_day_wage");
            restDayWage.setConfigValue("0.00");
            restDayWage.setConfigType("DECIMAL");
            restDayWage.setCategory("holiday");
            restDayWage.setDescription("公休日工资");
            restDayWage.setIsEditable(1);
            restDayWage.setSortOrder(1);
            save(restDayWage);
        }

        // 节假日工资标准
        FeeStandard holidayWage = getFeeStandardByKey("holiday_wage");
        if (holidayWage == null) {
            holidayWage = new FeeStandard();
            holidayWage.setConfigKey("holiday_wage");
            holidayWage.setConfigValue("0.00");
            holidayWage.setConfigType("DECIMAL");
            holidayWage.setCategory("holiday");
            holidayWage.setDescription("节假日工资");
            holidayWage.setIsEditable(1);
            holidayWage.setSortOrder(2);
            save(holidayWage);
        }
    }

    @Override
    public Integer getMonthlyWorkingDays() {
        FeeStandard standard = getFeeStandardByKey("monthly_working_days");
        if (standard != null && standard.getConfigValue() != null) {
            try {
                return Integer.parseInt(standard.getConfigValue().toString());
            } catch (NumberFormatException e) {
                // 如果转换失败，返回默认值
                return 27;
            }
        }
        // 默认返回27天
        return 27;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateAndUpdateWageStandards(BigDecimal dailyWage) {
        // 计算公休日工资 = 每日工资
        BigDecimal restDayWage = dailyWage.setScale(2, RoundingMode.HALF_UP);

        // 计算节假日工资 = 每日工资 * 2
        BigDecimal holidayWage = dailyWage.multiply(new BigDecimal("2")).setScale(2, RoundingMode.HALF_UP);

        // 更新或创建公休日工资标准
        FeeStandard restDayStandard = getFeeStandardByKey("rest_day_wage");
        if (restDayStandard == null) {
            restDayStandard = new FeeStandard();
            restDayStandard.setConfigKey("rest_day_wage");
            restDayStandard.setConfigType("DECIMAL");
            restDayStandard.setCategory("holiday");
            restDayStandard.setDescription("公休日工资");
            restDayStandard.setIsEditable(1);
            restDayStandard.setSortOrder(1);
        }
        restDayStandard.setConfigValue(restDayWage.toString());
        saveOrUpdate(restDayStandard);

        // 更新或创建节假日工资标准
        FeeStandard holidayStandard = getFeeStandardByKey("holiday_wage");
        if (holidayStandard == null) {
            holidayStandard = new FeeStandard();
            holidayStandard.setConfigKey("holiday_wage");
            holidayStandard.setConfigType("DECIMAL");
            holidayStandard.setCategory("holiday");
            holidayStandard.setDescription("节假日工资");
            holidayStandard.setIsEditable(1);
            holidayStandard.setSortOrder(2);
        }
        holidayStandard.setConfigValue(holidayWage.toString());
        saveOrUpdate(holidayStandard);
    }
}
