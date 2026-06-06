package com.njht.webyun.zuul.access.filter;


import com.njht.webyun.utils.PathUtil;
import com.njht.webyun.utils.StringUtils;
import com.njht.webyun.zuul.access.config.RandomValidateCode;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ：David
 * @date ：Created in 2020/12/8
 * @description：自定义登录过滤器，为了保证验证码和密码在一起验证，防止被截断的方式进行暴力破解
 * @modified By：
 * @version: $
 */
// urlPatterns 配置的指定url地址无效
@WebFilter(urlPatterns = {"/**"})
public class ValidateCodeFilter implements Filter {

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/system/login")));


    @Override
    public void init(FilterConfig paramFilterConfig) throws ServletException {
//        isValidateCode = Boolean.parseBoolean(paramFilterConfig.getInitParameter("isVaidateCode"));//获取web.xml配置文件中过滤器中的初始化值
    }

    @Override
    public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
            throws IOException, ServletException {

        //因为下面的getsession()方法是HttpServletRequest中的，而不是父接口ServletRequest中的，所以要向上强转，以保证安全
        HttpServletRequest request = (HttpServletRequest) paramServletRequest;
        //如上同理，子调用父里面的方法是安全的，即向下（隐形）转型。而父调用子里面的方法则是不安全的，即向上强转。
        HttpServletResponse response = (HttpServletResponse) paramServletResponse;
        response.setContentType("application/json;charset=UTF-8");

//        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
//        boolean allowedPath = ALLOWED_PATHS.contains(path);
        boolean allowedPath = false;
        for (String path : ALLOWED_PATHS) {
            if (PathUtil.isPathMatch(path, request.getRequestURI())) {
                allowedPath =true;
                break;
            }
        }
        String padFlag = request.getParameter("padFlag");
        if (allowedPath && StringUtils.isEmpty(padFlag)) {

            String validateCode = request.getParameter("validate");
            HttpSession session = request.getSession();
            if (session.getAttribute(RandomValidateCode.RANDOMVALIDATECODE) != null) {
                String realCode = (String) session.getAttribute(RandomValidateCode.RANDOMVALIDATECODE);
                System.out.println(realCode);
                // 验证失败，抛出自定义的验证码异常类，然都到controller中捕获，进行处理
                // 每次验证，销毁session中的验证码，保证每个验证码只能用一次
                session.removeAttribute(RandomValidateCode.RANDOMVALIDATECODE);
                if (!validateCode.equalsIgnoreCase(realCode)) {
                    response.getWriter().write("{\"status\":500,\"msg\":\"验证码错误\"}");
                    return;
                }
            } else {
                response.getWriter().write("{\"status\":500,\"msg\":\"验证码已失效，请重新输入\"}");
                return;
            }

        }
        paramFilterChain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
