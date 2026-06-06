package com.htht.executor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author piesat 2018-10-28 00:38:13
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.htht.**.dao"})
@EnableCaching
public class JobExecutorApplication {

	public static void main(String[] args) {
        SpringApplication.run(JobExecutorApplication.class, args);

	}
}