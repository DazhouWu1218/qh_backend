package com.njht.webyun.system.service.impl;

import com.njht.webyun.system.model.fn.AuthUser;
import com.njht.webyun.system.model.fn.ManagementResult;
import com.njht.webyun.system.service.fn.ManagementFnService;
import com.njht.webyun.system.service.inf.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    ManagementFnService managementLoginFnService;

    @Override
    public ManagementResult loginManagement(String username, String password) {

        try{
            ManagementResult managementResult = managementLoginFnService.managementLogin(username,password);
            if (managementResult.getCode() != 200){
                logger.info("Fegin远程调用数管登陆接口错误：{}",managementResult.toString());
                return null;
            }
            logger.info("Fegin远程调用数管登陆接口成功：{}",managementResult.toString());
            return managementResult;
        }catch(Exception e){
            logger.error("Fegin远程调用数管登陆接口失败：{}",e.getMessage());
        }

        return null;
    }

    @Override
    public ManagementResult userRegisterManagement(AuthUser authUser) {
        try{
            ManagementResult managementResult = managementLoginFnService.managementUserRegister(authUser);
            if (managementResult.getCode() != 200){
                logger.info("Fegin远程调用数管用户注册接口错误：{}",managementResult.toString());
                return null;
            }
            logger.info("Fegin远程调用数管用户注册接口成功：{}",managementResult.toString());
            return managementResult;
        }catch(Exception e){
            logger.error("Fegin远程调用数管用户接口接口失败：{}",e.getMessage());
        }

        return null;
    }


}
