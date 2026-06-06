package com.njht.webyun.zuul.web;

import com.njht.webyun.utils.TokenUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @Author David
 * @Date 2020/12/28
 */

@RestController
@Api(tags = "心跳接口",value= "HeartBeatController")
public class HeartBeatController {

    /**
     * 心跳接口
     */
    @GetMapping("/system/heartbeat")
    public void heartBeat(HttpServletRequest request,HttpServletResponse response){
        String token = request.getHeader(TokenUtil.TOKEN);

        try {
            TokenUtil.parseToken(token);
            response.setHeader(TokenUtil.TOKEN,token);
        }
         catch (Exception ex) {

            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
         }

    }
}


