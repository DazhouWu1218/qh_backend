package com.njht.webyun.system.dao.mapper;

import com.njht.webyun.system.model.sysRole.RoleMenuFun;
import com.njht.webyun.system.model.sysFun.FunRole;
import com.njht.webyun.system.model.sysRole.RoleAuthMenu;
import com.njht.webyun.system.model.sysRole.RoleQuery;
import com.njht.webyun.system.model.sysRole.SysRole;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface SysRoleMapper {

    void insertRoleUser(@Param("roleId") Integer roleId, @Param("userIdlist") List<Integer> userIdlist);

    List<SysRole> selectRoleByName(SysRole role);

    List<SysRole> selectRoleByOrgId(int orgId);

    Page<SysRole> getRoles(RoleQuery userQuery);

    int addRole(SysRole role);

    int selectCountByNameAndOrgId(SysRole role);

    //删除三张表相关数据：role,user_role,menu_role
    void deleteRole(int roleId);
    void deleteRoleFun(int roleId);
    void deleteRoleUser(int roleId);

    void insertRoleMenu(FunRole funRole);

    int updateByPrimaryKeySelective(SysRole record);

    //add by tianjm 20191219  一次查询
    List<RoleAuthMenu> selectMenusByRoleIdAndUserId(RoleAuthMenu ram);

    //    -----------   下面是整合的
    List<Map<String, Object>> selectRolesByUserId(int userId);

    /**
     *
     * @description 删除权限关联的菜单和功能
     * @param
     * @return
     */
    void deleteRoleMenuFun(Integer roleId);

    /**
     *
     * @description 添加权限菜单
     * @param
     * @return
     */
    void addRoleMenu(RoleMenuFun roleMenuFun);

    /**
     *
     * @description 添加权限功能
     * @param
     * @return
     */
    void addRoleFun(RoleMenuFun roleMenuFun);

    /**
     * 根据角色id 删除菜单
     * @param roleId
     */
    void deleteRoleMenu(Integer roleId);
}
