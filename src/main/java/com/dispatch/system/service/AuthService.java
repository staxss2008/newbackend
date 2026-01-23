package com.dispatch.system.service;

import com.dispatch.common.Result;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    Result<String> login(String username, String password);

    /**
     * 用户登出
     * @param token 令牌
     */
    void logout(String token);

    /**
     * 获取当前登录用户信息
     * @param token 令牌
     * @return 用户信息
     */
    Result<Object> getUserInfo(String token);

    /**
     * 重置用户密码
     * @param username 用户名
     * @param password 新密码
     * @return 重置结果
     */
    Result<String> resetPassword(String username, String password);
}
