package com.njht.webyun.system.model.sysRoute;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class RouteFunRes {

    @ApiModelProperty(value = "路由ID")
    private Integer routeId;

    @ApiModelProperty(value = "功能列表")
    List<FunModel> list = new ArrayList<>();

    public RouteFunRes() {
    }

    public RouteFunRes(Integer routeId, List<FunModel> list) {
        this.routeId = routeId;
        this.list = list;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public List<FunModel> getList() {
        return list;
    }

    public void setList(List<FunModel> list) {
        this.list = list;
    }
}
