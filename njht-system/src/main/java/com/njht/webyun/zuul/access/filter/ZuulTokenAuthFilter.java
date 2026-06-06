package com.njht.webyun.zuul.access.filter;

import com.njht.webyun.utils.PathUtil;
import com.njht.webyun.utils.TokenUtil;
import com.njht.webyun.zuul.token.TokenConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @Author David
 * @Date 2020/11/18
 *
 * 拦截所有,判断URI是否需要进行身份验证
 * 此过滤器是针对  通过网关访问的微服务请求
 */

//@Component
public class ZuulTokenAuthFilter extends ZuulFilter{
    Logger logger = LoggerFactory.getLogger(ZuulTokenAuthFilter.class);


    @Autowired
    TokenConfig tokenConfig;


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String[] paths = tokenConfig.getExcludeAuthPath().split(",");

        for (String path : paths) {
            if (PathUtil.isPathMatch(path, ctx.getRequest().getRequestURI())) {

                return false;
            }
        }



        return true;
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        String token = request.getHeader("token");

        try{
            // 验证token用户权限，根据验证结果是否进行路由转发　．setSendZuulResponse（）
            TokenUtil.parseToken(token);

            ctx.setSendZuulResponse(true);
        }catch (ExpiredJwtException expiredJwtEx){
            logger.error("token过期:()",expiredJwtEx.getMessage());
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("token expiration");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }catch (Exception ex) {
            logger.error("token 验证失败()",ex.getMessage());
            //不对请求进行路由
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("invalid token");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }


        return null;
    }


}
