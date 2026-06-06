package com.njht.webyun.management.common.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: 代国军
 * @CreateDate: 2022/2/18 16:07
 * @Description: 数据库连接配置类
 */
//@Configuration
public class SqlConnectionConfig {

    @Value("${spring.datasource.url}")
    String jdbcUrl;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

//    @Bean
//    public HikariDataSource determinePassword() {
//        HikariDataSource dataSource = new HikariDataSource();
//        return dataSource;
//    }


}
