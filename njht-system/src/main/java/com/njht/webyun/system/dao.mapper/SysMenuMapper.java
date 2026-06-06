package com.njht.webyun.system.dao.mapper;

import com.njht.webyun.system.model.sysMenu.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface SysMenuMapper {

    /**
     *
     * @description 根据父ID获取菜单树
     * @param
     * @return
     */
    List<Map<String, Object>> selectMenusByParentId(HashMap<String, Integer> map);

    /**
     *
     * @description 增加菜单
     * @param
     * @return
     */
    int addSysMenu(SysMenu sysMenu);

    /**
     *
     * @description 编辑菜单
     * @param
     * @return
     */
    int editSysMenu(SysMenu sysMenu);

    /**
     *
     * @description 删除菜单以及子节点
     * @param
     * @return
     */
    int removeSysMenu(SysMenu sysMenu);

    /**
     *
     * @description 更新菜单层级
     * @param
     * @return
     */
    void updateSysMenuSortNum(SysMenu sysMenu);

    /**
     *
     * @description 更新自身菜单
     * @param
     * @return
     */
    void updateSysMenuSelf(SysMenu m);

    /**
     *
     * @description 根据userId获取菜单
     * @param
     * @return
     */
    List<SysMenu> getSysMenusByUserId(Integer menuId, int currentUserId);

    /**
     *
     * @description 更新所有子节点
     * @param
     * @return
     */
    void updateSysMenuLevel(SysMenu m);


    /**
     *
     * @description 获取用户标准菜单
     * @param
     * @return
     */
    List<Map<String, Object>> getUserCommonFunction(List<Integer> menuIdList, int userId);

    /**
     *
     * @description 获取该用户所有能获取到的菜单和功能
     * @param
     * @return
     */
    List<Map<String,Object>> getAllMenuAndFun(int userId,int parentId);

    /**
     *
     * @description 根据角色ID获取全量菜单，标注是否有权限
     * @param
     * @return
     */
    List<Map<String, Object>> getAllMenusByRoleId(HashMap<String, Integer> map);

    /**
     *
     * @description 根据url获取菜单
     * @param
     * @return
     */
    Map<String, Object> selectMenuByUrl(String menuUrl);

    /**
     *
     * @description 获取游客访问的默认菜单
     * @param
     * @return
     */
    List<Map<String, Object>> selectMenusByVisitor(HashMap<String, Integer> map);

    /**
     *
     * @description 获取
     * @author David
     * @date 2022/4/19 17:06
     */
    List<Map<String, Object>> selectAllMenus(HashMap<String, Integer> map);
}
