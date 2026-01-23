package com.dispatch.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据源配置类
 * 直接配置 Railway 数据库连接信息
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        // 直接使用 Railway 数据库连接信息
        String jdbcUrl = "jdbc:mysql://root:TqyUFxrhLFlPDkrHTIYJPVKgLBIaxtYV@switchback.proxy.rlwy.net:30696/railway?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";

        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username("root")
                .password("TqyUFxrhLFlPDkrHTIYJPVKgLBIaxtYV")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
