package com.dispatch.config;

import com.zaxxer.hikari.HikariDataSource;
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
    public DataSource dataSource() {
        // 优先使用 MYSQL_PUBLIC_URL（包含所有连接信息）
        String mysqlPublicUrl = System.getenv("MYSQL_PUBLIC_URL");

        if (mysqlPublicUrl != null && !mysqlPublicUrl.isEmpty()) {
            // 将 mysql:// 协议转换为 jdbc:mysql://
            String jdbcUrl = mysqlPublicUrl.replace("mysql://", "jdbc:mysql://");

            // 添加必要的连接参数
            if (!jdbcUrl.contains("?")) {
                jdbcUrl += "?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
            }

            return DataSourceBuilder.create()
                    .type(HikariDataSource.class)
                    .url(jdbcUrl)
                    .username(System.getenv("MYSQLUSER"))
                    .password(System.getenv("MYSQLPASSWORD"))
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();
        }

        // 备用方案：使用 MYSQL_URL
        String mysqlUrl = System.getenv("MYSQL_URL");
        if (mysqlUrl != null && !mysqlUrl.isEmpty()) {
            String jdbcUrl = mysqlUrl.replace("mysql://", "jdbc:mysql://");

            if (!jdbcUrl.contains("?")) {
                jdbcUrl += "?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
            }

            return DataSourceBuilder.create()
                    .type(HikariDataSource.class)
                    .url(jdbcUrl)
                    .username(System.getenv("MYSQLUSER"))
                    .password(System.getenv("MYSQLPASSWORD"))
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();
        }

        // 最后的备用方案：使用单独的环境变量
        String mysqlHost = System.getenv("MYSQLHOST");
        String mysqlPort = System.getenv("MYSQLPORT");
        String mysqlDatabase = System.getenv("MYSQLDATABASE");
        String mysqlUser = System.getenv("MYSQLUSER");
        String mysqlPassword = System.getenv("MYSQLPASSWORD");

        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true",
                mysqlHost != null ? mysqlHost : "mysql.railway.internal",
                mysqlPort != null ? mysqlPort : "3306",
                mysqlDatabase != null ? mysqlDatabase : "railway");

        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(mysqlUser != null ? mysqlUser : "root")
                .password(mysqlPassword != null ? mysqlPassword : "TqyUFxrhLFlPDkrHTIYJPVKgLBIaxtYV")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
