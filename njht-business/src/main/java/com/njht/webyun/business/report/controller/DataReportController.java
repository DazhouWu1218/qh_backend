package com.njht.webyun.business.report.controller;

import com.njht.webyun.business.report.service.DataBaseInfoService;
import com.njht.webyun.business.report.service.DataReportService;
import com.njht.webyun.business.report.vo.DataDetailReportVo;
import com.njht.webyun.business.report.vo.DataReportReqVo;
import com.njht.webyun.business.report.vo.ProductReportVo;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 数据监控基础信息
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-08-16 15:49:24
 */
@RestController
@RequestMapping("report")
@Api(tags = "数据监控")
public class DataReportController {
    @Autowired
    private DataBaseInfoService dataBaseInfoService;

    @Autowired
    private DataReportService dataReportService;


    @ApiOperation("数据存储监控-数据集合")
    @ApiImplicitParam(paramType="String", name="type", dataType="String", required=true, value="查询周期类型（today weekly monthly，默认 today")
    @PostMapping("dataList")
    public ReturnT<List<DataReportReqVo>> dataList(@RequestParam("type")String type){
        List<DataReportReqVo> dataReportReqVos = dataBaseInfoService.queryNameList(type);
        return ReturnT.success(dataReportReqVos);
    }


    @ApiOperation("根据数据id 查询数据情况")
    @PostMapping("dataDetailList")
    public ReturnT<PageUtils> dataDetailList(@Validated @RequestBody DataDetailReportVo dataDetailReportVo){
        PageUtils pageUtils = dataReportService.queryDataReportList(dataDetailReportVo);
        return ReturnT.success(pageUtils);
    }

    @ApiOperation("生产监控")
    @PostMapping("dataProducerList")
    public ReturnT<PageUtils> dataProducerList(@Validated @RequestBody ProductReportVo reportVo){
        PageUtils pageUtils = dataReportService.queryDataProducerList(reportVo);
        return ReturnT.success(pageUtils);
    }

    @ApiOperation("产品列表")
    @GetMapping("productList")
    public ReturnT<List<CommonEntity>> productList(){
        List<CommonEntity> list = dataReportService.queryProductList();
        return ReturnT.success(list);
    }
}
