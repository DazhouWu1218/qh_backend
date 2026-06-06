package com.njht.webyun.system.controller;


/**
 * @author David
 * @Time 2019年11月5日 下午9:27:16
 */

import com.njht.webyun.common.RespBean;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.system.service.inf.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 这是一个只要登录就能访问的Controller
 * 主要用来获取一些配置信息
 */
@RestController
@RequestMapping("/system/config")
@Api(tags = "配置信息",value= "ConfigController")
public class ConfigController {
	
    @Autowired
    SysMenuService sysMenuService;
    
    @GetMapping("/sysmenu")
    @ApiOperation("查询用户菜单与功能权限")
    public RespBean<Map<String, Object>> sysmenu() {
//        Map<String, Object> menusByParentId = menuService.getMenusAndFunByParentId(UserUtil.getCurrentUser().getUserId(), 1);
        Map<String, Object> menusByParentId = sysMenuService.getMenusAndFunByParentId(UserUtil.getCurrentUser().getUserId(), 1);
        return RespBean.ok("ok",menusByParentId);
    }

}