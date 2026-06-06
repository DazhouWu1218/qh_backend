package com.htht.job.admin.dispatch.controller;

import com.htht.job.admin.dispatch.service.JobGroupService;
import com.njht.webyun.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 代国军
 * @description: 调度成功率统计
 * @date 2022/7/26 14:53
 */
@RestController
@RequestMapping("/registry")
public class JobRegistryController {

    @Autowired
    private JobGroupService jobGroupService;

    @GetMapping("/list")
    @ResponseBody
    public ReturnT<Map<String,Object>> list() {
        Map<String,Object> list = jobGroupService.addressInfo();
        return ReturnT.success(list);
    }

}
