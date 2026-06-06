package com.njht.webyun.zuul.access.config;

import com.njht.webyun.zuul.access.filter.TokenUpdateFilter;
import com.njht.webyun.zuul.access.filter.CORSFilter;
import com.njht.webyun.zuul.access.filter.CustomParamDecryptFilter;
import com.njht.webyun.zuul.token.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

@Configuration
public class FilterConfig {

    @Autowired
    private TokenConfig tokenConfig;

/*    @Bean
    public FilterRegistrationBean TokenAuthFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TokenAuthFilter(tokenConfig));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(5); // 设置排序
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }*/
    @Bean
    public FilterRegistrationBean CORSFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CORSFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(6); // 设置排序
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean TokenUpdateFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TokenUpdateFilter(tokenConfig));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(15); // 设置排序
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean CustomParamDecryptFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CustomParamDecryptFilter());
        registrationBean.addInitParameter("splitChar","@");
        registrationBean.addInitParameter("filterChar",">@<@\\'@\\\"@\\\\@#@(@)");
        registrationBean.addInitParameter("replaceChar","＞’@＜@‘@“@＼@＃@（@）");
        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(4); // 设置排序
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }
}