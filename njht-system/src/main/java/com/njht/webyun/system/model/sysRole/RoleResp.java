package com.njht.webyun.system.model.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：David
 * @date ：Created in 2019/11/25 19:53
 * @description：
 * @modified By：
 * @version: $
 */
@ApiModel(value="RoleResp",description="查询角色返回")
public class RoleResp extends SysRole {
    @ApiModelProperty(value="机构名称",required=true)
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
