package com.njht.webyun.security.filter;

import com.njht.webyun.security.config.TokenConfig;
import com.njht.webyun.utils.PathUtil;
import com.njht.webyun.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * token 拦截器
 * lvdw 2021-5-28
 *
 * */

@WebFilter(urlPatterns = {"/*"},filterName="tokenUpdate")
public class TokenUpdateFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(TokenUpdateFilter.class);

    private static  String[] excludePath = new String[]{};
    private static  int expirationTime;

    private TokenConfig tokenConfig;
    public TokenUpdateFilter(TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String contextPath = tokenConfig.getContextPath();
        String[] split = tokenConfig.getExcludeAuthPath().split(",");
        if (!StringUtils.isEmpty(contextPath)){
            excludePath = this.getExcludePath(contextPath,split);
        } else {
            excludePath = split;
        }
        expirationTime = tokenConfig.getExpirationTime().intValue();
    }

    /**
     * 获取过滤路径字符串，需要加前缀
     * @param contextPath
     * @param excludeAuthPath
     * @return
     */
    private String[] getExcludePath(String contextPath, String[] excludeAuthPath) {
        List<String> list = new ArrayList<>();
        for (String s:excludeAuthPath){
            list.add(contextPath+s);
        }
        return String.join(",", list).split(",");
    }

    @Override
    public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) paramServletRequest;
        HttpServletResponse response = (HttpServletResponse) paramServletResponse;
        response.setContentType("application/json;charset=UTF-8");
        String requestURI = request.getRequestURI();
        logger.info("请求的url={}",requestURI);
        boolean auth = true;
        for (String path : excludePath) {
            if (PathUtil.isPathMatch(path,requestURI )) {
                logger.info("该请求不需要进行拦截：url="+path);
                auth = false;
                break;
            }
        }

        if(auth){
            try{
                String token = request.getHeader(TokenUtil.TOKEN);
                logger.info("请求被拦截的token="+token);
                Claims claims = TokenUtil.parseToken(token);
                token = TokenUtil.updateToken(claims,expirationTime);
                response.setHeader(TokenUtil.TOKEN,token);

            }catch (ExpiredJwtException expiredJwtEx){
                logger.error("token过期");
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                this.printResponseInfo(response);
                return;
            }catch (Exception ex) {
                logger.error("token 验证失败");
                //不对请求进行路由
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                this.printResponseInfo(response);
                return;
            }
        }
        paramFilterChain.doFilter(request, response);
    }

    @SneakyThrows
    private void printResponseInfo(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.write("{\"status\":\"-1\",\"msg\":\"未登录!\"}");
        out.flush();
        out.close();
    }



}
