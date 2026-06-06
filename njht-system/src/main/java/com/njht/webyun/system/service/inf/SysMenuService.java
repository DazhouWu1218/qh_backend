package com.njht.webyun.system.service.inf;

import com.njht.webyun.system.model.sysMenu.MenuMoveModel;
import com.njht.webyun.system.model.sysMenu.SysMenu;

import java.util.List;
import java.util.Map;

public interface SysMenuService {


    /**
     *
     * @description 根据父ID获取菜单树
     * @param
     * @return
     */
    List<Map<String, Object>> getMenusByParentId(int userId,int parentId);

    /**
     *
     * @description 根据父id获取菜单和功能
     * @param
     * @return
     */
    Map<String, Object> getMenusAndFunByParentId(int userId, int parentId);

    /**
     *
     * @description 增加菜单
     * @param
     * @return
     */
    void addSysMenu(SysMenu menu);

    /**
     *
     * @description 编辑菜单
     * @param
     * @return
     */
    void editSysMenu(SysMenu menu);

    /**
     *
     * @description 删除菜单以及子菜单
     * @param
     * @return
     */
    void removeSysMenu(SysMenu menu);

    /**
     *
     * @description 移动菜单
     * @param
     * @return
     */
    void moveSysMenu(MenuMoveModel menuMoveModel);

    /**
     *
     * @description 获取用户标准菜单
     * @param
     * @return
     */
    List<Map<String, Object>> getUserCommonMenus(int typeId);

    /**
     *
     * @description 根据角色ID获取全量菜单，标注是否有权限
     * @param
     * @return
     */
    List<Map<String, Object>> getAllMenusByRoleId(int roleId);

    /**
     *
     * @description 游客登陆时获取的固定菜单列表
     * @param
     * @return
     */
    List<Map<String, Object>> getMenusByVisitor();

    /**
     *
     * @description 获取全量菜单
     * @author David
     * @date 2022/4/19 17:05
     */
    List<Map<String, Object>> getAllMenus();
}
