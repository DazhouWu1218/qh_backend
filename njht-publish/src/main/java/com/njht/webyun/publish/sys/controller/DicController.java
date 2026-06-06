package com.njht.webyun.publish.sys.controller;

import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.publish.sys.service.DicService;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 代国军
 * @description: 字典表
 * @date 2022/7/22 9:16
 */
@RestController
@RequestMapping("dic")
@Api(tags = "字典")
public class DicController {

    @Autowired
    private DicService dicService;

    /**
     * 产品标识集合
     */
    @ApiOperation(value = "字典测试", notes = "字典测试")
    @GetMapping({"/identifyList"})
    @ResponseBody
    public ReturnT<List<CommonEntity>> getIdentifyList(){
        List<CommonEntity> identifyList = dicService.getIdentifyList();
        return ReturnT.success(identifyList);
    }
}
