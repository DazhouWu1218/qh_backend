package com.njht.webyun.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页查询时用
 * @param <T>
 */
@ApiModel(value="RespBean",description="响应数据<泛型>")
public class PageRespBean<T> {

    @ApiModelProperty(value="状态码")
    private Integer status;
    @ApiModelProperty(value="状态信息")
    private String msg;
    @ApiModelProperty(value="响应bean")
    private T obj;
    @ApiModelProperty(value="数据条数")
    private Long total;

    public PageRespBean() {
    }

    public PageRespBean build() {
        return new PageRespBean();
    }

    public static<T> PageRespBean ok(String msg, T obj) {
        return new PageRespBean(200, msg, obj);
    }

    public static<T> PageRespBean ok(String msg) {
        return new PageRespBean(200, msg, null);
    }

    public static<T> PageRespBean ok(String msg, T obj, Long total) {
        return new PageRespBean(200, msg, obj, total);
    }

    public static<T> PageRespBean error(String msg, T obj) {
        return new PageRespBean(500, msg, obj);
    }

    public static<T> PageRespBean error(String msg) {
        return new PageRespBean(500, msg, null);
    }

    private PageRespBean(Integer status, String msg, T obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {

        return status;
    }

    public PageRespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public PageRespBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getObj() {
        return obj;
    }

    public PageRespBean setObj(T obj) {
        this.obj = obj;
        return this;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public PageRespBean(Integer status, String msg, T obj, Long total) {
        super();
        this.status = status;
        this.msg = msg;
        this.obj = obj;
        this.total = total;
    }
}
