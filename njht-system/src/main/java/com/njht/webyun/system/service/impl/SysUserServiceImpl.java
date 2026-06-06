package com.njht.webyun.system.service.impl;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.system.model.fn.AuthUser;
import com.njht.webyun.system.model.fn.ManagementResult;
import com.njht.webyun.system.service.inf.LoginService;
import com.njht.webyun.utils.FileUtil;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.config.UserLoginParam;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.constant.UserKeys;
import com.njht.webyun.system.dao.mapper.SysUserMapper;
import com.njht.webyun.system.model.base.BeanProperty.Bean;
import com.njht.webyun.system.model.base.BeanProperty.UserBean;
import com.njht.webyun.system.model.base.BeanProperty.OrgBean;
import com.njht.webyun.system.model.base.BeanProperty.Num;
import com.njht.webyun.system.model.base.LoginIndexResp;
import com.njht.webyun.system.model.sysRole.UserRoleQuery;
import com.njht.webyun.system.model.sysUser.*;
import com.njht.webyun.system.service.inf.SysUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @author David
 * @Time 2019年11月8日 上午10:50:43
 */
@Service
@Component
@Transactional
//@CacheConfig(cacheNames = "menus_cache")
public class SysUserServiceImpl implements SysUserService {

/*

    @Autowired
    RoleMapper roleMapper;*/

    @Autowired
    SysUserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserLoginParam userLoginParam;
    @Autowired
    LoginService loginService;

    @Value("${image.path1}")
    private String imagePath1;

    @Value("${image.path2}")
    private String imagePath2;


    /**
     * 查询所有用户，支持条件查询
     *
     * @param userQuery
     * @return
     */
    @Override
    public Page<UserResp> showUsers(UserQuery userQuery) throws Exception {
        PageHelper.startPage(userQuery.getPage(), userQuery.getRows());
        Page<UserResp> list = userMapper.getUsers(userQuery);
        return list;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Override
    public void addUsers(SysUser user) throws Exception {
        //1.查看是否有此用户名存在
        String username = StringUtils.trimAllWhitespace(user.getUsername());
        if (null != userMapper.selectUserByName(username)) {
//            throw new CommMsgException("用户名存在");
            throw new CommMsgException(MapUtil.get(UserKeys.USERNAME_EXISTS));
        }
        //2.判断是否设置密码，没有就给默认，给密码加密
        String password = user.getPassword();
        String pwd = "";
        if (StringUtils.isEmpty(password)) {
            pwd = passwordEncoder.encode(userLoginParam.getDefaultPassword());
        } else {
            pwd = passwordEncoder.encode(password);
        }
        //3.获取创建者信息
        Date now = new Date();
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        //4.封装其他信息
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put(UserBean.USERNAME, username);
        map.put(UserBean.REAL_NAME, StringUtils.trimAllWhitespace(user.getRealName()));
        map.put(UserBean.PWD, pwd);
        map.put(UserBean.ERROR_TIME, 0);
        map.put(OrgBean.ORG_ID, user.getOrgId());
        map.put(UserBean.PHONE, StringUtils.trimAllWhitespace(user.getPhone()));
        map.put(UserBean.MOBILE, StringUtils.trimAllWhitespace(user.getMobile()));
        map.put(UserBean.EMAIL, StringUtils.trimAllWhitespace(user.getEmail()));
        map.put(UserBean.ADDRESS, StringUtils.trimAllWhitespace(user.getAddress()));
        map.put(UserBean.REMARK, StringUtils.trimAllWhitespace(user.getRemark()));
        map.put(UserBean.LOCKED, user.isLocked());
        map.put(Bean.DELETED, false);
        map.put(Bean.CREATED_BY, currentUserId);
        map.put(Bean.CREATED_DATE, now);
        map.put(Bean.LAST_UPDATED_BY, currentUserId);
        map.put(Bean.LAST_UPDATED_DATE, now);

        int addNum = userMapper.addUsers(map);
        if (addNum == Num.ZERO) {
//            throw new CommMsgException("插入0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
    }

    /**
     * 编辑用户
     *
     * @param user
     * @return
     */
    @Override
    public void editUsers(SysUser user) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put(UserBean.USER_ID, user.getUserId());
        map.put(UserBean.REAL_NAME, StringUtils.trimAllWhitespace(user.getRealName()));
        map.put(UserBean.PHONE, StringUtils.trimAllWhitespace(user.getPhone()));
        map.put(UserBean.MOBILE, StringUtils.trimAllWhitespace(user.getMobile()));
        map.put(UserBean.EMAIL, StringUtils.trimAllWhitespace(user.getEmail()));
        map.put(UserBean.ADDRESS, StringUtils.trimAllWhitespace(user.getAddress()));
        map.put(UserBean.REMARK, StringUtils.trimAllWhitespace(user.getRemark()));
        map.put(Bean.LAST_UPDATED_BY, UserUtil.getCurrentUser().getUserId());
        map.put(Bean.LAST_UPDATED_DATE, new Date());
        map.put(OrgBean.ORG_ID, user.getOrgId());
        map.put("departmentId",user.getDepartmentId());

        int editNum = userMapper.editUsers(map);
        if (editNum == Num.ZERO) {
//            throw new CommMsgException("修改0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }
    }

    /**
     * 批量删除用户
     *
     * @param list
     * @return
     */
    @Override
    public void deleteUser(List<Integer> list) throws Exception {

        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Map<String, Object> map = null;
        for (int userId : list) {
            if(currentUserId == userId){
//                throw new CommMsgException("不能对本用户进行删除操作");
                throw new CommMsgException(MapUtil.get(CommonKey.DELETE_FAIL));
            }
            map = new HashMap<String, Object>(4);
            map.put(UserBean.USER_ID, userId);
            map.put(Bean.DELETED, true);
            map.put(Bean.LAST_UPDATED_BY, currentUserId);
            map.put(Bean.LAST_UPDATED_DATE, new Date());
            int deleteNum = userMapper.deleteUser(map);
            if (deleteNum == Num.ZERO) {
//                throw new CommMsgException("删除时存在无效用户ID");
                throw new CommMsgException(MapUtil.get(CommonKey.DELETE_FAIL));
            }
        }
    }

    /**
     * 重置用户密码，支持批量重置
     *
     * @param list
     * @return
     */
    @Override
    public void resetPassword(List<ResetPasswordModel> list) throws Exception {

        int currentUserId = UserUtil.getCurrentUser().getUserId();
        String pwd = null;
        Map<String, Object> map = new HashMap<String, Object>(4);
        for (ResetPasswordModel model : list) {
            if(currentUserId == model.getUserId()){
//                throw new CommMsgException("不能对本用户进行重置密码操作");
                throw new CommMsgException(MapUtil.get(UserKeys.RESET_PASSWORD)+MapUtil.get(CommonKey.FAIL));
            }
            pwd = passwordEncoder.encode(userLoginParam.getDefaultPassword());
            map.put(UserBean.USER_ID, model.getUserId());
            map.put(UserBean.PWD, pwd);
            map.put(Bean.LAST_UPDATED_BY, currentUserId);
            map.put(Bean.LAST_UPDATED_DATE, new Date());

            int updateNum = userMapper.updateUserPassword(map);
            if (updateNum == Num.ZERO) {
//                throw new CommMsgException("重置用户密码失败：重置时存在无效用户ID");
                throw new CommMsgException(MapUtil.get(UserKeys.RESET_PASSWORD)+MapUtil.get(CommonKey.FAIL));
            }
        }
    }

    @Override
    public int lockUsers(UserLockedModel model) throws Exception {
        Map<String, Object> map = null;
        boolean locked = model.isLocked();
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        for (int userId : model.getList()) {
            if(currentUserId == userId){
//                throw new CommMsgException("不能对本用户进行锁定或解锁操作");
                throw new CommMsgException(MapUtil.get(UserKeys.CANNOT_BE_LOCK_UNLOCK));
            }
            map = new HashMap<String, Object>(4);
            map.put(UserBean.USER_ID, userId);
            map.put(UserBean.LOCKED, locked);
            map.put(Bean.LAST_UPDATED_BY, UserUtil.getCurrentUser().getUserId());
            map.put(Bean.LAST_UPDATED_DATE, new Date());

            int updateNum = userMapper.updateUserLocked(map);
            if (updateNum == Num.ZERO) {
                return updateNum;
            }
        }
        return Num.ONE;
    }

    /**
     * 修改密码
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        Integer userId = UserUtil.getCurrentUser().getUserId();
//          1.验证新旧密码不能相同
        if (oldPassword.equals(newPassword)) {
//            throw new CommMsgException("新密码与旧密码相同");
            throw new CommMsgException(MapUtil.get(UserKeys.PWD_SAME));
        }
//          2.验证旧密码是否正确
        Map<String, Object> queryMap = new HashMap<String, Object>(2);
        queryMap.put("userId", userId);
        queryMap.put("recentTimes", userLoginParam.getRecentTimes());
        List<Map<String, Object>> historyPwdList = userMapper.queryHistoryPassword(queryMap);
        if (historyPwdList == null) {
//            throw new CommMsgException("用户历史密码查询异常");
            throw new CommMsgException(MapUtil.get(UserKeys.HISTORY_PASSWORD_QUERY_UNNORMAL));
        }
        SysUser user = userMapper.queryByUserId(UserUtil.getCurrentUser().getUserId());
        boolean matches = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!matches && historyPwdList.size() > 0) {
//            throw new CommMsgException("旧密码输入有误");
            throw new CommMsgException(MapUtil.get(UserKeys.OLD_PWD_ERROR));
        }
//          3.验证新密码是否在最近几次中使用过
        if (userLoginParam.isDiffWithPrePwd()) {

            boolean isSame = false;
            for (Map<String, Object> map2 : historyPwdList) {
                String hisPwd = (String) map2.get("HISTORY_PWD");
                if (hisPwd.equals(newPassword)) {
                    isSame = true;
                    break;
                }
            }

            if (isSame) {
//                throw new CommMsgException("新密码不能与最近" + userLoginParam.getRecentTimes() + "次使用的密码相同");
                throw new CommMsgException(MapUtil.get(UserKeys.PWD_SAME_TIMES));
            }

        }
//          4.修改用户密码		/*没有开启相同校验，或者开启了但是与之前密码不相同，则直接更新*/
        Map<String, Object> map = new HashMap<>();
        map.put(UserBean.USER_ID, userId);
        map.put(UserBean.PWD, passwordEncoder.encode(newPassword));
        map.put(Bean.LAST_UPDATED_BY, userId);
        map.put(Bean.LAST_UPDATED_DATE, new Date());
        userMapper.updateUserPassword(map);

//          5.插入用户旧密码的记录表 // 不管是否开启强制修改密码，都将老密码进行存储
        map.clear();
        map.put("userId", userId);
        map.put("historyPwd", oldPassword);
        map.put("lastUpdateDate", new Date());
        userMapper.insertHistoryPassword(map);

    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser getUserinfoByUsername(String username) {
        return userMapper.selectUserByName(username);
    }

    /**
     * 根据角色id查询用户
     * 支持分页查询，另：用户名模糊、机构id、是否包含子机构、去除不包含的userId条件查询 等
     *
     * @param userRoleQuery
     * @return
     * @throws Exception
     */
    @Override
    public Page<UserResp> getUsersByRoleId(UserRoleQuery userRoleQuery) throws Exception {
        PageHelper.startPage(userRoleQuery.getPage(), userRoleQuery.getRows());
        Page<UserResp> list = null;
        //判断查询时是否排除指定用户ID
        //根据角色id来查
        if (!StringUtils.isEmpty(userRoleQuery.getRoleId())) {
            //这是来查询该角色的用户信息
            userRoleQuery.setOrgId(UserUtil.getCurrentUser().getOrgId());
            list = userMapper.getUsersByRoleId(userRoleQuery);
        } else {
            //查询未添加的用户信息，支持分页和多条件
            list = userMapper.getUsersWhetherExcepte(userRoleQuery);
        }
        return list;
    }

    /**
     * Description: 检验是否需要强制修改过期密码,如果不配置，默认不修改
     */
    @Override
    public void checkPasswordChange(LoginIndexResp loginIndexResp) {
        boolean need = false;
        if (!userLoginParam.isConfigNeed()) {
            loginIndexResp.setNeedChangePwd(userLoginParam.isConfigNeed());
        }
        // 第一次登录的话，必须要修改，历史记录表里面查询是否有修改记录
        Map<String, Object> queryMap = new HashMap<String, Object>(2);
        queryMap.put("userId", loginIndexResp.getUserId());
        queryMap.put("recentTimes", userLoginParam.getRecentTimes());
        List<Map<String, Object>> historyPwdList = userMapper.queryHistoryPassword(queryMap);
        if (historyPwdList.isEmpty() || !(historyPwdList.size() > 0)) {
            loginIndexResp.setNeedChangePwd(true);
            loginIndexResp.setChangeReason("首次登陆，请修改密码");
        } else {

            Date lastChangeDate = (Date) historyPwdList.get(0).get("LAST_UPDATE_DATE");
            Date nextChangeDate = null;
            String unitType = "";
            Date now = new Date();

            switch (userLoginParam.getUnit()) {
                case 0:
                    nextChangeDate = DateUtils.addYears(lastChangeDate, userLoginParam.getInterval());
                    unitType = "年";
                    break;
                case 1:
                    nextChangeDate = DateUtils.addMonths(lastChangeDate, userLoginParam.getInterval());
                    unitType = "月";
                    break;
                case 2:
                    nextChangeDate = DateUtils.addDays(lastChangeDate, userLoginParam.getInterval());
                    unitType = "天";
                    break;
                case 3:
                    nextChangeDate = DateUtils.addHours(lastChangeDate, userLoginParam.getInterval());
                    unitType = "小时";
                    break;
                case 4:
                    nextChangeDate = DateUtils.addMinutes(lastChangeDate, userLoginParam.getInterval());
                    unitType = "分钟";
                    break;
                default:
                    break;
            }

            // 如果当前时间大于下次需要修改的时间，则需要修改，否则不需要修改
            if (now.compareTo(nextChangeDate) >= 0) {
                loginIndexResp.setNeedChangePwd(true);
                loginIndexResp.setChangeReason("密码已使用满" + userLoginParam.getInterval() + unitType + "，自动过期，请修改密码");
            }
        }


    }

    @Override
    public void updateByUserSelf(Integer userId, MultipartFile picture) throws Exception {
        String fileName = FileUtil.multipartFileToFileReturnFileName(picture, new File(imagePath1+imagePath2).getAbsolutePath()+File.separator);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("userPicData",imagePath2+File.separator+fileName);
        int editNum = userMapper.updateByUserSelf(map);
        if (editNum == Num.ZERO) {
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }
    }

    @Override
    public SysUser queryByUserId(int userId) throws Exception {
        return userMapper.queryByUserId(userId);
    }

    /**
     *    (描述):
     * <p>(说明):      更新用户密码错误次数
     * <p>     	     @param map
     * <p>date:      2019年4月10日
     * <p>createdBy: guoy
     */
    @Override
    public void updateUserErrorInfo(Map<String, Object> map) {
        userMapper.updateUserErrorInfo(map);
    }

    /** (non-Javadoc)
     * <p>Description: 登录错误超过一定次数，锁定用户
     */
    @Override
    public void lockUserByLoginError(Integer userId) {
        HashMap<String, Object> map = new HashMap<String, Object>(4);

        map.put(UserBean.USER_ID, userId);
        map.put(UserBean.LOCKED, true);
        map.put(Bean.LAST_UPDATED_BY, userId);
        map.put(Bean.LAST_UPDATED_DATE, new Date());

        userMapper.updateUserLocked(map);
    }

    /**
     * 用户密码至少包含大写字母、小写字母、数字、特殊字符中的 3类且至少8位，否则必须要修改
     * @param pwd
     * @return
     */
    @Override
    public boolean checkPwcMatch(String pwd){
        boolean need = false;
        // 读取配置文件，检查是否需要修改，默认不修改
        Boolean configNeed = userLoginParam.isConfigNeed();
        if(configNeed){
            int result1 = pwd.matches(".*[a-z]{1,}.*") == true ? 1 : 0;
            int result2 = pwd.matches(".*[A-Z]{1,}.*") == true ? 1 : 0;
            int result3 = pwd.matches(".*\\d{1,}.*")== true ? 1 : 0;
            int result4 = pwd.matches(".*[~!@#$^&*()_?]{1,}.*")== true ? 1 : 0;
            int sumary = result1 + result2 + result3 + result4;
            if(sumary < 3 || pwd.length()<8){
                need = true;
            }
        }
        return need;
    }

    @Override
    public List<CurrentUser> userIdList() {
        return userMapper.userIdList();
    }


}
