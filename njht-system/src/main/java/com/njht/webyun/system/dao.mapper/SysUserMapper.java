package com.njht.webyun.system.dao.mapper;

import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.system.model.sysRole.UserRoleQuery;
import com.njht.webyun.system.model.sysUser.SysUser;
import com.njht.webyun.system.model.sysUser.UserQuery;
import com.njht.webyun.system.model.sysUser.UserResp;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface SysUserMapper {

    CurrentUser queryByName(String name);
    
    Page<UserResp> getUsers(UserQuery userQuery);

    int addUsers(Map<String, Object> map);

    int editUsers(Map<String, Object> map);

    int deleteUser(Map<String, Object> map);

    SysUser  selectUserByName(String username);

    int updateUserPassword(Map<String, Object> map);

    int insertHistoryPassword(Map<String, Object> map);

    int updateUserLocked(Map<String, Object> map);

    Page<UserResp> getUsersByRoleId(UserRoleQuery userRoleQuery);

    Page<UserResp> getUsersWhetherExcepte(UserRoleQuery userRoleQuery);

    List<Map<String, Object>> queryHistoryPassword(Map<String, Object> map);

    int updateByUserSelf(Map<String,Object> map);

    SysUser queryByUserId(int id);

    void updateUserErrorInfo(Map<String, Object> map);

    List<Map<String, Object>> selectUsersByRoleId(int roleId);

    Page<List<Map<String, Object>>> selectUsersExceptByPage(Map<String, Object> map);

    List<CurrentUser> userIdList();

}