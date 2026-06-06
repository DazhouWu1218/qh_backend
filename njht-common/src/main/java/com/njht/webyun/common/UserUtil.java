package com.njht.webyun.common;


import com.njht.webyun.model.CurrentUser;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author chengqh
 * @Date 2020/11/19
 */
public class UserUtil {

    public static final String BEHAVIOR_ID = "__BEHAVIOR_ID__";

    public static final String TOKEN = "token";

    public static final String COOKIE = "cookie";

    public static ThreadLocal<CurrentUser> local = new ThreadLocal<CurrentUser>();

    public static ThreadLocal<Map<String,String>> headers = new ThreadLocal<>();

    public static CurrentUser getCurrentUser() {
        return local.get();
    }

    public static void setUser(CurrentUser user){
        local.set(user);
    }

    public static Map<String,String> getHeaders() {
        return headers.get();
    }

    public static void setHeaders(Map<String,String> user){
        headers.set(user);
    }

    public static int getBehaviorId() {
        RequestAttributes r = RequestContextHolder.getRequestAttributes();

        if (null == r)
        {
            return 0;
        }

        HttpServletRequest request = ((ServletRequestAttributes) r).getRequest();

        if (null == request)
        {
            return 0;
        }

        Object o = request.getAttribute(BEHAVIOR_ID);
        return (null == o) ? 0 : (Integer)o;
    }

}
