package com.njht.webyun.zuul.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.njht.webyun.common.RespBean;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.system.config.UserLoginParam;
import com.njht.webyun.system.dao.mapper.SysRoleMapper;
import com.njht.webyun.system.dao.mapper.SysUserMapper;
import com.njht.webyun.system.model.base.BeanProperty;
import com.njht.webyun.system.model.base.LoginIndexResp;
import com.njht.webyun.system.model.log.LoginLogModel;
import com.njht.webyun.system.service.inf.*;
import com.njht.webyun.utils.IPUtil;
import com.njht.webyun.utils.StringUtils4Aoto;
import com.njht.webyun.utils.TokenUtil;
import com.njht.webyun.zuul.session.AotoSessionListener;
import com.njht.webyun.zuul.token.TokenConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author David
 * @date 2021/3/5
 * @description：
 */
@Component
public class LoginSuccessHandel implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandel.class);
    @Autowired
    TokenConfig tokenConfig;
    @Autowired
    LogService logService;
    @Autowired
    SysUserService userService;

    @Autowired
    SysUserMapper userMapper;
    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    protected SysFunService sysFunService;
    @Autowired
    protected SysRoleMapper roleMapper;
    @Autowired
    UserLoginParam userLoginParam;
    @Autowired
    LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse resp, Authentication auth)
            throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        ObjectMapper om = new ObjectMapper();
        /*  增加对密码登录成功后的合法校验  20200922   start */
        LoginIndexResp loginIndexResp = new LoginIndexResp();
        CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
        String token = TokenUtil.genToken(currentUser,tokenConfig.getExpirationTime().intValue());
        logger.info(token);
        resp.setHeader(TokenUtil.TOKEN,token);
        loginIndexResp.setUserId(currentUser.getUserId());
        userService.checkPasswordChange(loginIndexResp);
//        if(loginIndexResp.isNeedChangePwd()){
//            //第一次登录的话，必须要修改
//            //超过密码有效期，必须要修改
//            logger.info("【用户需修改密码】：第一次登录或者是密码过期");
//            resp.getWriter().write(om.writeValueAsString(new RespBean(666, "此用户第一次登录或密码已过期", currentUser)));
//            return;
//        }
        String pwd = request.getParameter("password");
        if (userService.checkPwcMatch(pwd)){
            logger.info("【用户需修改密码】：用户密码至少包含大写字母、小写字母、数字、特殊字符中的 3类且至少8位");
            resp.getWriter().write(om.writeValueAsString(new RespBean(666, "用户密码至少包含大写字母、小写字母、数字、特殊字符中的 3类且至少8位", currentUser)));
            return;
        }
        /*  增加对密码登录成功后的合法校验  20200922   end */

        //记录登陆日志
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        currentUser.setSessionId(sessionId);

        LoginLogModel model = new LoginLogModel();
        Date now = new Date();
        String userAgent = StringUtils4Aoto.trim(request.getHeader("USER-AGENT"));
        if (!StringUtils4Aoto.isEmpty(userAgent)) {
            String[] arr = StringUtils4Aoto.analyzeUserAgent(userAgent);
            model.setOs(arr[0]);
            model.setBrowser(arr[1]);
            model.setUserAgent(userAgent);
        }
        model.setSessionId(sessionId);
        model.setUserId(currentUser.getUserId());
        model.setUsername(currentUser.getUsername());
        model.setRealName(currentUser.getRealName());
        model.setIp(IPUtil.getIpAddr(request));
        model.setOrgId(currentUser.getOrgId());
        model.setOrgName(currentUser.getOrgName());
        model.setInheritedName(currentUser.getInheritedName());
        model.setLoginDate(now);

        logService.createLoginLog(model);
        AotoSessionListener.setSessionMap(sessionId, currentUser);


        /*
        * 增加发布平台访客（登陆）日志
        * */
        int loginBehaviorCount = logService.getLoginBehaviorCountToday(currentUser.getUserId());
        if (loginBehaviorCount == 0){
            //今天还没添加过访客记录
            logService.addLoginBehavior(currentUser.getUserId());
        }else{
            logService.updateLoginBehavior(currentUser.getUserId());
        }

        resp.getWriter().write(om.writeValueAsString(RespBean.ok("登录成功!", currentUser)));
    }
}
