package com.dispatch.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dispatch.common.Result;
import com.dispatch.system.entity.SysUser;
import com.dispatch.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统用户管理控制器
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户列表
     */
    @GetMapping
    public Result<Page<SysUser>> getUserList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer role) {

        Page<SysUser> page = new Page<>(current, size);
        Page<SysUser> result = sysUserService.getUserList(page, username, role);
        return Result.success(result);
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        SysUser user = sysUserService.getUserById(id);
        return user != null ? Result.success(user) : Result.error("用户不存在");
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        boolean success = sysUserService.updateUser(user);
        return success ? Result.success() : Result.error("更新失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        boolean success = sysUserService.deleteUser(id);
        return success ? Result.success() : Result.error("删除失败");
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String password = params.get("password");
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        boolean success = sysUserService.resetPassword(id, password);
        return success ? Result.success() : Result.error("重置密码失败");
    }
}
