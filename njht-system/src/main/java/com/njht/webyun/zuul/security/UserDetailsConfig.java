package com.njht.webyun.zuul.security;



import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.system.config.UserLoginParam;
import com.njht.webyun.system.dao.mapper.SysRoleMapper;
import com.njht.webyun.system.dao.mapper.SysUserMapper;
import com.njht.webyun.system.model.base.BeanProperty;
import com.njht.webyun.system.service.inf.LoginService;
import com.njht.webyun.system.service.inf.SysFunService;
import com.njht.webyun.system.service.inf.SysMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

/**
 * 自定义用户登录方法
 */
public class UserDetailsConfig implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsConfig.class);


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

    /**
     * 通过用户名查找到用户
     * 常见异常：
     * UsernameNotFoundException（用户不存在）
     * DisabledException（用户已被禁用）
     * BadCredentialsException（坏的凭据）
     * LockedException（账户锁定）
     * AccountExpiredException （账户过期）
     * CredentialsExpiredException（证书过期）
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CurrentUser user = userMapper.queryByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        int userId = user.getUserId();

        if (user.getDeleted()==1)
        {
            logger.error("用户已被删除:"+username);
            throw new LockedException("用户名无效");
        }


        if(user.isLocked()){
            logger.error("用户已被锁定:"+username);
            throw new LockedException("用户已被锁定");
        }

        List<Map<String, Object>> menus = sysMenuService.getMenusByParentId(userId, 1);
        boolean chooseLed = userLoginParam.isChooseLED();

        if(userLoginParam.isMipsEnable()){
            boolean isChooseUnitPaltForm = userLoginParam.isChooseUnitPaltForm();
            List<Map<String, Object>> tmpMenus = null;
            if(isChooseUnitPaltForm){
                Map<String, Object> tmpMenu= menus.stream().filter(m->m.get("url").toString().equals("play")).findFirst().get();
                tmpMenus =  (List<Map<String, Object>>)tmpMenu.get("children");
            }else{
                tmpMenus = menus;
            }
            Optional.ofNullable(tmpMenus).ifPresent(ms->{
                Map<String, Object> tmpMenu=null;
                if(chooseLed){
                    tmpMenu=  ms.stream().filter(m->m.get("url").toString().equals("led")).findFirst().get();
                }else{
                    tmpMenu=  ms.stream().filter(m->m.get("url").toString().equals("ledBox")).findFirst().get();
                }
                Optional.ofNullable(tmpMenu).ifPresent(m->{
                    ms.remove(m);
                });
            });
        }

//        Map<String, Boolean> map = sysFunService.getFunctionsByUserId(userId);
//        user.setFunction(map);
        user.setMenus(menus);


        boolean enabled = true; // 是否可用
        boolean accountNonExpired = true; // 是否过期
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        Set<GrantedAuthority> authorities = new HashSet<>();

        if (userId > 0) {
            // 如果你使用资源和权限配置在xml文件中，如：<intercept-url pattern="/user/admin"
            // access="hasRole('ROLE_ADMIN')"/>；
            // 并且也不想用数据库存储，所有用户都具有相同的权限的话，你可以手动保存角色(如：预订网站)。
//            authorities.add(new SimpleGrantedAuthority("user"));

            // 2022-05-25 给用户添加角色权限和接口访问权限
            List<Map<String, Object>> list = roleMapper.selectRolesByUserId(userId);

            for (Map<String, Object> m : list) {
                authorities.add(new SimpleGrantedAuthority("ROLE_"+String.valueOf(m.get(BeanProperty.Role.ROLE_NAME))));
            }
            authorities.add(new SimpleGrantedAuthority("sys:org:job"));
            user.setAuthorities(authorities);
        }


        /**
         * 同步登陆运管系统，获取运管系统的登陆报文
         * David
         * 2021-12-29
         * */
//        try{
//            //用HTTP请求调用
//            String result = HttpUtil.sendPost("http://10.181.26.105:8086/uus/system/login?username=admin&password=Modis%5E66%2553%23Terra",null);
//            if (result != null && result.startsWith("null")){
//                result = result.substring(4);
//            }
//            ManagementResult managementResult = (ManagementResult) JSONObject.toJavaObject(JSONObject.parseObject(result),ManagementResult.class);
//
////            //用Fegin调用
////            ManagementResult managementResult = loginService.loginManagement("admin","Modis%5E66%2553%23Terra");
//
//            logger.info("数管同步登陆返回返回结果：{}",managementResult.toString());
//            user.setContent(managementResult.getContent() == null ? null: managementResult.getContent());
//        }catch (Exception e){
//            logger.error("数管同步登陆接口调用失败：{}",e.getMessage());
//        }

        return user;
    }
}
