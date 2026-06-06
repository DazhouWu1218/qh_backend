package com.njht.webyun.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 非分页查询接口用
 * @param <T>
 */
@ApiModel(value="RespBean",description="响应数据<泛型>")
public class RespBean<T> {

    @ApiModelProperty(value="状态码")
    private Integer status;
    @ApiModelProperty(value="状态信息")
    private String msg;
    @ApiModelProperty(value="响应bean")
    private T obj;

    public RespBean() {
    }

    public RespBean build() {
        return new RespBean();
    }

    public static<T> RespBean ok(String msg, T obj) {
        return new RespBean(200, msg, obj);
    }

    public static<T> RespBean ok(String msg) {
        return new RespBean(200, msg, null);
    }

    public static<T> RespBean error(String msg, T obj) {
        return new RespBean(500, msg, obj);
    }

    public static<T> RespBean error(String msg) {
        return new RespBean(500, msg, null);
    }

    public RespBean(Integer status, String msg, T obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {

        return status;
    }

    public RespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getObj() {
        return obj;
    }

    public RespBean setObj(T obj) {
        this.obj = obj;
        return this;
    }

}
