package com.njht.webyun.zuul.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author David
 * @Date 2020/11/18
 */
// 使用了@Value  就不需要 @ConfigurationProperties
//@ConfigurationProperties
@Configuration
public class TokenConfig {


    @Value("${token.expiration.Time}")
    private Long expirationTime;


    @Value("${token.authPath.exclude}")
    private String excludeAuthPath;


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
}
