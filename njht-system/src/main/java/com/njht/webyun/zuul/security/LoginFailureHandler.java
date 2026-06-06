package com.njht.webyun.zuul.security;

import com.njht.webyun.common.RespBean;
import com.njht.webyun.system.config.UserLoginParam;
import com.njht.webyun.system.model.sysUser.SysUser;
import com.njht.webyun.system.service.inf.SysUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
 *
 * @author ：David
 * @date ：Created in 2020/5/6 17:23
 * @description：定义自定义失败处理类 实现 AuthenticationFailureHandler
 * @modified By：
 * @version: $
 */


@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final static Logger logger = LoggerFactory.getLogger(LoginFailureHandler.class);
    @Autowired
    protected MessageSource messageSource;
    @Autowired
    private SysUserService userService;
    @Autowired
    UserLoginParam userLoginParam;

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.info("进入登录认证失败处理类");

        String message = StringUtils.EMPTY;

        try {
            message = dealLoginError( exception,request, userLoginParam.isErrorLock(), userLoginParam.getErrorTimes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper om = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(om.writeValueAsString(RespBean.error(message)));
        return;
    }

    private String dealLoginError(AuthenticationException ex,HttpServletRequest request, Boolean errorLock, Integer maxErrorTimes) throws Exception{
        String message = StringUtils.EMPTY;
        String username = request.getParameter(usernameParameter);
        Locale locale = request.getLocale();
        if (ex instanceof LockedException) {
            message = messageSource.getMessage("login.error1", null, locale);
        } else if (ex instanceof BadCredentialsException) {
            if (!errorLock) {
                message = messageSource.getMessage("login.error2", null, locale);
            } else {
                if (null == username) {
                    message = messageSource.getMessage("login.error2", null, locale);
                } else {
                    SysUser tmpUser = userService.getUserinfoByUsername(username);
                    if(tmpUser == null || -1 == tmpUser.getUserId()){
                        message = messageSource.getMessage("login.error2", null, locale); //超级管理员密码输入错误次数不限
                    }else{

                        // 失败后，错误次数+1
                        Integer errorTime = (null == tmpUser.getErrorTime() || 0 == tmpUser.getErrorTime()) ? 0
                                : (Integer) tmpUser.getErrorTime();
                        errorTime++;
                        Map<String, Object> paraMap = new HashMap<String, Object>(2);
                        paraMap.put("userId", tmpUser.getUserId());
                        paraMap.put("errorTime", errorTime);
                        userService.updateUserErrorInfo(paraMap);

                        // 错误次数超过最大次数，锁定用户
                        if (errorTime >= maxErrorTimes) {
                            userService.lockUserByLoginError((Integer) tmpUser.getUserId());
                            message = messageSource.getMessage("login.error1", null, locale);
                        } else {
                            Integer restTime = maxErrorTimes - errorTime;
                            Object[] objs = { restTime };
                            message = messageSource.getMessage("login.error4", objs, locale);
                        }
                    }
                }
            }
        }
        logger.info("message：" + message +" username：" + username);
        return message;
    }

}
