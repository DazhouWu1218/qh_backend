package com.njht.webyun.system.model.sysRole;

import com.njht.webyun.system.model.sysMenu.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author ：David
 * @date ：Created in 2019/12/19 10:47
 * @description： 给角色授权
 * @modified By：
 * @version: $
 */
@ApiModel(value="RoleAuthMenu",description="给角色授权的菜单")
public class RoleAuthMenu {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="菜单功能ID")
    private Integer id;
    @ApiModelProperty(value="父ID")
    private Integer parentId;
    @ApiModelProperty(value = "URL地址：菜单时为路径，功能时为接口地址")
    private String url;
    @ApiModelProperty(value="菜单资源名称")
    private String name;
    @ApiModelProperty(value="类型（0目录 1按钮）")
    private Integer menuType;
    @ApiModelProperty(value="序号")
    private Integer sortNum;
    @ApiModelProperty(value="层级")
    private Integer levelNum;
    @ApiModelProperty(value="角色id")
    private Integer roleId;
    @ApiModelProperty(value="用户id")
    private Integer userId;
    @ApiModelProperty(value="授权状态")
    private boolean authStatu;

    @ApiModelProperty(value="子菜单")
    private List<SysMenu> children;


    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getMenuType() {
        return menuType;
    }
    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }
    public Integer getSortNum() {
        return sortNum;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    public Integer getLevelNum() {
        return levelNum;
    }
    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }
    public Integer getRoleId() {
        return roleId;
    }
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public boolean isAuthStatu() {
        return authStatu;
    }
    public void setAuthStatu(boolean authStatu) {
        this.authStatu = authStatu;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }
}
