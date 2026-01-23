package com.dispatch.system.controller;

import com.dispatch.common.Result;
import com.dispatch.system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        Result<String> result = authService.login(username, password);
        System.out.println("登录结果: " + result.getCode() + ", " + result.getMessage());
        if (result.getCode() == 200) {
            System.out.println("生成的Token: " + result.getData());
        }
        return result;
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            authService.logout(token);
        }
        return Result.success();
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/user-info")
    public Result<Object> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        System.out.println("获取用户信息 - 收到的Token: " + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null"));
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("Token验证失败: " + (token == null ? "token为null" : "token格式不正确"));
            return Result.error("用户未登录或登录已过期");
        }
        token = token.substring(7);
        Result<Object> result = authService.getUserInfo(token);
        System.out.println("获取用户信息结果: " + result.getCode() + ", " + result.getMessage());
        return result;
    }

    /**
     * 重置驾驶员密码（临时接口）
     */
    @PostMapping("/reset-driver-password")
    public Result<String> resetDriverPassword(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }

        Result<String> result = authService.resetPassword(username, password);
        return result;
    }
}
