package com.njht.webyun.zuul.security;


import com.njht.webyun.zuul.access.filter.CustomParamDecryptFilter;
import com.njht.webyun.zuul.access.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//使用springsecurity开启内置的注解方式来开启方法级的动态授权
//@EnableGlobalMethodSecurity(securedEnabled = true)

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
public class SecurityConfigAdapter extends WebSecurityConfigurerAdapter {

    /**
     * 自定义登录密码认证处理器
     */
    @Autowired
    UserDetailsConfig userDetailsConfig;

    /**
     * 自定义当前用户是否具备访问url的角色
     */
    @Autowired
    UrlAccessDecisionManager urlAccessDecisionManager;

    /**
     * 自定义暂无权限处理器
     */
    @Autowired
    AuthenticationAccessDeniedHandler deniedHandler;

    /**
     * 自定义访问url资源所需的角色列表管理器
     */
    @Autowired
    CustomMetadataSource metadataSource;

    /**
     * 加密方式
     */
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionRegistry sessionRegistry;

    /**
     * 自定义无权限处理器
     */
    @Autowired
    MyLoginUrlAuthenticationEntryPoint myLoginUrlAuthenticationEntryPoint;


    /**
     * 自定义登录失败处理器
     */
    @Autowired
    LoginFailureHandler loginFailureHandler;

    /**
     * 自定义登录成功处理器
     */
    @Autowired
    LoginSuccessHandel loginSuccessHandler;

    /**
     * 自定义注销成功处理器
     */
    @Autowired
    MyLogoutSuccessHandel logoutSuccessHandler;

    /**
     * 注入自定义PermissionEvaluator
     */
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new UserPermissionEvaluator());
        return handler;
    }

//
//    @Autowired
//    CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(urlAccessDecisionManager);
                        return o;
                    }
                });

        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .and().exceptionHandling().authenticationEntryPoint(myLoginUrlAuthenticationEntryPoint)
                .and().sessionManagement().maximumSessions(10).expiredUrl("/system/login")
                .sessionRegistry(sessionRegistry)
                .and().and()
                .formLogin().loginProcessingUrl("/system/login")
                .usernameParameter("username").passwordParameter("password")
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/system/logout")
                //指定是否在注销时让httpsession无效
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies()
                .permitAll()
                .and().csrf().disable()// 关闭csrf验证(防止跨站请求伪造攻击)
                // 用来解决匿名用户访问无权限资源时的异常
                .exceptionHandling().accessDeniedHandler(deniedHandler);

        // 将参数XSS过滤与解密过滤器配置在UsernamePasswordAuthenticationFilter之前   20210312
        http.addFilterBefore(new CustomParamDecryptFilter(), UsernamePasswordAuthenticationFilter.class);
        // 将验证码过滤器配置在UsernamePasswordAuthenticationFilter之前   20201208
         http.addFilterBefore(new ValidateCodeFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    /**
     * 配置不做拦截的URL地址
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/system/menu/tree/visitor","/doc.*","/sys/dic/**","/favicon.ico","/static/**","/system/user/getSecurityUser","/**/swagger-ui.html",
                "/img/**","/Kaptcha?","/signin/**","/system/random/image","/system/orgs/export","/system/role/getUsersByRoleId","/system/role/getAllRoles","/system/user/getUserIdList","/system/orgs/getOrgByCode");
    }

    /**
     * 认证用户的来源【内存还是数据库】
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsConfig).passwordEncoder(passwordEncoder);
    }


}
