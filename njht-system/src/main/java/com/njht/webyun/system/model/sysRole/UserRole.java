package com.njht.webyun.system.model.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @date 2019-11-01
 */
@ApiModel(value="UserRole",description="角色用户")
public class UserRole {

    @ApiModelProperty(value="用户表ID")
    private Integer userId;

    @ApiModelProperty(value="角色表ID")
    private Integer roleId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}