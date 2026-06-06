package com.njht.webyun.system.service.inf;

import com.njht.webyun.system.model.sysRoute.RouteRes;
import com.njht.webyun.system.model.sysRoute.RouteResp;
import com.njht.webyun.system.model.sysRoute.FunModel;
import com.njht.webyun.system.model.sysRoute.RouteFunRes;
import com.github.pagehelper.Page;

import java.util.List;

public interface SysRouteService {

    /**
     *
     * @description  分页查询路由列表
     * @param
     * @return
     */
    Page<RouteResp> getRoute(RouteRes routeRes);

    /**
     *
     * @description 添加路由信息
     * @param
     * @return
     */
    void addRoute(RouteRes routeRes);

    /**
     *
     * @description 修改路由信息
     * @param
     * @return
     */
    void editRoute(RouteRes routeRes);

    /**
     *
     * @description 删除路由信息
     * @param
     * @return
     */
    void deleteRoute(List<Integer> routeIds);

    /**
     *
     * @description 添加路由里面的功能信息
     * @param
     * @return
     */
    void addFunInRoute(FunModel funModel);

    /**
     *
     * @description 查询路由中的功能列表
     * @param
     * @return
     */
    List<FunModel> showFunInRoute(Integer routeId);


    /**
     *
     * @description 查询素有
     * @param
     * @return
     */
    RouteResp selectRoutes();

    /**
     *
     * @description 修改路由里面的功能信息
     * @author David
     * @date 2022/4/25 11:36
     */
    void editFunInRoute(FunModel funModel);

    /**
     *
     * @description 删除功能
     * @author David
     * @date 2022/4/25 13:39
     */
    void deleteFunInRoute(List<Integer> funIds);
}
