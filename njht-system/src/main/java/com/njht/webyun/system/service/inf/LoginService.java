package com.njht.webyun.system.service.inf;

import com.njht.webyun.system.model.fn.AuthUser;
import com.njht.webyun.system.model.fn.ManagementResult;

public interface LoginService {

    /**
     *
     * @description 同步登陆接口
     * @param
     * @return
     * @author David
     * @date 2022/1/5 11:43
     */
    ManagementResult loginManagement(String username,String password);


    ManagementResult userRegisterManagement(AuthUser authUser);
}
