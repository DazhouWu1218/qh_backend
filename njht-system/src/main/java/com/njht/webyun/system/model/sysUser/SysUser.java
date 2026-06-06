package com.njht.webyun.system.model.sysUser;

import com.njht.webyun.model.CurrentUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author David
 * @date 2019-11-01
 */
@ApiModel(value="SysUser",description="系统用户")
public class SysUser extends CurrentUser {
}