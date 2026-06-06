package com.njht.webyun.i18n;

import com.njht.webyun.spring.SpringContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @Author chengqh
 * @Date 2020/11/19
 */

@Configuration
public class LocalConfig implements WebMvcConfigurer {
    @Bean
    SpringContext springContext(){
        return new SpringContext();
    }

    @Bean
    public LocaleResolver localeResolver() {
        I18nLocaleResolver resolver = new I18nLocaleResolver();
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        lci.setParamName("lang");
        return lci;
    }
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/locale/framework");
        messageSource.setCacheSeconds(86400);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
