package com.dispatch.system.service.impl;

import com.dispatch.common.JwtUtil;
import com.dispatch.common.Result;
import com.dispatch.system.entity.SysUser;
import com.dispatch.system.mapper.SysUserMapper;
import com.dispatch.system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "token:";
    private static final long TOKEN_EXPIRE_TIME = 24; // 24小时

    @Override
    public Result<String> login(String username, String password) {
        // 查询用户
        SysUser user = userMapper.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 验证密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.error("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        System.out.println("生成的JWT令牌: " + token);

        // 将令牌存储到Redis
        String redisKey = TOKEN_PREFIX + token;
        System.out.println("Redis Key: " + redisKey);
        try {
            redisTemplate.opsForValue().set(redisKey, user, TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
            System.out.println("Token已成功存储到Redis");
        } catch (Exception e) {
            System.out.println("Redis存储失败: " + e.getMessage());
            // Redis 禁用时忽略错误
            System.out.println("Redis已禁用，跳过存储");
        }

        return Result.success("登录成功", token);
    }

    @Override
    public void logout(String token) {
        // 从Redis中删除令牌
        try {
            String redisKey = TOKEN_PREFIX + token;
            redisTemplate.delete(redisKey);
            System.out.println("Token已从Redis删除");
        } catch (Exception e) {
            // Redis 禁用时忽略错误
            System.out.println("Redis已禁用，跳过删除");
        }
    }

    @Override
    public Result<Object> getUserInfo(String token) {
        // 从Redis中获取用户信息
        String redisKey = TOKEN_PREFIX + token;
        System.out.println("尝试从Redis获取用户信息, Key: " + redisKey);

        SysUser user = null;
        try {
            user = (SysUser) redisTemplate.opsForValue().get(redisKey);
            System.out.println("Redis查询结果: " + (user != null ? "找到用户" : "未找到用户"));
        } catch (Exception e) {
            // Redis 禁用时忽略错误
            System.out.println("Redis已禁用，跳过查询");
            user = null;
        }

        if (user == null) {
            // 如果Redis中没有,尝试从令牌中解析
            System.out.println("尝试从JWT令牌中解析用户ID");
            Long userId = jwtUtil.getUserIdFromToken(token);
            System.out.println("从令牌解析的用户ID: " + userId);

            if (userId != null) {
                user = userMapper.selectById(userId);
                System.out.println("从数据库查询用户结果: " + (user != null ? "找到用户" : "未找到用户"));

                if (user != null) {
                    // 重新放入Redis
                    try {
                        redisTemplate.opsForValue().set(redisKey, user, TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
                        System.out.println("用户信息已重新存储到Redis");
                    } catch (Exception e) {
                        // Redis 禁用时忽略错误
                        System.out.println("Redis已禁用，跳过存储");
                    }
                }
            }
        }

        if (user == null) {
            System.out.println("用户验证失败，返回错误");
            return Result.error("用户未登录或登录已过期");
        }

        // 构建返回数据
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("realName", user.getRealName());
        userInfo.put("role", user.getRole());
        userInfo.put("phone", user.getPhone());
        userInfo.put("email", user.getEmail());
        userInfo.put("driverId", user.getDriverId());

        System.out.println("返回用户信息: " + userInfo);
        return Result.success(userInfo);
    }

    @Override
    public Result<String> resetPassword(String username, String password) {
        // 查询用户
        SysUser user = userMapper.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 加密新密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        // 更新密码
        user.setPassword(encodedPassword);
        userMapper.updateById(user);

        System.out.println("密码重置成功，用户名: " + username);
        return Result.success("密码重置成功");
    }
}
