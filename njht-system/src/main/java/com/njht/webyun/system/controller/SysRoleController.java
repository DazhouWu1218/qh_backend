package com.njht.webyun.system.controller;


/*
 * @author ：David
 * @date ：Created in 2019/11/25 19:46
 * @description：系统角色管理模块
 * @modified By：
 * @version: $*/


import com.njht.webyun.system.model.sysRole.RoleMenuFun;
import com.njht.webyun.system.service.inf.SysMenuService;
import com.njht.webyun.system.model.sysRole.RoleQuery;
import com.njht.webyun.system.model.sysRole.SysRole;
import com.njht.webyun.system.model.sysRole.UserRoleQuery;
import com.njht.webyun.system.service.inf.SysFunService;
import com.njht.webyun.system.service.inf.SysRoleService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "角色管理",value= "rolemanger")
@RequestMapping(value = "/system/role")
public class SysRoleController {

    private final static Logger logger = LoggerFactory.getLogger(SysRoleController.class);
    @Autowired
    SysRoleService roleService;  
    @Autowired
    SysFunService sysFunService;

    @Autowired
    SysMenuService sysMenuService;

    @PostMapping("/show")
    @ApiOperation("分页查询角色")
    public Page<SysRole> getRoles(@RequestBody RoleQuery query) throws Exception
    {
        return roleService.showRoles(query);
    }

    @PostMapping("/add")
    @ApiOperation("新增角色")
    public void addRole(@RequestBody SysRole role ) throws Exception
    {
        roleService.addRole(role);
    }

    @PostMapping("/edit")
    @ApiOperation("编辑角色")
    public void editRole(@RequestBody SysRole role ) throws Exception
    {
        roleService.editRole(role);
    }

    /**
     * 删除三张表相关数据：role,user_role,menu_role
     * @param list
     * @return*/
    @PostMapping("/delete")
    @ApiOperation("批量删除角色")
    public void deleteRole(@RequestBody List<Integer> list ) throws Exception
    {
        roleService.deleteRoles(list);
    }

    /**
     * 用于 给角色授权添加菜单功能节点
     * @param role
     * @return menuType(给前端判断是否为menu或者fun)  checkStatu 是否被勾选
     */
    @PostMapping(value = "/select")
    @ApiOperation("查询所有功能（包含角色已设置的功能）")
    public List<Map<String, Object>> selectAllFunsByCondition(@RequestBody SysRole role)
    {
//        List<Map<String, Object>> functions = new ArrayList<Map<String,Object>>();
//
//        functions = sysFunService.selectFunContainCheck(
//                UserUtil.getCurrentUser().getUserId(), 1,role.getRoleId());
//        if(functions == null || functions.size()==0){
////                return RespBean.error("未获取到功能信息");
//            throw new CommMsgException(MapUtil.get(RoleKeys.ROLE_QUERY_FAIL));
//        }
        return sysMenuService.getAllMenusByRoleId(role.getRoleId());
    }
    
    
//    @PostMapping("/menu")
//    @ApiOperation("给角色设置功能")
//    public void addRoleFun(@RequestBody FunRole funRole) throws Exception
//    {
//        roleService.addRoleMenu(funRole);
//    }

    @PostMapping("/setmenufun")
    @ApiOperation("给角色设置菜单和功能")
    public void setMenuFun(@RequestBody RoleMenuFun roleMenuFun) throws Exception
    {
        roleService.setMenuFun(roleMenuFun);
    }


    @PostMapping("/addusers")
    @ApiOperation("给角色设置成员")
    public void newRoleUser(@RequestBody UserRoleQuery userRoleQuery) throws Exception
    {
        roleService.createRoleUser(userRoleQuery.getRoleId(), userRoleQuery.getUserIdList());
    }


    @GetMapping(value = "/funs")
    @ApiOperation("根据roleId查询功能id集合")
    public List<Integer> getFunctionsByRoleId(@Param("roleId") int roleId)
    {
        return sysFunService.getFunctionsByRoleId(roleId);
    }

    @PostMapping(value = "/users/bind")
    @ApiOperation("根据roleId查询已绑定成员")
    public List<Map<String, Object>> getUsersByRoleId(@RequestBody UserRoleQuery model) throws Exception
    {
        return roleService.getUsersByRoleId(model.getRoleId());
    }

    @PostMapping(value = "/users/unbind")
    @ApiOperation("根据roleId查询未绑定的成员")
    public Page<List<Map<String, Object>>> getUsersExcepted(@RequestBody UserRoleQuery model)
    {
        return roleService.getUsersExceptByPage(model);
    }
    @GetMapping("/queryRolesByUserId")
    @ApiOperation("通过用户id获取角色列表信息")
    public List<Map<String, Object>> queryRolesByUserId(@Param("userId") int userId){
        return roleService.queryRolesByUserId(userId);
    }

    @GetMapping("/getAllRoles")
    public List<SysRole> getAllRoles(int orgId) throws Exception{
        return roleService.selectRoleByOrgId(orgId);
    }

    @GetMapping("/getUsersByRoleId")
    public List<Map<String, Object>> getUsersByRoleId(int roleId) throws Exception{
        return roleService.getUsersByRoleId(roleId);
    }

}
