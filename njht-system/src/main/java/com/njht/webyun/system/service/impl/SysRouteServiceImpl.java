package com.njht.webyun.system.service.impl;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.dao.mapper.SysRouteMapper;
import com.njht.webyun.system.model.sysRoute.RouteRes;
import com.njht.webyun.system.model.sysRoute.RouteResp;
import com.njht.webyun.system.service.inf.SysRouteService;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.model.base.BeanProperty;
import com.njht.webyun.system.model.sysRoute.FunModel;
import com.njht.webyun.system.model.sysRoute.RouteFunRes;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 *
 * @description 路由管理业务层
 * @author David
 * @date 2021/11/24 16:06
 */
@Service
@Transactional
public class SysRouteServiceImpl implements SysRouteService {

    @Autowired
    SysRouteMapper sysRouteMapper;

    @Override
    public Page<RouteResp> getRoute(RouteRes routeRes) {
        PageHelper.startPage(routeRes.getPage(), routeRes.getRows());
        return sysRouteMapper.getRoute(routeRes);
    }

    @Override
    public void addRoute(RouteRes routeRes) {
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();
        routeRes.setCreatedBy(currentUserId);
        routeRes.setCreatedDate(now);
        routeRes.setLastUpdatedBy(currentUserId);
        routeRes.setLastUpdatedDate(now);

        if (BeanProperty.Num.ZERO == sysRouteMapper.addRoute(routeRes)) {
            throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
    }

    @Override
    public void editRoute(RouteRes routeRes) {
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();
        routeRes.setLastUpdatedBy(currentUserId);
        routeRes.setLastUpdatedDate(now);

        if (BeanProperty.Num.ZERO == sysRouteMapper.editRoute(routeRes)) {
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }

    }

    @Override
    public void deleteRoute(List<Integer> routeIds) {
        if (StringUtils.isEmpty(routeIds)){
            throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
        }

        //删除路由列表
        int retNum = sysRouteMapper.deleteRoutes(routeIds);
        if (retNum != routeIds.size()) {
            throw new CommMsgException("删除成功的数量不一致");
        }
        //删除对应得功能列表
        sysRouteMapper.deleteFuns(routeIds);
    }

    @Override
    public void addFunInRoute(FunModel funModel) {
        int currentUserId = UserUtil.getCurrentUser().getUserId();

        funModel.setCreatedBy(currentUserId);
        funModel.setCreatedDate(new Date());
        funModel.setLastUpdatedBy(currentUserId);
        funModel.setCreatedDate(new Date());

        if (StringUtils.isEmpty(funModel.getRouteId())){
            throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
        }
        //添加功能列表
        if (BeanProperty.Num.ZERO == sysRouteMapper.addFunInRoute(funModel)) {
            throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }


    }

    @Override
    public List<FunModel> showFunInRoute(Integer routeId) {
        return sysRouteMapper.getFunsInRoute(routeId);
    }

    @Override
    public RouteResp selectRoutes() {
        return sysRouteMapper.selectRoute();
    }

    @Override
    public void editFunInRoute(FunModel funModel) {
        int currentUserId = UserUtil.getCurrentUser().getUserId();

        funModel.setLastUpdatedBy(currentUserId);
        funModel.setCreatedDate(new Date());

        if (StringUtils.isEmpty(funModel.getRouteId())){
            throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
        }
        //修改功能列表
        if (BeanProperty.Num.ZERO == sysRouteMapper.updateFunInRoute(funModel)) {
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }
    }

    @Override
    public void deleteFunInRoute(List<Integer> funIds) {
        if (StringUtils.isEmpty(funIds)){
            throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
        }

        //删除功能列表
        int retNum = sysRouteMapper.deleteFunsByFunIds(funIds);
        if (retNum != funIds.size()) {
            throw new CommMsgException("删除成功的数量不一致");
        }

        //删除关联的角色-功能表
        sysRouteMapper.deleteRolesFunsByFunIds(funIds);
    }
}
