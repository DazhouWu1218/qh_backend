package com.njht.webyun.business.index.contorller;

import com.njht.entity.xxljob.CountInfo;
import com.njht.webyun.business.index.service.AlgorithmService;
import com.njht.webyun.business.index.service.IndexService;
import com.njht.webyun.business.index.service.RegistryService;
import com.njht.webyun.business.index.vo.*;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/6 17:03
 * @Description: 首页
 */
@RestController
@RequestMapping("/index")
@Api(tags = "首页")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private RegistryService registryService;

    @Autowired
    private AlgorithmService algorithmService;

    /**
     * 首页信息统计
     */
    @ApiOperation(value = "首页信息统计")
    @GetMapping({"/count"})
    public ReturnT<CountInfo> count(){
        CountInfo count = indexService.count();
        return ReturnT.success(count);
    }


    /**
     * 产品监控（查询周期类型（1-本日，2-本周，3-本月，默认是1-本日））
     * 只统计调度成功的任务(调度失败的任务不进行统计)
     */
    @ApiOperation(value = "任务监控")
    @ApiImplicitParam(paramType="String", name="type", dataType="List", required=true, value="查询周期类型（today weekly monthly，默认 today")
    @PostMapping({"/monitor"})
    @ResponseBody
    public ReturnT<List<MonitorReqVo>> monitor(@RequestParam("type")String type){
        List<MonitorReqVo>  reqVoList = indexService.queryProductMonitorByType(type);
        return ReturnT.success(reqVoList);
    }

    @ApiOperation(value = "服务器详情列表")
    @GetMapping({"/serverList"})
    public ReturnT<List<ServerReqVo>> serverList(){
        List<ServerReqVo> reqVoList = registryService.serverList();
        return ReturnT.success(reqVoList);
    }

    @ApiOperation(value = "根据运行状态和IP查询节点状态以及节点绑定算法")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="Integer", name="isRun", dataType="Integer", required=false, value="服务器状态（-1 全部,0正常，1异常）"),
            @ApiImplicitParam(paramType="String", name="ip", dataType="String", required=false, value="ip,模糊搜索")
    })
    @PostMapping({"/serverDetailList"})
    public ReturnT<List<ServerDetailReqVo>> getServerList(@RequestParam(value = "isRun",required = false,defaultValue = "-1") Integer isRun,
                                                          @RequestParam(value = "ip",required = false)String ip){
        List<ServerDetailReqVo> reqVoList = registryService.getServerListByIpOrRunStatus(Long.valueOf(isRun),ip);
        return ReturnT.success(reqVoList);
    }

    @ApiOperation(value = "首页算法库")
    @GetMapping({"/algorithmCountInfo"})
    public ReturnT<List<AlgorithmCountReqVo>> algorithmCountInfo(){
        return ReturnT.success(algorithmService.algorithmCountInfo());
    }

    @ApiOperation(value = "算法详情")
    @PostMapping({"/algorithmDetailInfo"})
    public ReturnT<PageUtils> algorithmDetailInfo(@Validated  @RequestBody AlgorithmVo algorithmVo){
        PageUtils pageUtils = algorithmService.algorithmDetailInfo(algorithmVo);
        return ReturnT.success(pageUtils);
    }

}
