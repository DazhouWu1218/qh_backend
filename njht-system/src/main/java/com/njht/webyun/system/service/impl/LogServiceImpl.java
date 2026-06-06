package com.njht.webyun.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.enums.NumberEnum;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.constant.UserKeys;
import com.njht.webyun.system.dao.mapper.LogMapper;
import com.njht.webyun.system.dao.mapper.SysUserMapper;
import com.njht.webyun.system.model.base.BeanProperty;
import com.njht.webyun.system.model.log.LogQuery;
import com.njht.webyun.system.model.log.LoginLogModel;
import com.njht.webyun.system.model.log.OnlineLogQuery;
import com.njht.webyun.system.model.log.SysBehaviorLogModel;
import com.njht.webyun.system.model.sysUser.SysUser;
import com.njht.webyun.system.service.inf.LogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.TypeReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author : David
 * @date : 9:58 2019/12/4
 */
@Service
@Transactional
public class LogServiceImpl implements LogService {

    @Autowired
    LogMapper logMapper;

    @Autowired
    SysUserMapper userMapper;

    @Autowired
    MessageSource messageSource;

    @Autowired
    protected SessionRegistry sessionRegistry;


    private static SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 展示户登录日志
     *
     * @param logQuery 查询条件
     * @return 用户登录日志*/


    @Override
    public Page<LoginLogModel> showLoginLogsByPage(LogQuery logQuery){
        PageHelper.startPage(logQuery.getPage(), logQuery.getRows());
        return  logMapper.selectLoginLogsByPage(logQuery);
    }


    /**
     * 展示户行为日志列表
     *
     * @param logQuery 查询条件
     * @return 用户行为日志列表
     */

    @Override
    public Page<LoginLogModel> showBehaviorLogsByPage(LogQuery logQuery) {
        PageHelper.startPage(logQuery.getPage(), logQuery.getRows());
        return  logMapper.selectBehaviorLogsByPage(logQuery);
    }


    @Override
    public int createBehaviorLog(String code, String[] args) {
        String[] params = null;

        if (null != args && args.length > 0)
        {
            List<String> list = new ArrayList<>(2);

            for (String str : args)
            {
                list.add(messageSource.getMessage(str, null, Locale.CHINESE));
            }

            params = new String[list.size()];
            list.toArray(params);
        }

        String action = messageSource.getMessage(code, params, Locale.CHINESE);
        return createBehaviorLog(action,null,false,-1);
    }

    /**
     * @param onlineLogQuery
     * @return 在线用户分页列表*/


    @Override
    public Page<SysUser> showOnlineLogsByPage(OnlineLogQuery onlineLogQuery) {
        // 捕捉事务
        try {
            // 清空表
            logMapper.clearOnlineLogs();
            List<Object> principals = sessionRegistry.getAllPrincipals();
            SysUser user = null;
            for (Object principal : principals) {

                //4.封装其他信息
                Map<String, String> map = JSONObject.parseObject(JSONObject.toJSONString(principal), new TypeReference<Map<String, String>>() {
                });
                // 转换时间
                String lastLoginDate = format.format(new Date(Long.parseLong(map.get("lastLoginDate"))));
                String createdDate = format.format(new Date(Long.parseLong(map.get("createdDate"))));
                String lastUpdateDate = format.format(new Date(Long.parseLong(map.get("lastUpdateDate"))));
                map.put("lastLoginDate", lastLoginDate);
                map.put("createdDate", createdDate);
                map.put("lastUpdateDate", lastUpdateDate);
                // 插入在线用户数据
                logMapper.insertOnlineLog(map);
            }

            // 分页查询
            PageHelper.startPage(onlineLogQuery.getPage(), onlineLogQuery.getRows());
            return logMapper.selectOnlineLogsByPage(onlineLogQuery);
        } catch (Exception e) {
            e.printStackTrace();
            // trigger事务回滚
//            throw new RuntimeException("查询在线用户失败：" + e.getMessage());
            throw new RuntimeException(MapUtil.get(UserKeys.USER_FAILED) + e.getMessage());
        }
    }


    @Override
    public int createBehaviorLog(String action,String args, boolean dataChanged,int menuId)
    {
        if(args != null){

            action = getAction(action,
                    StringUtils.isNotEmpty(args) ? StringUtils.split(args, ",") : null);

        }
        if (null != UserUtil.getCurrentUser())
        {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String sessionId = request.getRequestedSessionId();

            SysBehaviorLogModel model = new SysBehaviorLogModel();
            model.setAction(action);
            model.setSessionId(sessionId);
            model.setLoggedDate(new Date());
            model.setDataChanged(dataChanged?1:0);
            model.setMenuId(menuId);
            logMapper.insertBehaviorLog(model);
            return model.getBehaviorId();
        }

        return 0;
    }

    /**
     * 新建登录日志&更新用户列表
     * @param model
     * @return
     */
    @Override
    public int createLoginLog(LoginLogModel model) {
        CurrentUser currentUser = UserUtil.getCurrentUser();
        Date now = new Date();
        // 更新sys_user表用户上次登录信息
        Map<String, Object> map = new HashMap<String, Object>(NumberEnum.NUMBER_4.getNum());
        map.put(BeanProperty.UserBean.USER_ID, model.getUserId());
        map.put(BeanProperty.UserBean.LAST_LOGIN_DATE, now);
        map.put(BeanProperty.UserBean.LAST_LOGIN_IP, model.getIp());
        map.put(BeanProperty.UserBean.ERROR_TIME, 0);
        userMapper.editUsers(map);

        //新增sys_login_log表记录
        return logMapper.insertLoginLog(model);
    }

    /**
     * 更新登出时间
     * @param map
     */
    @Override
    public void updateLogoutDate(Map<String, Object> map) {
        logMapper.updateLogoutDate(map);
    }

    @Override
    public int createBehaviorLog(String action, boolean dataChanged) {
    /*    CurrentUser u = CurrentUserHolder.getCurrentUser();

        if (null != u) {
            String sessionId = CurrentUserHolder.getCurrentUser().getSessionId();

            Map<String, Object> map = new HashMap<String, Object>(4);
            map.put(LoginLog.SESSION_ID, sessionId);
            map.put(BehaviorLog.LOGGED_DATE, new Date());
            map.put(BehaviorLog.ACTION, action);
            map.put(BehaviorLog.DATA_CHANGED, dataChanged);
            logMapper.insertBehaviorLog(map);

            BigInteger re = (BigInteger) map.get(BehaviorLog.BEHAVIOR_ID);
            return  re.intValue();
        }*/

        return 0;
    }

    @Override
    public int getLoginBehaviorCountToday(Integer userId) {
        return logMapper.getLoginBehaviorCountToday(userId);
    }

    @Override
    public void addLoginBehavior(Integer userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("createdBy",userId);
        map.put("createdDate",new Date());
        if (logMapper.addLoginBehavior(map) == BeanProperty.Num.ZERO) {
            throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
    }

    @Override
    public void updateLoginBehavior(Integer userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("lastUpdatedBy",userId);
        map.put("lastUpdatedDate",new Date());
        if (logMapper.updateLoginBehavior(map) == BeanProperty.Num.ZERO) {
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }
    }

    public String getAction(String code, String... args)
    {
        String[] params = null;

        if (StringUtils.isEmpty(code))
        {
            return StringUtils.EMPTY;
        }

        if (null != args && args.length > 0)
        {
            List<String> list = new ArrayList<String>(2);

            for (String str : args)
            {
                list.add(messageSource.getMessage(str, null, Locale.CHINESE));
            }

            params = new String[list.size()];
            list.toArray(params);
        }

        return messageSource.getMessage(code, params, Locale.CHINESE);
    }
}
