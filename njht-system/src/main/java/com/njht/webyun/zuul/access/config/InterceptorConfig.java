package com.njht.webyun.zuul.access.config;


import com.njht.webyun.zuul.access.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author David
 * @Date 2020/12/3
 * 配置 com.aoto.zuul.access.interceptor 中的拦截器
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${token.authPath.exclude}")//取配置文件的值
    private  String[] excludePath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // token 中获取用户信息
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePath);


    }


    /**
     * 解决swagger-ui.html 404无法访问的问题
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
