package com.njht.webyun.system.service.fn;


import com.njht.webyun.system.model.fn.AuthUser;
import com.njht.webyun.system.model.fn.ManagementResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="njht-management")
public interface ManagementFnService {

    @RequestMapping(value="/uus/system/login",method = RequestMethod.POST)
    ManagementResult managementLogin(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password);

    @RequestMapping(value="/uus/system/register",method = RequestMethod.POST)
    ManagementResult managementUserRegister(@RequestBody AuthUser authUser);

}
