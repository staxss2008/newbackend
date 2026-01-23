package com.dispatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 调度管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.dispatch.system.mapper")
@EnableAutoConfiguration(exclude = {
    org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class
})
public class DispatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(DispatchApplication.class, args);
    }
}
