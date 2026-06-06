package com.htht.job.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author piesat 2018-10-28 00:38:13
 */
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
@Slf4j
@EnableTransactionManagement
public class JobAdminApplication {

	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext application = SpringApplication.run(JobAdminApplication.class, args);
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