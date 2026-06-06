package com.njht.webyun.zuul.security;

import com.njht.webyun.common.RespBean;
import com.njht.webyun.system.model.base.BeanProperty;
import com.njht.webyun.system.service.inf.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author David
 * @date 2021/3/5
 * @description：
 */
@Component
public class MyLogoutSuccessHandel implements LogoutSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyLogoutSuccessHandel.class);
    @Autowired
    SessionRegistry sessionRegistry;
    @Autowired
    LogService logService;

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException{
        resp.setContentType("application/json;charset=utf-8");
        RespBean respBean = RespBean.ok("退出成功，请重新登录!");
        // -------------------------------------------      youngjing20191204    ----------------------------------------------
        if (null != authentication) {
            Map<String, Object> map = new HashMap<>();
            map.put(BeanProperty.LoginLog. SESSION_ID, req.getRequestedSessionId());
            map.put(BeanProperty.LoginLog.LOGOUT_DATE, new Date());
            logService.updateLogoutDate(map);
        }
        try {
            sessionRegistry.removeSessionInformation(req.getRequestedSessionId());
        } catch (Exception e) {
            logger.error("【logou操作失败】：{}",e.getMessage());
        }
        // -------------------------------------------      youngjing20191204    ----------------------------------------------
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        out.write(om.writeValueAsString(respBean));
        out.flush();
        out.close();
    }
}
