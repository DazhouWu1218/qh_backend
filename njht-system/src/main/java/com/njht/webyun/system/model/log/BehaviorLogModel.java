package com.njht.webyun.system.model.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author : David
 * @date : 19:53 2019/12/5
 */
@ApiModel(value = "BehaviorLoginLogModel", description = "新增用户行为日志")
public class BehaviorLogModel {
    @ApiModelProperty(value = "操作码", required = true)
    private String code;

    @ApiModelProperty(value = "args", required = true)
    private String[] args;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
