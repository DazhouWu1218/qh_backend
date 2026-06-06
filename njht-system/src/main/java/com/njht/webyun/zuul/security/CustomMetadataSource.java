package com.njht.webyun.zuul.security;


import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.utils.StringUtils;
import com.njht.webyun.system.config.UserLoginParam;
import com.njht.webyun.system.constant.MenuKeys;
import com.njht.webyun.system.dao.mapper.SysMenuMapper;
import com.njht.webyun.system.model.sysRole.SysRole;
import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.njht.webyun.system.service.inf.LogService;
import com.njht.webyun.system.service.inf.SysUrlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;



/**
 * 实现 FilterInvocationSecurityMetadataSource 通过当前的请求地址，获取该地址需要的用户角色
 * 获取有权访问当前url的角色列表
 */
public class CustomMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final static Logger logger  = LoggerFactory.getLogger(CustomMetadataSource.class);

    @Autowired
    LogService logService;
    @Autowired
    SysUrlService sysUrlService;
    @Autowired
    SysMenuMapper sysMenuMapper;

    private final RegexRequestMatcher loginMatcher ;

    private Collection<ConfigAttribute> userConfigAttr ;
    private Collection<ConfigAttribute> adminConfigAttr ;
    private Map<String, RegexRequestMatcher> permitAllMap;

    @Autowired
    UserLoginParam userLoginParam;
    private static final String ROUTE = "route";

    /**
     * 初始权限校验配置数据
     */
    public CustomMetadataSource(){
        permitAllMap = new HashMap<String, RegexRequestMatcher>();

        // 该权限是所有用户都有的权限，user权限保存在CurrentUser对象的authorities变量中
        userConfigAttr = SecurityConfig.createList("user");
        // 该权限是管理员角色，当用户id小于0时，该角色值不起作用，但是如果用户id大于0是，起作用了，会变为无权限。
        adminConfigAttr = SecurityConfig.createList("-1");

        // 获取登录请求的正则表达式
        loginMatcher = new RegexRequestMatcher("/system/login", "OPTIONS");



    }
    private boolean  isChinese(){
        Locale locale = LocaleContextHolder.getLocale();
        return locale.toString().toLowerCase().startsWith("zh");
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 获取请求对象
        HttpServletRequest request = ((FilterInvocation) o).getRequest();

        String requestUrl = request.getRequestURI();
        logger.debug("当前请求 path: {}, method: {}", requestUrl, request.getMethod());

        // 判断是否是安全请求
        boolean isSecurityRequest = (request instanceof SecurityContextHolderAwareRequestWrapper);
        // 如果不是安全请求，返回空，页面跳转到登录页面
        if (!isSecurityRequest) {
            return null;
        }

        // 判断请求的地址是否是登录请求，如果是登录请求则返回空，页面跳转到登录页面
        if (loginMatcher.matches(request)) {

            return null;
        }

        RegexRequestMatcher matcher = null;

        // 获取sys_fun_url表中的所有请求地址存入到map中
        // 角色对应的访问url接口地址关系，
        List<SysUrl> allUrl = sysUrlService.selectUrlsAndRole();
        for (SysUrl url : allUrl) {

            matcher = new RegexRequestMatcher(url.getUrlPattern(), url.getHttpMethod());

            if (matcher.matches(request)) {
                // 如果功能表sys_fun_url没有对应的角色，则返回-1角色
                if (null == url.getRoles()) {
                    return adminConfigAttr;
//                    return null;
                }

                String action = null;
                String args = null;
                boolean changed = false;
                String message = "";

                // 判断是否记录用户请求日志
                if (userLoginParam.isLogBehaviorEnabled()) {
                    action = url.getActionCode();
                    args = url.getArgsCode();
                    changed = url.getLoggedDataChanged()==0?false:true;
                    message = MapUtil.get(action,new Object[]{(isChinese() ? url.getUrlName() : url.getUrlNameEn())});

//                    if (StringUtils.isNotEmpty(action) || StringUtils.isNotEmpty(args)) {
//                        // 写增删改查行为日志
//                        int logId = logService.createBehaviorLog(action,args,changed);
//                        request.setAttribute(UserUtil.BEHAVIOR_ID, logId);
//                    }token 验证失败


                    String menuUrl = request.getHeader(ROUTE);
                    String[] obj = new String[2];
                    if(menuUrl != null && !menuUrl.equals("null")){
                        Map<String, Object> menuMap = sysMenuMapper.selectMenuByUrl(menuUrl.replaceAll("/",""));
                        MenuKeys keys = new MenuKeys();
                        obj[0] = getFieldValueByFieldName((String) menuMap.get("parentName"),keys)
                                + "/" + getFieldValueByFieldName((String) menuMap.get("menuName"),keys);;
                        obj[1] = args;

                        message = MapUtil.get(action,new Object[]{(isChinese() ? url.getUrlName() : url.getUrlNameEn())});

                        if (StringUtils.isNotEmpty(message)) {

                            //增加对菜单功能的使用频率统计，由前端全局获取当前路由地址，在header中传参  20201109
                            int logId = 0;
                            if (!StringUtils.isEmpty(menuUrl)) {
                                logId = logService.createBehaviorLog(message, menuUrl, changed,(Integer) menuMap.get("menuId"));
                            } else {
                                // 写日志
                                logId = logService.createBehaviorLog(message, changed);
                            }
                            request.setAttribute("__BEHAVIOR_ID__", logId);
                        }
                    }
                }


                List<SysRole> roles = url.getRoles();
                int size = roles.size();
                String[] values = new String[size];
                if( size == 0 ){
                    break;
                }else{
                    for (int i = 0; i < size; i++) {
                        values[i] = "ROLE_"+ String.valueOf(roles.get(i).getRoleName());
                    }
                }
                //将此请求需要什么样的角色送给 UrlAccessDecisionManager
                return SecurityConfig.createList(values);
            }
        }


        //没有匹配上的资源，都是登录访问
        return SecurityConfig.createList("ROLE_LOGIN");

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

    /**
     * 利用反射根据属性名获取属性值
     * 20201011
     * @param fieldName
     * @param object
     * @return
     */
    private static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            return (String) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

}
