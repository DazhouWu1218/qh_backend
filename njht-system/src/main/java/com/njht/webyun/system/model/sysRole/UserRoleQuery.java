package com.njht.webyun.system.model.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author ：David
 * @date ：Created in 2019/11/27 17:01
 * @description：角色用户查询
 * @modified By：
 * @version: $
 */
@ApiModel(value="UserRoleQuery",description="角色用户查询")
public class UserRoleQuery extends RoleQuery {
    @ApiModelProperty(value="不包括（用户id）")
    private String excepted;

    @ApiModelProperty(value="角色id")
    private Integer roleId;

    @ApiModelProperty(value="用户名")
    private String username;

    @ApiModelProperty(value="用户ids")
    private List<Integer> userIdList;

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getExcepted() {

        return excepted;
    }

    public void setExcepted(String excepted) {
        this.excepted = excepted;
    }

    @ApiOperation(value = "获取不包含的用户id，用逗号分隔")
    public String getNotIn()
    {
        if (StringUtils.isEmpty(excepted))
        {
            return null;
        }
        else
        {
            String notIn = StringUtils.EMPTY;
            int id = 0;

            if (!StringUtils.isEmpty(excepted))
            {
                String[] arr = StringUtils.split(excepted, ',');

                for (String str : arr)
                {
                    id = Integer.parseInt(str);
                    notIn += id + ",";
                }
            }

            return StringUtils.removeEnd(notIn, ",");
        }
    }
}
