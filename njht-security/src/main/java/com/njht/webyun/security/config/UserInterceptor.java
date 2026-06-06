package com.njht.webyun.security.config;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
public class UserInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Autowired
    private static  String[] excludePath = new String[]{};
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        logger.info("拦截 {} 请求，获取到token= {}",request.getRequestURI(),token);
        if (token == null){
            return true;
        }

        Map<String,String> headers=new HashMap<>();
        headers.put(UserUtil.TOKEN,token);
        headers.put(UserUtil.COOKIE,request.getHeader(UserUtil.COOKIE));
        UserUtil.setHeaders(headers);

        CurrentUser user = TokenUtil.parseTokenToUser(token);
        logger.info("token转换成user，userId="+user.getUserId());
        UserUtil.setUser(user);
        return true;
    }

}
