package com.njht.webyun.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author David
 * @Date 2020/11/18
 */
@Configuration
public class TokenConfig {


    @Value("${token.expiration.Time}")
    private Long expirationTime;


    @Value("${token.authPath.exclude}")
    private String excludeAuthPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getExcludeAuthPath() {
        return excludeAuthPath;
    }

    public void setExcludeAuthPath(String excludeAuthPath) {
        this.excludeAuthPath = excludeAuthPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
