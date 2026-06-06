package com.njht.webyun.management;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动类
 * @author 代国军
 */
@SpringBootApplication(scanBasePackages = "com.njht.webyun")
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableSwagger2
@EnableFeignClients
@MapperScan(basePackages = {"com.njht.**.dao"})
@ServletComponentScan(basePackages = "com.njht.webyun.management.common.token")
@Slf4j
public class ManagementApplication {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext application = SpringApplication.run(ManagementApplication.class, args);
            Environment env = application.getEnvironment();

            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String path = env.getProperty("server.servlet.context-path");

            log.info("\n----------------------------------------------------------\n\t" +
                    "Application dmscloud is running! Access URLs:\n\t" +
                    "Local: \t\thttp://localhost:" + port + path + "\n\t" +
                    "External: \thttp://" + ip + ":" + port + path + "\n\t" +
                    "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
                    "----------------------------------------------------------");
        } catch (UnknownHostException e) {
            String msg = e.getMessage();
            String sysInfo = msg != null ? "服务已启动，可能有异常发生！" + msg : "服务正常启动！";
            log.info(sysInfo);
        }
    }
}
