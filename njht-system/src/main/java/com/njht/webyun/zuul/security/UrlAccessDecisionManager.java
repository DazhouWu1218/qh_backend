package com.njht.webyun.zuul.security;


import com.njht.webyun.model.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Iterator;


/**
 * 校验当前用户是否具备访问该路径的角色
 */
public class UrlAccessDecisionManager implements AccessDecisionManager {
    private static final Logger logger = LoggerFactory.getLogger(UrlAccessDecisionManager.class);

    /**
     * @param authentication  authentication是 SYSUSER实体类 中循环添加到 GrantedAuthority 对象中的权限信息集合
     * @param object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest()
     * @param collection  CustomMetadataSource 的getAttributes(Object object)这个方法循环添加到 SecurityConfig.createList(values) 中的权限信息集合
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        // TODO 后续需要修改
        if (true) {
            return;
        }
        if(null== collection || collection.size() <=0) {

            return;
        }

        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {

            ConfigAttribute ca = iterator.next();
            //当前请求需要的权限
            String needRole = ca.getAttribute();

            if ("user".equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("未登录");
                } else
                    return;
            }

            try {
                CurrentUser principal = (CurrentUser)authentication.getPrincipal();
                if(principal != null && principal.getUserId()==-1 ){
    //                System.out.println("admin来了》》》》》》》》》-");
                    return;
                }
            } catch (Exception e) {
                throw new BadCredentialsException("未登录");
            }

            // 默认用户登录访问
            if ("ROLE_LOGIN".equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("未登录");
                } else
                    return;
            }


            //当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        logger.error("权限不足 【"+requestUrl+"】");
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {

        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {

        return true;
    }
}
