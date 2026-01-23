package com.dispatch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dispatch.system.entity.SysUser;
import com.dispatch.system.mapper.SysUserMapper;
import com.dispatch.system.service.SysUserService;
import com.dispatch.util.PasswordEncoderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 系统用户服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public Page<SysUser> getUserList(Page<SysUser> page, String username, Integer role) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

        // 添加用户名模糊查询
        if (StringUtils.hasText(username)) {
            queryWrapper.like("username", username);
        }

        // 添加角色查询
        if (role != null) {
            queryWrapper.eq("role", role);
        }

        // 按创建时间倒序排序
        queryWrapper.orderByDesc("created_at");

        return this.page(page, queryWrapper);
    }

    @Override
    public SysUser getUserById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean updateUser(SysUser user) {
        return this.updateById(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean resetPassword(Long id, String password) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(PasswordEncoderUtil.encode(password));
        return this.updateById(user);
    }
}
