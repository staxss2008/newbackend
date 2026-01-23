package com.dispatch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.system.entity.DispatchRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 派车记录服务接口
 */
public interface DispatchRecordService extends IService<DispatchRecord> {

    /**
     * 添加派车记录
     */
    boolean addDispatchRecord(DispatchRecord record);

    /**
     * 更新派车记录
     */
    boolean updateDispatchRecord(DispatchRecord record);

    /**
     * 删除派车记录
     */
    boolean deleteDispatchRecord(Long id);

    /**
     * 获取派车记录详情
     */
    DispatchRecord getDispatchRecordById(Long id);

    /**
     * 查询驾驶员月度详细记录
     */
    List<Map<String, Object>> getMonthlyDetailByDriver(Long driverId, Integer year, Integer month);

    /**
     * 统计驾驶员月度数据
     */
    Map<String, Object> getMonthlyStatistics(Long driverId, Integer year, Integer month);

    /**
     * 计算派车记录的各项费用
     */
    void calculateRecordAmount(DispatchRecord record);

    /**
     * 判断是否值班(根据用车事由)
     */
    boolean isDuty(String reason);

    /**
     * 重新计算所有派车记录的金额
     */
    void recalculateAllAmounts();

    /**
     * 获取车辆上一次的收车公里数
     */
    BigDecimal getLastEndMileage(Long vehicleId);
}
