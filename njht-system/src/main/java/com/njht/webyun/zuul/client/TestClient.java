package com.njht.webyun.zuul.client;

import com.njht.webyun.system.model.sysUser.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @Author David
 * @Date 2020/11/19
 */


@FeignClient("test-service")
public interface TestClient {

    @GetMapping("/getUserByName")
    SysUser getUserByName(@RequestParam("userName") String name);

}
