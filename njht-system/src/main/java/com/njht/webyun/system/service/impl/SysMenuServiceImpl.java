package com.njht.webyun.system.service.impl;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.dao.mapper.SysMenuMapper;
import com.njht.webyun.system.model.base.BeanProperty;
import com.njht.webyun.system.model.sysMenu.MenuMoveModel;
import com.njht.webyun.system.model.sysMenu.SysMenu;
import com.njht.webyun.system.service.inf.SysFunService;
import com.njht.webyun.system.service.inf.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *
 * @description 菜单管理模块业务实现层
 * @author David
 * @date 2021/11/24 16:02
 */
@Service
@Transactional
@CacheConfig(cacheNames = "SysMenus_cache")
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Autowired
    SysFunService sysFunService;

    @Override
    @Cacheable(value = BeanProperty.Cache.MENU_CACHE, key = "#root.targetClass + #root.methodName + #userId + '_' + #parentId")
    public List<Map<String, Object>> getMenusByParentId(int userId, int parentId) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("userId", userId);
        map.put("parentId", parentId);
        List<Map<String, Object>> list = sysMenuMapper.selectMenusByParentId(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> getMenusByVisitor() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("parentId", 0);
        List<Map<String, Object>> list = sysMenuMapper.selectMenusByVisitor(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> getAllMenus() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("parentId",0);
        return sysMenuMapper.selectAllMenus(map);
    }

    @Override
    public List<Map<String, Object>> getAllMenusByRoleId(int roleId) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("roleId", roleId);
        map.put("parentId",0);
        List<Map<String, Object>> list = sysMenuMapper.getAllMenusByRoleId(map);
        return list;
    }

    @Override
    @Cacheable(value = BeanProperty.Cache.MENU_CACHE, key = "#root.targetClass + #root.methodName + #userId + '_' + #parentId")
    public Map<String, Object> getMenusAndFunByParentId(int userId, int parentId)
    {
        Map<String,Object> allMap = new HashMap<String,Object>();

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("userId", userId);
        map.put("parentId", parentId);
        List<Map<String, Object>> list = sysMenuMapper.selectMenusByParentId(map);
        Map<String, Boolean> funmap = sysFunService.getFunctionsByUserId(userId);
        allMap.put("menu",list);
        allMap.put("fun",funmap);
        return allMap;
    }

    public List<SysMenu> getSysMenusByUserId() {
        Integer userId = UserUtil.getCurrentUser().getUserId();
        System.out.println("当前用户的userId="+userId);
        List<SysMenu> list = sysMenuMapper.getSysMenusByUserId(0,userId);
        return list;
    }

    @Override
    public void addSysMenu(SysMenu sysMenu) {
        if (!checkSysMenuParam(sysMenu, BeanProperty.Num.ONE)) {
            throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
        }
        int userId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();

        sysMenu.setCreatedBy(userId);
        sysMenu.setCreatedDate(now);
        sysMenu.setLastUpdatedBy(userId);
        sysMenu.setLastUpdatedDate(now);
        if (sysMenuMapper.addSysMenu(sysMenu) == BeanProperty.Num.ZERO) {
            throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
    }

    /**
     * 编辑菜单时，同时sys_fun表中的菜单也要跟着修改
     * @param sysMenu
     */
    @Override
    public void editSysMenu(SysMenu sysMenu) {
        if(!checkSysMenuParam(sysMenu, BeanProperty.Num.TWO)){
            throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
        }
        int userID= UserUtil.getCurrentUser().getUserId();
        Date date = new Date();
        //  修改sys_menu
        sysMenu.setLastUpdatedBy(userID);
        sysMenu.setLastUpdatedDate(date);
        if(sysMenuMapper.editSysMenu(sysMenu) == BeanProperty.Num.ZERO){
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }

    }

    boolean checkSysMenuParam(SysMenu sysMenu, int checkType){
        //新增时的校验
        if(BeanProperty.Num.ONE==checkType){
            if (StringUtils.isEmpty(sysMenu.getParentId()) || StringUtils.isEmpty(sysMenu.getBusiness())
                    || StringUtils.isEmpty(sysMenu.getMenuName()) || StringUtils.isEmpty(sysMenu.getSortNum())
                    || StringUtils.isEmpty(sysMenu.getLevelNum())) {
                return false;
            }
        }
        //编辑时的校验
        if(BeanProperty.Num.TWO==checkType){
            if (StringUtils.isEmpty(sysMenu.getBusiness()) || StringUtils.isEmpty(sysMenu.getMenuName())
                    || StringUtils.isEmpty(sysMenu.getSortNum()) || StringUtils.isEmpty(sysMenu.getMenuId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除包括子节点
     * 同时删除功能表对应的菜单
     * @param sysMenu
     * @return
     */
    @Override
    public void removeSysMenu(SysMenu sysMenu) {
        if(sysMenuMapper.removeSysMenu(sysMenu) == BeanProperty.Num.ZERO){
            throw new CommMsgException(MapUtil.get(CommonKey.DELETE_FAIL));
        }
    }


    /**
     * 移动功能树
     * @return
     */
    @Override
    public void moveSysMenu(MenuMoveModel model) {
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();
        // 1.更新排序号 更新其父节点的所有儿子的排序号 由前端传过来的顺序，重新赋sortNum值
        List<SysMenu> SysMenuList = model.getMenu();
        int i = 1;
        for (SysMenu sysMenu : SysMenuList) {
            //sortNum 重新排序
            sysMenu.setSortNum(i);
            sysMenu.setLastUpdatedBy(currentUserId);
            sysMenu.setLastUpdatedDate(now);
            sysMenu.setPk_menuId(sysMenu.getMenuId());
            sysMenuMapper.updateSysMenuSortNum(sysMenu);
            i++;
        }

        //2.更新菜单本身的信息
        SysMenu m = new SysMenu();
        m.setLastUpdatedBy(currentUserId);
        m.setLastUpdatedDate(now);
        m.setParentId(model.getParentId());
        m.setLevelNum(model.getParentLevelNum()+1);
        m.setMenuId(model.getMenuId());
        m.setPk_menuId(model.getMenuId());
        sysMenuMapper.updateSysMenuSelf(m);

        //3.更新本菜单原始的子菜单信息
        List<SysMenu> mList = sysMenuMapper.getSysMenusByUserId(model.getMenuId(), currentUserId);
        updateChildren(mList, model.getParentLevelNum()+1, currentUserId, now);
    }


    private void updateChildren(List<SysMenu> list, int parentLevelNum, int currentUserId, Date now)
    {
        int levelNum = parentLevelNum + 1;
        List<SysMenu> children = null;

        for (SysMenu m : list)
        {
            if (null != m.getChildren())
            {
                children = m.getChildren();
            }

            m.setLevelNum(levelNum);
            m.setLastUpdatedBy(currentUserId);
            m.setLastUpdatedDate(now);
            m.setPk_menuId(m.getMenuId());
            // 更新所有子节点本身
            sysMenuMapper.updateSysMenuLevel(m);

            if (null != children && children.size() > 0)
            {
                updateChildren(children, levelNum, currentUserId, now);
            }
        }
    }

    //	标准常用
    @Override
    public List<Map<String,Object>> getUserCommonMenus(int typeId) {
        int userId = 0;
        if (typeId == 1) {
            userId = UserUtil.getCurrentUser().getUserId();
        } else {
            userId = -1;
        }
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.njht.webyun.model.CurrentUser user = null;
        if(principal instanceof com.njht.webyun.model.CurrentUser){
            user = (com.njht.webyun.model.CurrentUser)principal;
        }
        List<Map<String, Object>> menus =(user != null ? user.getMenus() : null);
        List<Integer> menuIdList  = new ArrayList<>();
        if(menus != null && menus.size() > 0){
            getMenuIdList(menus,menuIdList);
        }
        return sysMenuMapper.getUserCommonFunction(menuIdList,userId);
    }

    private void getMenuIdList (List<Map<String, Object>> function, List<Integer> list){
        for (Map<String,Object> map: function ) {
            SysMenu attributes = (SysMenu) map.get("attributes");
            List<Map<String, Object>> children = (List<Map<String, Object>>)map.get("children");
            Integer menuId = attributes.getMenuId();
            list.add(menuId);
            if(children==null || children.size()==0){
                continue;
            }else{
                getMenuIdList(children,list);
            }
        }
    }

}
