package com.njht.webyun.security.auth;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.model.SysRole;
import com.njht.webyun.security.annotation.Logical;
import com.njht.webyun.security.annotation.RequiresPermissions;
import com.njht.webyun.security.annotation.RequiresRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Token 权限验证，逻辑实现类
 * 
 * @author piesat
 */
public class AuthLogic
{

    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    /** 管理员角色权限标识 */
    private static final String SUPER_ADMIN = "admin";


    /**
     * 根据注解(@RequiresPermissions)鉴权, 如果验证未通过
     * 
     * @param requiresPermissions 注解对象
     */
    public void checkPermi(RequiresPermissions requiresPermissions)
    {
        if (requiresPermissions.logical() == Logical.AND)
        {
            checkPermiAnd(requiresPermissions.value());
        }
        else
        {
            checkPermiOr(requiresPermissions.value());
        }
    }

    /**
     * 验证用户是否含有指定权限，必须全部拥有
     *
     * @param permissions 权限列表
     */
    public void checkPermiAnd(String... permissions)
    {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions)
        {
            if (!hasPermi(permissionList, permission))
            {
                throw new CommonException("无访问权限");
            }
        }
    }

    /**
     * 验证用户是否含有指定权限，只需包含其中一个
     * 
     * @param permissions 权限码数组
     */
    public void checkPermiOr(String... permissions)
    {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions)
        {
            if (hasPermi(permissionList, permission))
            {
                return;
            }
        }
        if (permissions.length > 0)
        {
            throw new CommonException("无访问权限");
        }
    }

    /**
     * 判断用户是否拥有某个角色
     * 
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role)
    {
        return hasRole(getRoleNameList(), role);
    }

    /**
     * 判断用户是否拥有某个角色, 如果验证未通过
     * 
     * @param role 角色标识
     */
    public void checkRole(String role)
    {
        if (!hasRole(role))
        {
            throw new CommonException("无访问权限");
        }
    }

    /**
     * 根据注解(@RequiresRoles)鉴权
     * 
     * @param requiresRoles 注解对象
     */
    public void checkRole(RequiresRoles requiresRoles)
    {
        if (requiresRoles.logical() == Logical.AND)
        {
            checkRoleAnd(requiresRoles.value());
        }
        else
        {
            checkRoleOr(requiresRoles.value());
        }
    }

    /**
     * 验证用户是否含有指定角色，必须全部拥有
     * 
     * @param roles 角色标识数组
     */
    public void checkRoleAnd(String... roles)
    {
        Set<String> roleList = getRoleNameList();
        for (String role : roles)
        {
            if (!hasRole(roleList, role))
            {
                throw new CommonException(role+"无访问权限");
            }
        }
    }

    /**
     * 验证用户是否含有指定角色，只需包含其中一个
     * 
     * @param roles 角色标识数组
     */
    public void checkRoleOr(String... roles)
    {
        Set<String> roleList = getRoleNameList();
        for (String role : roles)
        {
            if (hasRole(roleList, role))
            {
                return;
            }
        }
        if (roles.length > 0)
        {
           throw new CommonException("无访问权限");
        }
    }


    /**
     * 根据注解(@RequiresRoles)鉴权
     * 
     * @param at 注解对象
     */
    public void checkByAnnotation(RequiresRoles at)
    {
        String[] roleArray = at.value();
        if (at.logical() == Logical.AND)
        {
            this.checkRoleAnd(roleArray);
        }
        else
        {
            this.checkRoleOr(roleArray);
        }
    }

    /**
     * 根据注解(@RequiresPermissions)鉴权
     * 
     * @param at 注解对象
     */
    public void checkByAnnotation(RequiresPermissions at) {
        String[] permissionArray = at.value();
        if (at.logical() == Logical.AND)
        {
            this.checkPermiAnd(permissionArray);
        }
        else
        {
            this.checkPermiOr(permissionArray);
        }
    }

    /**
     * 获取当前账号的权限列表
     *
     * @return 权限列表
     */
    public Set<String> getPermiList() {
        try {
            Set<String> permissions = new HashSet<>();
            Set<GrantedAuthority> authorityList =  UserUtil.getCurrentUser().getAuthorities();
            for (GrantedAuthority authority:authorityList) {
                if (!authority.getAuthority().contains("ROLE_")) {
                    permissions.add(authority.getAuthority());
                }
            }
            return permissions;
        }
        catch (Exception e)
        {
            return new HashSet<>();
        }
    }

    /**
     * 获取当前账号的角色列表
     *
     * @return 角色列表
     */
    public Set<String> getRoleNameList() {
        try {
            List<SysRole> sysRoles = UserUtil.getCurrentUser().getRoles();
            Set<String> roleNames = sysRoles.stream().map(sysRole -> sysRole.getRoleName()).collect(Collectors.toSet());
            return roleNames;
        }
        catch (Exception e) {
            return new HashSet<>();
        }
    }

    public List<String> getRoleIdList() {
        try {
            List<SysRole> sysRoles = UserUtil.getCurrentUser().getRoles();
            List<String> roleIdList = sysRoles.stream().map(sysRole -> String.valueOf(sysRole.getRoleId())).collect(Collectors.toList());
            return roleIdList;
        }
        catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(Collection<String> authorities, String permission)
    {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role 角色
     * @return 用户是否具备某角色权限
     */
    public boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> SUPER_ADMIN.contains(x) || PatternMatchUtils.simpleMatch(x, role));
    }

}
