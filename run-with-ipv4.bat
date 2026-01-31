@echo off
chcp 65001 >nul
echo ========================================
echo 派车管理系统 - 启动脚本 (IPv4模式)
echo ========================================
echo.

REM 设置Java系统属性，强制使用IPv4
set JAVA_OPTS=-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=false

REM 启动Spring Boot应用
echo 正在启动应用...
call mvn spring-boot:run -Dspring-boot.run.jvmArguments="%JAVA_OPTS%"

pause
