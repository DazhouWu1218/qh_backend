package com.njht.webyun.publish.index.controller;

import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.publish.index.service.IndexService;
import com.njht.webyun.publish.index.vo.IndexReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 首页控制层
 */
@RestController
@RequestMapping("/index")
@Api(value = "", tags = "首页")
public class IndexController {

    @Autowired
    private IndexService indexService;


    @ApiOperation(value = "首页基本信息", notes = "首页标题信息")
    @GetMapping({"/info"})
    @ResponseBody
    public ReturnT<IndexReqVo> getIndexBaseInfo(){
        IndexReqVo reqVo = indexService.getIndexInfo();
        return ReturnT.success(reqVo);
    }

}
