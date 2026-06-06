package com.njht.webyun.system.model.sysRole;

import com.njht.webyun.system.model.base.BasePageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：David
 * @date ：Created in 2019/11/25 19:35
 * @description：分页查询系统角色
 * @modified By：
 * @version: $
 */
@ApiModel(value="RoleQuery",description="分页查询角色")
public class RoleQuery extends BasePageModel {
    @ApiModelProperty(value="角色名",required=true)
    private String roleName;
    @ApiModelProperty(value="机构ID",required=true)
    private int orgId;
    @ApiModelProperty(value="是否包含子机构",required=true)
    private boolean containSub;
    @ApiModelProperty(value="排序字段",required=false)
    private String sort;
    @ApiModelProperty(value="排序方式",required=false)
    private String order;


    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public boolean isContainSub() {
        return containSub;
    }

    public void setContainSub(boolean containSub) {
        this.containSub = containSub;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
