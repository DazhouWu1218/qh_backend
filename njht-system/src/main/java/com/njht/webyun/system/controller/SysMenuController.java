package com.njht.webyun.system.controller;


import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.validator.BeanFieldCheck;
import com.njht.webyun.system.model.sysMenu.MenuMoveModel;
import com.njht.webyun.system.model.sysMenu.SysMenu;
import com.njht.webyun.system.service.inf.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @description 菜单管理模块开放的HTTP接口
 * @author David
 * @date 2021/11/24 15:59
 */

@RestController
@RequestMapping("/system/menu/")
@Api(tags = "菜单管理",value= "menumanger")
public class SysMenuController {

    private static final Logger logger = LoggerFactory.getLogger(SysMenuController.class);

    @Autowired
    SysMenuService sysMenuService;

    @RequestMapping(value = "/tree",method = RequestMethod.POST)
    @ApiOperation(value = "查询菜单树",notes = "查询菜单树")
    public List<Map<String,Object>> getMenuInTreeJson(){
        return sysMenuService.getMenusByParentId(UserUtil.getCurrentUser().getUserId(), 0);
    }

    @RequestMapping(value = "/treeall",method = RequestMethod.POST)
    @ApiOperation(value = "查询菜单树",notes = "查询菜单树")
    public List<Map<String,Object>> getAllMenuInTreeJson(){
        return sysMenuService.getAllMenus();
    }


    @RequestMapping(value = "/tree/visitor",method = RequestMethod.POST)
    @ApiOperation(value = "游客访问查询菜单树",notes = "游客访问查询菜单树")
    public List<Map<String,Object>> getMenuInTreeJsonByVisitor(){
        return sysMenuService.getMenusByVisitor();
    }

    /**
     * 新增菜单
     * 必需的参数有：MENU_NAME,ROUT_ID,ICON,PARENT_ID,BUSINESS,SORT_NUM,LEVEL_NUM,DELETED,CREATED_BY,CREATED_DATE,LAST_UPDATED_BY,LAST_UPDATED_DATE
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增菜单")
    public void newMenu(@RequestBody SysMenu menu)
    {
        sysMenuService.addSysMenu(menu);
    }

    /**
     * 编辑菜单
     * 必需的参数有：id
     * @param menu
     * @return
     */
    @PostMapping(value = "/edit")
    @ApiOperation("编辑菜单")
    public void editMenu(@RequestBody SysMenu menu)
    {
        sysMenuService.editSysMenu(menu);
    }

    /**
     * 逻辑删除
     * 必需的参数有：id
     * @param menu
     * @return
     */
    @PostMapping(value = "/delete")
    @ApiOperation("删除菜单")
    public void removeMenu(@RequestBody SysMenu menu) {
        sysMenuService.removeSysMenu(menu);
    }

    /**
     * move修改   因菜单和在一个页面管理，暂时取消拖动效果
     * @param menuMoveModel
     * @return
     */
    @PostMapping(value = "/move")
    @ApiOperation("拖动修改菜单树")
    public void moveMenu(@Validated @RequestBody MenuMoveModel menuMoveModel, BindingResult result)
    {
        if (!"".equals(BeanFieldCheck.check(result))){
            logger.error("移动菜单树失败：{}",result);
            throw new CommMsgException(BeanFieldCheck .check(result));
        }
        sysMenuService.moveSysMenu(menuMoveModel);
    }

    @GetMapping(value = "/getUserCommonMenus")
    @ApiOperation("用户常用与标准的菜单")
    public List<Map<String,Object>> getUserCommonMenus(@Param("typeId") int typeId) {
        return sysMenuService.getUserCommonMenus(typeId);
    }


}
