package com.njht.webyun.zuul.client;

import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.njht.webyun.system.model.sysUser.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * @Author David
 * @Date 2020/11/19
 */


@FeignClient("system-service")
public interface SystemClient {

    @GetMapping("/system/user/getUserByName")
    SysUser getUserByName(@RequestParam("userName") String name);

    @PostMapping("/system/user/updateerrorinfo")
    SysUser updateUserErrorInfo(@RequestBody Map<String, Object> map);

    @PostMapping("/system/user/lock")
    SysUser lockUserByLoginError(@RequestParam("userId") int userId);

    @GetMapping("/system/url/selectUrlsAndRole")
    List<SysUrl> selectUrlsAndRole();

    @GetMapping("/system/logs/createBehaviorLog")
    int createBehaviorLog(@RequestParam("action") String action,
                          @RequestParam("args") String args, @RequestParam("dataChanged") boolean dataChanged);
}
