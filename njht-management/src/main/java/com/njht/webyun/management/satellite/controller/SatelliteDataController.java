package com.njht.webyun.management.satellite.controller;


import com.njht.webyun.management.common.service.CommonService;
import com.njht.webyun.management.satellite.constant.SatelliteConstant;
import com.njht.webyun.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 代国军
 * @CreateDate: 2021/4/13 14:42
 * @Description: 卫星遥感专题数据
 */
@RestController
@RequestMapping("satellite")
public class SatelliteDataController {


    @Autowired
    private CommonService commonService;

    @GetMapping("getDataTree")
    @ResponseBody
    public ReturnT<Object> getServiceTree(){
        return  commonService.getProductTree(SatelliteConstant.SATELLITE_MARK);
    }
}
