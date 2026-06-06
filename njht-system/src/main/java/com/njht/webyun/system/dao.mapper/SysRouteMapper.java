package com.njht.webyun.system.dao.mapper;

import com.njht.webyun.system.model.sysRoute.RouteRes;
import com.njht.webyun.system.model.sysRoute.RouteResp;
import com.njht.webyun.system.model.sysRoute.FunModel;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SysRouteMapper {

    /**
     *
     * @description 分页查询路由列表
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
    int addRoute(RouteRes routeRes);

    /**
     *
     * @description 修改路由信息
     * @param
     * @return
     */
    int editRoute(RouteRes routeRes);

    /**
     *
     * @description 删除路由列表
     * @param
     * @return
     */
    int deleteRoutes(List<Integer> routeIds);

    /**
     *
     * @description 根据ids删除功能列表
     * @param
     * @return
     */
    void deleteFuns(List<Integer> routeIds);

    /**
     *
     * @description 根据id删除功能列表
     * @param
     * @return
     */
    void deleteFun(Integer routeId);

    /**
     *
     * @description 增加功能信息
     * @param
     * @return
     */
    int addFunInRoute(FunModel funModel);

    /**
     *
     * @description 根据路由查询功能列表
     * @param  
     * @return  
     * @author David 
     * @date 2021/11/25 17:45 
     */
    List<FunModel> getFunsInRoute(Integer routeId);

    /**
     *
     * @description 查询出所有路由列表
     * @param
     * @return
     * @date 2021/11/26 9:42
     */
    RouteResp selectRoute();

    /**
     *
     * @description 修改功能信息
     * @author David
     * @date 2022/4/25 11:37
     */
    int updateFunInRoute(FunModel funModel);

    
    /**
     *
     * @description 
     * @author David 
     * @date 2022/4/25 13:43 
     */
    int deleteFunsByFunIds(List<Integer> funIds);

    /**
     *
     * @description
     * @author David 
     * @date 2022/4/25 14:50
     */
    void deleteRolesFunsByFunIds(List<Integer> funIds);
}
