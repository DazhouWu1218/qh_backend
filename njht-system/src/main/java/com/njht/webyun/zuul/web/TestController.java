package com.njht.webyun.zuul.web;

import com.njht.webyun.zuul.token.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author David
 * @Date 2020/11/18
 */

@RestController
public class TestController {

    @Autowired
    TokenConfig tokenConfig;

    @GetMapping("/system/test")
    public String test(){



        return tokenConfig.getExpirationTime()+"";
    }


}
