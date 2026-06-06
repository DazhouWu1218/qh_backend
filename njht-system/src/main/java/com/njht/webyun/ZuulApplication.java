package com.njht.webyun;

import com.njht.webyun.i18n.EnableI18n;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author HTHT
 */
@SpringBootApplication
@EnableZuulProxy
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients
@EnableI18n
@EnableSwagger2
@MapperScan({"com.njht.webyun.**.dao"})
public class ZuulApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(ZuulApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

}
