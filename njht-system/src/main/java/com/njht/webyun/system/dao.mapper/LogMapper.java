package com.njht.webyun.system.dao.mapper;


import com.njht.webyun.system.model.log.LoginLogModel;
import com.njht.webyun.system.model.log.LogQuery;
import com.njht.webyun.system.model.log.OnlineLogQuery;
import com.njht.webyun.system.model.log.SysBehaviorLogModel;
import com.njht.webyun.system.model.sysUser.SysUser;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Mapper
public interface LogMapper {

    /**
     * 查询登录日志列表
     * @param logQuery 登录日志查询model
     * @return 返回登录日志列表
     */

    Page<LoginLogModel> selectLoginLogsByPage(LogQuery logQuery);

    /**
     * 记录登录日志
     * @param model 登录记录
     */

    int insertLoginLog(LoginLogModel model);

    /**
     *  更新登出时间
     * @param map
     */

    void updateLogoutDate(Map<String, Object> map);

    /**
     * 新增行为日志
     * @param model
     */

    void insertBehaviorLog(SysBehaviorLogModel model);


    /**
     * 分页查询行为日志列表
     * @param logQuery
     * @return
     */

    Page<LoginLogModel> selectBehaviorLogsByPage(LogQuery logQuery);

    /**
     * 清空表
     */

    void clearOnlineLogs();

    /**
     * 新增在线用户日志
     * @param obj
     */

    void insertOnlineLog(Object obj);

    /**
     * 分页查询在线用户
     * @return
     * @param onlineLogQuery
     */

    Page<SysUser> selectOnlineLogsByPage(OnlineLogQuery onlineLogQuery);

    /**
     *
     * @description 查询是否存在今天的访问日志（发布平台）
     * @param
     * @return
     */
    Integer getLoginBehaviorCountToday(Integer userId);

    /**
     *
     * @description 增加今日访客记录（发布平台）
     * @param
     * @return
     */
    int addLoginBehavior(Map<String, Object> map);

    /**
     *
     * @description 修改今日访客记录（发布平台）
     * @param
     * @return
     */
    int updateLoginBehavior(Map<String, Object> map);
}
