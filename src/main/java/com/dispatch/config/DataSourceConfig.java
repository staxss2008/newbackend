package com.dispatch.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 数据源配置类
 * 使用 Railway 提供的 MYSQL_PUBLIC_URL 环境变量
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        // 使用 Railway 提供的 MYSQL_PUBLIC_URL 环境变量
        String mysqlPublicUrl = System.getenv("MYSQL_PUBLIC_URL");
        if (mysqlPublicUrl != null && !mysqlPublicUrl.isEmpty()) {
            // 将 mysql:// 协议转换为 jdbc:mysql://
            String jdbcUrl = mysqlPublicUrl.replace("mysql://", "jdbc:mysql://");

            return DataSourceBuilder.create()
                    .type(HikariDataSource.class)
                    .url(jdbcUrl)
                    .username(System.getenv("MYSQLUSER"))
                    .password(System.getenv("MYSQLPASSWORD"))
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();
        }

        // 如果 MYSQL_PUBLIC_URL 不存在，使用默认配置
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(System.getenv("DATABASE_URL"))
                .username(System.getenv("MYSQLUSER"))
                .password(System.getenv("MYSQLPASSWORD"))
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
