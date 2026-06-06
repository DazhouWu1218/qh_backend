package com.njht.webyun.system.controller;

import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.njht.webyun.system.model.sysUrl.SysUrlQuery;
import com.njht.webyun.system.model.sysUrl.SysUrlResp;
import com.njht.webyun.system.service.inf.SysUrlService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "URL管理",value= "urlmanger")
@RestController
@RequestMapping(value = "/system/url")
public class SysUrlController {

    @Autowired
    SysUrlService urlService;

    private final static Logger logger = LoggerFactory.getLogger(SysUrlController.class);


    @PostMapping(value = "/show")
    @ApiOperation(value="查询URL列表，支持分页")
    public Page<SysUrlResp> getUrls(@RequestBody SysUrlQuery sysUrlQuery) throws Exception{
        return urlService.showSysUrl(sysUrlQuery);
    }

    @PostMapping("/add")
    @ApiOperation("新增URL")
    public void addUrl(@RequestBody SysUrl sysUrl )throws Exception{
       urlService.addUrl(sysUrl);
    }

    @PostMapping("/edit")
    @ApiOperation("编辑URL")
    public void editUrl(@RequestBody SysUrl sysUrl )throws Exception{
        urlService.editUrl(sysUrl);
    }

    @PostMapping("/delete")
    @ApiOperation("批量删除URL")
    public void deleteUrl(@RequestBody List<Integer> list )throws Exception{
        urlService.deleteUser(list);
    }

}
