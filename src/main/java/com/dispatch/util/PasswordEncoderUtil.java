
package com.dispatch.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密工具类
 */
public class PasswordEncoderUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 加密密码
     */
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 主方法 - 生成新的BCrypt哈希
     */
    public static void main(String[] args) {
        String password = "admin123";
        String encodedPassword = encode(password);
        System.out.println("明文密码: " + password);
        System.out.println("加密后密码: " + encodedPassword);

        // 验证密码
        boolean matches = matches(password, encodedPassword);
        System.out.println("密码验证: " + matches);
    }
}
