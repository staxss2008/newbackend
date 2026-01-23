package com.dispatch.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dispatch.system.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆Mapper接口
 */
@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
}
