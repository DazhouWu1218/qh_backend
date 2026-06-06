package com.njht.webyun.system.model.sysRole;

import io.swagger.annotations.ApiModelProperty;

public class BaseOperation {

    @ApiModelProperty(value = "功能ID")
    private Integer id;

    @ApiModelProperty(value = "操作")
    private Integer operation;

    public BaseOperation() {
    }

    public BaseOperation(Integer id, Integer operation) {
        this.id = id;
        this.operation = operation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }
}
