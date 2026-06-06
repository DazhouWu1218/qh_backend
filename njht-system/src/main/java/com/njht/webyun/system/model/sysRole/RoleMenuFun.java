package com.njht.webyun.system.model.sysRole;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RoleMenuFun {

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "菜单ID列表")
    private List<BaseOperation> menuList;

    @ApiModelProperty(value = "功能ID列表")
    private List<BaseOperation> funList;

    public RoleMenuFun() {
    }

    public RoleMenuFun(Integer roleId, List<BaseOperation> menuList, List<BaseOperation> funList) {
        this.roleId = roleId;
        this.menuList = menuList;
        this.funList = funList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<BaseOperation> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<BaseOperation> menuList) {
        this.menuList = menuList;
    }

    public List<BaseOperation> getFunList() {
        return funList;
    }

    public void setFunList(List<BaseOperation> funList) {
        this.funList = funList;
    }
}
