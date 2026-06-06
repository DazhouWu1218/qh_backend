package com.htht.job.admin.dispatch.controller;

import com.htht.job.admin.dispatch.core.conf.XxlJobAdminConfig;
import com.htht.job.core.biz.AdminBiz;
import com.htht.job.core.biz.model.HandleCallbackParam;
import com.htht.job.core.biz.model.RegistryParam;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.util.XxlJobRemotingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by piesat on 17/5/10.
 * 执行器注册或者回调
 */
@Controller
@RequestMapping("/api")
public class JobApiController {

    @Resource
    private AdminBiz adminBiz;

    @PostMapping("/callback")
    @ResponseBody
    public ReturnT<String> api(HttpServletRequest request, @RequestBody(required = false) List<HandleCallbackParam> callbackParamList) {
        if (XxlJobAdminConfig.getAdminConfig().getAccessToken()!=null
                && XxlJobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !XxlJobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN))) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }
        return adminBiz.callback(callbackParamList);
    }

    /**
     * api
     * @param uri
     * @param registryParam
     * @return
     */
    @RequestMapping("/{uri}")
    @ResponseBody
    public ReturnT<String> api(HttpServletRequest request, @PathVariable("uri") String uri, @RequestBody(required = false) RegistryParam registryParam) {

        // valid
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "invalid request, HttpMethod not support.");
        }
        if (uri==null || uri.trim().length()==0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "invalid request, uri-mapping empty.");
        }
        if (XxlJobAdminConfig.getAdminConfig().getAccessToken()!=null
                && XxlJobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !XxlJobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN))) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // services mapping
       if ("registry".equals(uri)) {
            return adminBiz.registry(registryParam);
        } else if ("registryRemove".equals(uri)) {
            return adminBiz.registryRemove(registryParam);
        } else {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping("+ uri +") not found.");
        }

    }

    @GetMapping("test")
    @ResponseBody
    public String test(@RequestBody(required = false) String data){
        return "12346";
    }
}
