package com.dispatch.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dispatch.system.entity.SysUser;

/**
 * 系统用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     */
    Page<SysUser> getUserList(Page<SysUser> page, String username, Integer role);

    /**
     * 根据ID获取用户
     */
    SysUser getUserById(Long id);

    /**
     * 更新用户信息
     */
    boolean updateUser(SysUser user);

    /**
     * 删除用户
     */
    boolean deleteUser(Long id);

    /**
     * 重置用户密码
     */
    boolean resetPassword(Long id, String password);
}
