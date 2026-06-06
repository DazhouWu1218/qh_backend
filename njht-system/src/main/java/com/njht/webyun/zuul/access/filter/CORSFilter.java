package com.njht.webyun.zuul.access.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(urlPatterns = {"/*"},filterName="cors")
@Component
public class CORSFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
	throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) servletRequest;
	String a=request.getHeader("Origin");
	HttpServletResponse response = (HttpServletResponse) servletResponse;
	response.setHeader("Access-Control-Allow-Origin",a);
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	response.setHeader("Access-Control-Allow-Credentials", "true"); // 是否支持cookie跨域
	filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}


}
