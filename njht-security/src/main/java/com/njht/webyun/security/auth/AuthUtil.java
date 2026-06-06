package com.njht.webyun.security.auth;


import com.njht.webyun.security.annotation.RequiresPermissions;
import com.njht.webyun.security.annotation.RequiresRoles;

/**
 * Token 权限验证工具类
 * 
 * @author piesat
 */
public class AuthUtil
{
    /**
     * 底层的 AuthLogic 对象
     */
    public static AuthLogic authLogic = new AuthLogic();



    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotRoleException
     * 
     * @param requiresRoles 角色权限注解
     */
    public static void checkRole(RequiresRoles requiresRoles)
    {
        authLogic.checkRole(requiresRoles);
    }



    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     * 
     * @param requiresPermissions 权限注解
     */
    public static void checkPermi(RequiresPermissions requiresPermissions)
    {
        authLogic.checkPermi(requiresPermissions);
    }

}
