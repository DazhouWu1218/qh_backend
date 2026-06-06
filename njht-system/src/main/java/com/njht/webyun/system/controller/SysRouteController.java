package com.njht.webyun.system.controller;

import com.njht.webyun.system.model.sysRoute.RouteRes;
import com.njht.webyun.system.model.sysRoute.RouteResp;
import com.njht.webyun.system.model.sysRoute.FunModel;
import com.njht.webyun.system.model.sysRoute.RouteFunRes;
import com.njht.webyun.system.service.inf.SysRouteService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @description 路由管理控制层
 * @author David
 * @date 2021/11/24 16:04
 */
@RestController
@RequestMapping("/system/route/")
@Api(tags = "路由管理",value= "menumanger")
public class SysRouteController {
    private static final Logger logger = LoggerFactory.getLogger(SysRouteController.class);

    @Autowired
    SysRouteService sysRouteService;


    @RequestMapping(value = "/show",method = RequestMethod.POST)
    @ApiOperation(value = "路由列表查询",notes = "路由列表查询")
    public Page<RouteResp> showRoute(@RequestBody RouteRes routeRes){
        return sysRouteService.getRoute(routeRes);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "增加路由信息",notes = "增加路由信息")
    public void addRoute(@RequestBody RouteRes routeRes){
        sysRouteService.addRoute(routeRes);
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ApiOperation(value = "修改路由信息",notes = "修改路由信息")
    public void editRoute(@RequestBody RouteRes routeRes){
        sysRouteService.editRoute(routeRes);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ApiOperation(value = "删除路由信息",notes = "删除路由信息")
    public void editRoute(@RequestBody List<Integer> routeIds){
        sysRouteService.deleteRoute(routeIds);
    }

    @RequestMapping(value = "/fun/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加功能信息",notes = "添加功能信息")
    public void addFunInRoute(@RequestBody FunModel funModel){
        sysRouteService.addFunInRoute(funModel);
    }

    @RequestMapping(value = "/fun/edit",method = RequestMethod.POST)
    @ApiOperation(value = "修改功能信息",notes = "添加功能信息")
    public void editFunInRoute(@RequestBody FunModel funModel){
        sysRouteService.editFunInRoute(funModel);
    }

    @RequestMapping(value = "/fun/show",method = RequestMethod.POST)
    @ApiOperation(value = "根据路由ID查询功能信息",notes = "根据路由ID查询功能信息")
    public List<FunModel> showFunInRoute(@RequestBody FunModel funModel){
        return sysRouteService.showFunInRoute(funModel.getRouteId());
    }

    @RequestMapping(value = "/fun/delete",method = RequestMethod.POST)
    @ApiOperation(value = "修改功能信息",notes = "添加功能信息")
    public void deleteFunInRoute(@RequestBody List<Integer> funIds){
        sysRouteService.deleteFunInRoute(funIds);
    }

    @RequestMapping(value = "/select",method = RequestMethod.POST)
    @ApiOperation(value = "查询所有的路由列表",notes = "查询所有的路由列表")
    public RouteResp selectRoute(){
        return sysRouteService.selectRoutes();
    }

}
