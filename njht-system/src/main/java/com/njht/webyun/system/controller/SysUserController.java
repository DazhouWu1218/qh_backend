package com.njht.webyun.system.controller;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.constant.UserKeys;
import com.njht.webyun.system.model.base.LoginIndexResp;
import com.njht.webyun.system.model.sysRole.UserRoleQuery;
import com.njht.webyun.system.model.sysUser.*;
import com.njht.webyun.system.service.inf.SysUserService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户管理",value= "usermanger")
@RestController
@RequestMapping(value="/system/user/")
public class SysUserController {
	/**
	 * 1.分页查询
	 * 2.新增
	 * 3.编辑
	 * 4.删除，批量删除
	 * 5.修改密码
	 * 6.重置密码
	 * 7.锁定用户
	 * 8.查询除开某条件的用户
	 * 9.记录输错密码的次数
	 */

	@Value("${default.heartbeat.interval:#{60}}")
	protected int heartbeatInterval;

	@Autowired
	SysUserService userService;

	private final static Logger logger = LoggerFactory.getLogger(SysUserController.class);

	@PostMapping("/index")
	@ApiOperation("登录成功后的校验")
	public LoginIndexResp index(){
		LoginIndexResp loginIndexResp = new LoginIndexResp();
		userService.checkPasswordChange(loginIndexResp);
		loginIndexResp.setHeartbeatInterval(heartbeatInterval * 1000);
		return loginIndexResp;
	}

/*    @PostMapping("/login")
    @ApiOperation("用户登录")
    public boolean login(){
        return true;
    }*/

    @PostMapping("/show")
    @ApiOperation("分页查询用户")
    public Page<UserResp> getUsers(@RequestBody UserQuery userQuery)throws Exception{
		return userService.showUsers(userQuery);
    }

    @PostMapping("/add")
    @ApiOperation("新增用户")
    public void addUser(@RequestBody SysUser user ) throws Exception{
		userService.addUsers(user);
    }

    @PostMapping("/edit")
    @ApiOperation("编辑用户")
    public void editUser(@RequestBody SysUser user ) throws Exception{
		 userService.editUsers(user);
    }

    @PostMapping("/delete")
    @ApiOperation("批量删除用户")
    public void deleteUser(@RequestBody List<Integer> list ) throws Exception{
		userService.deleteUser(list);
    }

    @PostMapping("/resetpwd")
    @ApiOperation("批量重置用户密码")
    public void resetPassword(@RequestBody List<ResetPasswordModel> list) throws Exception{
		userService.resetPassword(list);
    }

    @PostMapping("/locked")
    @ApiOperation("批量锁定与解锁用户")
    public void lockUsers(@RequestBody UserLockedModel model) throws Exception{
		if(0 == userService.lockUsers(model)){
			logger.error("锁定解锁用户失败：锁定时存在无效用户ID");
//				return RespBean.error("锁定解锁时存在无效用户ID");
			throw new CommMsgException(MapUtil.get(UserKeys.USER_HAVE_LOSE_EFFICACY));
		}
    }
    
/*    @PostMapping("/unlocked")
    @ApiOperation("批量解锁用户")
    public RespBean unlockUsers(@RequestBody List<Integer> list)
    {
        try {
        	int retNum = userService.lockUsers(list,false);
			if(0 == retNum){
				logger.error("解锁用户失败：解锁时存在无效用户ID");
				return RespBean.error("解锁时存在无效用户ID");
			}
		} catch (Exception e) {
			logger.error("解锁用户失败："+e.getMessage());
			return RespBean.error("解锁用户失败");
		}
        return RespBean.ok("OK");
    }*/

    @PostMapping(value = "/editpwd")
    @ApiOperation("修改用户密码")
    public void editUserPassword(@ApiParam("oldPassword") String oldPassword, @ApiParam("newPassword")String newPassword) throws Exception{
//    	生产环境，密码是不能输出日志的
		userService.changePassword(oldPassword, newPassword);
    }

	/**
	 * 用于 角色添加用户  将原来系统的两个接口 整合成一个接口
	 * @param userRoleQuery
	 * @return
	 */
	@PostMapping("/userRole")
	@ApiOperation("查询角色用户（有roleId就是查已添加的用户）")
	public Page<UserResp> getUsersByRoleId(@RequestBody UserRoleQuery userRoleQuery) throws Exception{
		Page<UserResp> list = userService.getUsersByRoleId(userRoleQuery);
		if(list == null){
			logger.error("暂未查询到已添加的用户");
			throw new CommMsgException(MapUtil.get(UserKeys.USER_ADD_QUERY_FAIL));
		}
		return list;
	}

//	@PostMapping("/updateByUserSelf")
//	@ApiOperation("用户设定修改自己头像或姓名")
//	public SysUser updateByUserSelf(@RequestBody SysUser user) throws Exception{
//		SysUser userUpdate = new SysUser();
//		int retNum1 = userService.updateByUserSelf(user);
//		if(retNum1==0){
//			logger.error("修改自己头像或姓名：更新了0条记录");
////				return RespBean.error("修改自己头像或姓名失败");
//			throw new CommMsgException(MapUtil.get(UserKeys.USER_EDIT_AVATAR_NAME_FAIL));
//		}else if(retNum1==1){
//			userUpdate = userService.queryByUserId(user.getUserId());
//		}
//		return userUpdate;
//	}

	@PostMapping("/updateByUserSelf")
	@ApiOperation("用户设定修改自己头像或姓名")
	public void updateByUserSelf(@RequestParam("userId") Integer userId,@RequestParam("picture") MultipartFile picture) throws Exception{
		userService.updateByUserSelf(userId,picture);
	}

	@GetMapping("/getCurrentUser")
	public CurrentUser getCurrentUser(){
		return UserUtil.getCurrentUser();
	}

	@GetMapping("/queryByUserId")
	@ApiOperation("根据用户ID获取用户信息")
	public SysUser getUserById(@Param("userId") int userId) throws Exception{
		return userService.queryByUserId(userId);
	}

	@PostMapping("/lockUserByLoginError")
	@ApiOperation("登录错误超过一定次数，锁定用户")
	public void lockUserByLoginError(@Param("userId") int userId){
		userService.lockUserByLoginError(userId);
	}

	@PostMapping("/updateUserErrorInfo")
	@ApiOperation("更新用户密码错误信息")
	public void updateUserErrorInfo(@Param("userId") int userId,@Param("errorTime") int errorTime){
		Map<String, Object> paraMap = new HashMap<String, Object>(2);
		paraMap.put("userId", userId);
		paraMap.put("errorTime", errorTime);
		userService.updateUserErrorInfo(paraMap);
	}


	@GetMapping("/queryUserByName")
	@ApiOperation("通过用户名称查询用户信息")
	public SysUser queryUserByName(@Param("userName") String userName) throws Exception{
		return userService.getUserinfoByUsername(userName);
	}

	@GetMapping("/testInt")
	public Object seleteTestInt(){
		return 1+1;
	}

	@GetMapping("/testString")
	public String seleteTestString(){
		return "【is test msg········】";
	}

	@GetMapping("/getUserIdList")
	public List<CurrentUser> getUserIdList(){

		return userService.userIdList();
	}

	@GetMapping("/getSecurityUser")
	public CurrentUser  getSecurityUser(){

		Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CurrentUser user = null;
		if(principal instanceof CurrentUser){
			user = (CurrentUser)principal;
		}
		return user;
	}
}
