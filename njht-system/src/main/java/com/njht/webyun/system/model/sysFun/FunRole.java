package com.njht.webyun.system.model.sysFun;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * InnoDB free: 13312 kB
 * 
 * @author David
 * 
 * @date 2019-11-01
 */
@ApiModel(value="FunRole",description="角色功能")
public class FunRole {
    @ApiModelProperty(value="表ID")
    private Integer id;

    @ApiModelProperty(value="功能ID")
    private Integer funId;

    @ApiModelProperty(value="角色ID")
    private Integer roleId;

    @ApiModelProperty(value="功能iDList")
    private List<Integer> funIdList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFunId() {
        return funId;
    }

    public void setFunId(Integer funId) {
        this.funId = funId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getFunIdList() {
        return funIdList;
    }

    public void setFunIdList(List<Integer> funIdList) {
        this.funIdList = funIdList;
    }
}