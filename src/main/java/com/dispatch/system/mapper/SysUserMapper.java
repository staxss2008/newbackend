package com.dispatch.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dispatch.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 系统用户Mapper接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND status = 1")
    SysUser findByUsername(@Param("username") String username);

    /**
     * 根据驾驶员ID查询用户
     */
    @Select("SELECT * FROM sys_user WHERE driver_id = #{driverId} AND status = 1")
    SysUser findByDriverId(@Param("driverId") Long driverId);
}
