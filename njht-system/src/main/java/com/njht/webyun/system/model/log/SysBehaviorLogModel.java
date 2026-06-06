package com.njht.webyun.system.model.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author ：David
 * @date ：Created in 2020/5/15 11:42
 * @description：
 * @modified By：
 * @version: $
 */
@ApiModel(value = "SysBehaviorLoginLogModel", description = "后台操作行为日志")
public class SysBehaviorLogModel {

    @ApiModelProperty(value = "表id", required = true)
    private int behaviorId;
    @ApiModelProperty(value = "sessionid", required = true)
    private String sessionId;
    @ApiModelProperty(value = "日期", required = true)
    private Date loggedDate;
    @ApiModelProperty(value = "操作码", required = true)
    private String action;
    @ApiModelProperty(value = "数据是否改变", required = true)
    private int dataChanged;
    @ApiModelProperty(value = "菜单id", required = false)
    private int menuId;

    public int getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(int behaviorId) {
        this.behaviorId = behaviorId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLoggedDate() {
        return loggedDate;
    }

    public void setLoggedDate(Date loggedDate) {
        this.loggedDate = loggedDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(int dataChanged) {
        this.dataChanged = dataChanged;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
