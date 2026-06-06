package com.njht.webyun.publish.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.njht.webyun.publish.collect.service.CollectionInfoService;
import com.njht.webyun.utils.R;



/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:53:36
 */
@RestController
@RequestMapping("collect/info")
@Api(tags = "添加收藏")
public class CollectionInfoController {
    @Autowired
    private CollectionInfoService collectionInfoService;

    /**
     * 保存
     */
    @ApiOperation(value = "添加收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="ids", dataType="String", required=true, value="id 拿逗号分隔开"),
            @ApiImplicitParam(paramType="String", name="types", dataType="String", value="文件类型,逗号分隔开")
    })
    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public R save(@Validated @Length(min = 1, message = "id不能为空") @RequestParam(name = "ids",required = false) String ids,
                  @RequestParam("types") String types){
		collectionInfoService.saveInfo(ids,types);
        return R.ok();
    }



}
