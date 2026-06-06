package com.njht.webyun.system.service.inf;


import com.njht.webyun.system.model.sysRole.RoleMenuFun;
import com.njht.webyun.system.model.sysFun.FunRole;
import com.njht.webyun.system.model.sysRole.RoleQuery;
import com.njht.webyun.system.model.sysRole.SysRole;
import com.njht.webyun.system.model.sysRole.UserRoleQuery;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author ：David
 * @date ：Created in 2019/11/25 19:51
 * @description：
 * @modified By：
 * @version: $*/


public interface SysRoleService {

    Page<SysRole> showRoles(RoleQuery userQuery) throws Exception;

    void addRole(SysRole role) throws Exception;

    void editRole(SysRole role) throws Exception;

    void deleteRoles(List<Integer> list) throws Exception;

    void addRoleMenu(FunRole funRole) throws Exception;

    void createRoleUser(int roleId, List<Integer> list) throws Exception;

    List<Map<String, Object>> getUsersByRoleId(int roleId) throws Exception;

    Page<List<Map<String, Object>>> getUsersExceptByPage(UserRoleQuery model);

    List<Map<String, Object>> queryRolesByUserId(int userId);

    List<SysRole> selectRoleByOrgId(int orgId);

    /**
     *
     * @description  设置角色菜单和功能
     * @param
     * @return
     */
    void setMenuFun(RoleMenuFun roleMenuFun);
}
