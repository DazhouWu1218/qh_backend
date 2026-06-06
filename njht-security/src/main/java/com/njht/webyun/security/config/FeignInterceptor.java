package com.njht.webyun.security.config;
import com.njht.webyun.common.UserUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

@Configuration
public class FeignInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if(requestAttributes == null) {
            Map<String,String> headers = UserUtil.getHeaders();
            if(headers != null) {
                for (String key : headers.keySet()) {
                    requestTemplate.header(key, headers.get(key));
                }
            }
            return;
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Enumeration headerNames = request.getHeaderNames();
        boolean hasCookie=false;
        boolean hasToken=false;
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement().toString();
            if(!hasCookie && name != null && UserUtil.COOKIE.equals(name.toLowerCase())){
                hasCookie = true;
            }
            if(!hasToken && name != null  && UserUtil.TOKEN.equals(name)){
                hasToken = true;
            }
            requestTemplate.header(name, request.getHeader(name));
        }
        Map<String,String> headers = UserUtil.getHeaders();
        if(!hasCookie && headers != null){
            requestTemplate.header(UserUtil.COOKIE, headers.get(UserUtil.COOKIE));
        }
        if(!hasToken && headers != null){
            requestTemplate.header(UserUtil.TOKEN, headers.get(UserUtil.TOKEN));
        }
    }
}
