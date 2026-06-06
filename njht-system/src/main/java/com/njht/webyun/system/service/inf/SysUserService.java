package com.njht.webyun.system.service.inf;


import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.system.model.base.LoginIndexResp;
import com.njht.webyun.system.model.sysRole.UserRoleQuery;
import com.github.pagehelper.Page;
import com.njht.webyun.system.model.sysUser.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface SysUserService {

	public Page<UserResp> showUsers(UserQuery userQuery) throws Exception;

	public void addUsers(SysUser user) throws Exception;

	public void editUsers(SysUser user) throws Exception;

	public void deleteUser(List<Integer> list) throws Exception;

	public void resetPassword(List<ResetPasswordModel> list) throws Exception ;

    public int lockUsers(UserLockedModel model) throws Exception;

    void changePassword(String oldPassword, String newPassword) throws Exception;

	public Page<UserResp> getUsersByRoleId(UserRoleQuery userRoleQuery) throws Exception;

	public void checkPasswordChange(LoginIndexResp loginIndexResp);

	void updateByUserSelf(Integer userId, MultipartFile picture) throws Exception;

	public SysUser queryByUserId(int userId) throws Exception;

	public SysUser getUserinfoByUsername(String username) throws Exception;

	void updateUserErrorInfo(Map<String, Object> map);

	void lockUserByLoginError(Integer userId);

	/**
	 * 密码的规则校验 20200922
	 * @param pwd
	 * @return
	 */
	boolean checkPwcMatch(String pwd);

	public List<CurrentUser> userIdList();
}
