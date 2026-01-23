package com.dispatch.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dispatch.system.entity.FeeStandard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 费用标准Mapper接口
 */
@Mapper
public interface FeeStandardMapper extends BaseMapper<FeeStandard> {

    /**
     * 根据配置键查询费用标准
     */
    @Select("SELECT * FROM fee_standard WHERE config_key = #{configKey}")
    FeeStandard findByConfigKey(@Param("configKey") String configKey);

    /**
     * 根据分类查询费用标准列表
     */
    @Select("SELECT * FROM fee_standard WHERE category = #{category} ORDER BY sort_order")
    java.util.List<FeeStandard> findByCategory(@Param("category") String category);
}
