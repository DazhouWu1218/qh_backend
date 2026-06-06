package com.njht.webyun.system.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：David
 * @date ：Created in 2020/4/21 9:37
 * @description：登录后的处理
 * @modified By：
 * @version: $
 */
@ApiModel(value="LoginIndex",description="登录后的处理Model")
public class LoginIndexResp {
    @ApiModelProperty(value="心跳时长")
    private Integer heartbeatInterval;
    @ApiModelProperty(value="是否需要修改密码")
    private boolean needChangePwd;
    @ApiModelProperty(value="修改密码原因")
    private String changeReason;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Integer heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public boolean isNeedChangePwd() {
        return needChangePwd;
    }

    public void setNeedChangePwd(boolean needChangePwd) {
        this.needChangePwd = needChangePwd;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
}
