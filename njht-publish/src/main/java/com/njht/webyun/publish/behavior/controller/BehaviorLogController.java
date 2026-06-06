package com.njht.webyun.publish.behavior.controller;

import com.njht.webyun.enums.ReturnCodeEnum;
import com.njht.webyun.publish.behavior.service.BehaviorDownloadLogService;
import com.njht.webyun.publish.behavior.service.BehaviorLogService;
import com.njht.webyun.publish.behavior.vo.CountInfoReqVo;
import com.njht.webyun.publish.behavior.vo.ProductCountReqVo;
import com.njht.webyun.publish.behavior.vo.StatisticSearchVo;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 
 * 系统操作日志，信息统计相关接口
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:55:20
 */
@RestController
@RequestMapping("statistics/log")
@Api(tags = "信息统计以及日志")
public class BehaviorLogController {
    @Autowired
    private BehaviorLogService behaviorLogService;

    @Autowired
    private BehaviorDownloadLogService behaviorDownloadLogService;

    /**
     * 列表
     */
    @ApiOperation(value = "用户行为信息列表", notes = "信息统计列表")
    @PostMapping("/list")
    @ResponseBody
    public ReturnT<PageUtils> list(@Validated  @RequestBody StatisticSearchVo statisticSearchVo){
        PageUtils pageUtils = behaviorLogService.queryPage(statisticSearchVo);
        return ReturnT.success(pageUtils);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "用户访问量，数据下载量，用户注册量统计", notes = "信息统计列表")
    @GetMapping("/statisticInfo")
    @ResponseBody
    public ReturnT<List<CountInfoReqVo>> statisticInfo(){
        List<CountInfoReqVo> count = behaviorDownloadLogService.getStatisticCountInfo();
        return ReturnT.success(count);
    }

    /**
     * 产品下载量统计
     */
    @ApiOperation(value = "产品下载量统计", notes = "信息统计列表")
    @GetMapping("/productCountList")
    @ResponseBody
    public ReturnT<List<ProductCountReqVo>> getProductCountInfoList(){
        List<ProductCountReqVo> list = behaviorDownloadLogService.queryProductCountInfoList();
        if(list == null ){
            return ReturnT.failed(ReturnCodeEnum.FAIL_CODE.getCode(),ReturnCodeEnum.FAIL_CODE.getMessage());
        }
        return ReturnT.success(list);
    }


}
