package com.dispatch.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dispatch.system.entity.MonthlyStatistics;
import org.apache.ibatis.annotations.Mapper;

/**
 * 月度统计Mapper接口
 */
@Mapper
public interface MonthlyStatisticsMapper extends BaseMapper<MonthlyStatistics> {
}
