package com.njht.webyun.zuul.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * 登录时用户信息获取
     * @return
     */
    @Bean
    public UserDetailsConfig userDetailsConfig(){
        return new UserDetailsConfig();
    }

    @Bean
    public AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler(){
        return new AuthenticationAccessDeniedHandler();
    }

    @Bean
    public CustomMetadataSource customMetadataSource(){
        return new CustomMetadataSource();
    }

    /**
     * 密码加盐加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityConfigAdapter springSecurityConfig(){
//        return new SecurityConfigAdapter();
//    }

    @Bean
    public UrlAccessDecisionManager urlAccessDecisionManager(){
        return new UrlAccessDecisionManager();
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    MyLoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint(){
        return new MyLoginUrlAuthenticationEntryPoint();
    }

    //允许多请求地址多加斜杠  比如 /msg/list   //msg/list
    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }

}
