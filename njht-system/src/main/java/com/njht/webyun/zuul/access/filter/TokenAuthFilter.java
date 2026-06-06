package com.njht.webyun.zuul.access.filter;

import com.njht.webyun.utils.PathUtil;
import com.njht.webyun.utils.TokenUtil;
import com.njht.webyun.zuul.token.TokenConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author David
 * @Date 2020/11/18
 *
 * 拦截所有,判断URI是否需要进行身份验证
 * 针对直接访问zuul提供接口的请求                       那是不是就不需要 ZuulTokenAuthFilter 进行对微服务的请求过滤了？
 */
//@Component
public class TokenAuthFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(TokenAuthFilter.class);

    private static  String[] excludePath = new String[]{};
    private static  int expirationTime;

    private TokenConfig tokenConfig;
    public TokenAuthFilter(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludePath = tokenConfig.getExcludeAuthPath().split(",");
        expirationTime = tokenConfig.getExpirationTime().intValue();
    }


    @Override
    public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) paramServletRequest;
        HttpServletResponse response = (HttpServletResponse) paramServletResponse;
        response.setContentType("application/json;charset=UTF-8");
        String requestURI = request.getRequestURI();
        boolean auth = true;
        for (String path : excludePath) {
            if (PathUtil.isPathMatch(path,requestURI )) {
                auth = false;
                break;
            }
        }

        if(auth){
            try{
                String token = request.getHeader(TokenUtil.TOKEN);
                Claims claims = TokenUtil.parseToken(token);
                token = TokenUtil.updateToken(claims,expirationTime);
                response.setHeader(TokenUtil.TOKEN,token);

            }catch (ExpiredJwtException expiredJwtEx){
                logger.error("token过期:()",expiredJwtEx.getMessage());
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }catch (Exception ex) {
                logger.error("token 验证失败:()",ex.getMessage());
                //不对请求进行路由
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        paramFilterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }


}
