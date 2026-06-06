package com.njht.webyun.security.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author 代国军
 * @description: 过滤 fegin 请求头
 * @date 2022/8/16 11:06
 */
@Configuration
public class ClientConfiguration {
    @Bean
    public RequestInterceptor headerInterceptor() {

        return template -> {
            // 从RequestTemplate获取headers
            Field connectorField = ReflectionUtils.findField(RequestTemplate.class, "headers");
            connectorField.setAccessible(true);

            TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            try {
                treeMap = (TreeMap) connectorField.get(template);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(Objects.isNull(treeMap)) {
                return;
            }

            // json格式请求带 content-length 失败
            if (treeMap.containsKey(HttpHeaders.CONTENT_LENGTH)) {
                treeMap.remove(HttpHeaders.CONTENT_LENGTH);
            }
        };

    }
}


