package com.njht.webyun.management.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 跨域问题解决
 */
@Configuration
public class ManagementCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        CorsConfiguration config = new CorsConfiguration();
        // 设置为true 可以携带cookie信息
        config.setAllowCredentials(true);
        // 允许跨域访问的来源(* 代表允许所有的请求地址访问)
        config.addAllowedOrigin("*");
        //  * 允许携带所有的请求头信息
        config.addAllowedHeader("*");
        // *  允许所有的请求方式（get,post,option等）
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // /**过滤所有的路径
        source.registerCorsConfiguration("/**",config);

        CorsWebFilter filter = new CorsWebFilter(source);
        return  filter;
    }
}
