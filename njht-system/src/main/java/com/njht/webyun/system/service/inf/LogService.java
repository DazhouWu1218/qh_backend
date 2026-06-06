package com.njht.webyun.system.service.inf;


import com.njht.webyun.system.model.log.LogQuery;
import com.njht.webyun.system.model.log.LoginLogModel;
import com.njht.webyun.system.model.log.OnlineLogQuery;
import com.njht.webyun.system.model.sysUser.SysUser;
import com.github.pagehelper.Page;

import java.util.Map;

public interface LogService {

    /**
     * 展示登录日志列表
     * @param logQuery
     * @return 分页登录日志列表
     * @throws Exception
     */
    Page<LoginLogModel> showLoginLogsByPage(LogQuery logQuery) throws Exception;

    /**
     * 跟踪系统登录行为，实时创建登录信息
     * @param model
     */
    int createLoginLog(LoginLogModel model);


    /**
     * 更新登录日志列表
     * @param map
     */
    void updateLogoutDate(Map<String, Object> map);


    /**
     * 展示用户行为日志列表
     * @param behaviorLogQuery
     * @return
     */
    Page<LoginLogModel> showBehaviorLogsByPage(LogQuery behaviorLogQuery);


    /**
     * 新增用户行为日志
     * @param code 操作码
     * @param args
     */
    int createBehaviorLog(String code, String[] args);


    /**
     * 查询实时在线用户日志
     * @param onlineLogQuery
     * @return
     */
    Page<SysUser> showOnlineLogsByPage(OnlineLogQuery onlineLogQuery);

    /**
     * 过滤器记录后台用户行为
     * @param action
     * @param dataChanged
     * @return
     */
    int createBehaviorLog(String action, String args, boolean dataChanged,int menuId);


    int createBehaviorLog(String action, boolean dataChanged);

    /**
     *
     * @description 查询是否存在今天的访问日志（发布平台）
     * @param
     * @return
     */
    int getLoginBehaviorCountToday(Integer userId);

    /**
     *
     * @description 增加今日访客记录（发布平台）
     * @param
     * @return
     */
    void addLoginBehavior(Integer userId);

    /**
     *
     * @description 修改今日访客记录（发布平台）
     * @param
     * @return
     */
    void updateLoginBehavior(Integer userId);
}
