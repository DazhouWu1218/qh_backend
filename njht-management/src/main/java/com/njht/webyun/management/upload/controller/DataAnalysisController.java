package com.njht.webyun.management.upload.controller;


import com.njht.webyun.management.upload.dto.BusinessStatisticDTO;
import com.njht.webyun.management.upload.dto.DiskCapacityDTO;
import com.njht.webyun.management.upload.service.DataAnalysisService;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/23 10:17
 * @Description: 数据管理 数据统计
 */
@RestController
@RequestMapping("analysis")
@Api(tags = "数据管理 数据统计")
public class DataAnalysisController {

    @Autowired
    private DataAnalysisService dataAnalysisService;



    /**
     * 磁盘容量情况统计
     * @return
     */
    @ApiOperation(value = "磁盘容量统计")
    @GetMapping("/diskCapacity")
    public ReturnT<Object> dataUploadTree(){
        DiskCapacityDTO diskCapacityDTO = dataAnalysisService.getDiskCapacity();
        return ReturnT.success(diskCapacityDTO);
    }



    /**
     * 业务产品生产模块统计
     * @return
     */
    @ApiOperation(value = "业务模块生产")
    @GetMapping("/getBusinessStatisticInfo")
    public ReturnT<Object> getBusinessStatisticInfo(){
        List<BusinessStatisticDTO> businessStatisticDTOList = dataAnalysisService.getBusinessStatisticInfo();
        return ReturnT.success(businessStatisticDTOList);
    }

}
