package com.njht.webyun.system.model.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author : David
 * @date : 14:01 2019/12/4
 */

@ApiModel(value = "LoginLogModel", description = "日志模型")
public class LoginLogModel {
    @ApiModelProperty(value = "用户行为ID", required = true)
    private int behaviorId;

    @ApiModelProperty(value = "会话ID", required = true)
    private String sessionId;

    @ApiModelProperty(value = "用户ID", required = true)
    private int userId;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "ip", required = true)
    private String ip;

    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;

    @ApiModelProperty(value = "机构id", required = true)
    private int orgId;

    @ApiModelProperty(value = "机构名", required = true)
    private String orgName;

    @ApiModelProperty(value = "继承名", required = true)
    private String inheritedName;

    @ApiModelProperty(value = "登录时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginDate;

    @ApiModelProperty(value = "登出时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date logoutDate;

    @ApiModelProperty(value = "用户代理", required = false)
    private String userAgent;

    @ApiModelProperty(value = "操作系统", required = false)
    private String os;

    @ApiModelProperty(value = "浏览器", required = false)
    private String browser;

    @ApiModelProperty(value = "存入时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loggedDate;

    @ApiModelProperty(value = "行为", required = true)
    private String action;

    @ApiModelProperty(value = "数据是是否改变", required = true)
    private Boolean dataChanged;

    public int getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(int behaviorId) {
        this.behaviorId = behaviorId;
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

    public Boolean getDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(Boolean dataChanged) {
        this.dataChanged = dataChanged;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getInheritedName() {
        return inheritedName;
    }

    public void setInheritedName(String inheritedName) {
        this.inheritedName = inheritedName;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
