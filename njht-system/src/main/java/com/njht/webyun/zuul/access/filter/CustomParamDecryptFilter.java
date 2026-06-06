package com.njht.webyun.zuul.access.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author David
 * @date 2021/3/12
 * @description：
 */
//@Component
public class CustomParamDecryptFilter implements Filter {

    private static String filterChar;
    private static String replaceChar;
    private static String splitChar;
    FilterConfig filterConfig = null;
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterChar=filterConfig.getInitParameter("filterChar");
        this.replaceChar=filterConfig.getInitParameter("replaceChar");
        this.splitChar=filterConfig.getInitParameter("splitChar");
        this.filterConfig = filterConfig;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request,filterChar,replaceChar,splitChar), response);
    }

    @Override
    public void destroy() {

    }

}
