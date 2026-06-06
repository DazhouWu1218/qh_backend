package com.njht.webyun.zuul.access.interceptor;


import com.njht.webyun.common.UserUtil;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.utils.TokenUtil;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author David
 * @Date 2020/12/3
 *  根据  token  获取用户信息
 */


public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if (token == null){
            return false;
        }

        CurrentUser user = TokenUtil.parseTokenToUser(token);

        UserUtil.setUser(user);
        return true;
    }
}
