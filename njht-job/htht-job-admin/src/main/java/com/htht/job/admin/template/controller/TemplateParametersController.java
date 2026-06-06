package com.htht.job.admin.template.controller;

import com.htht.job.admin.template.service.TemplateParametersService;
import com.htht.job.admin.template.vo.TemplateParamVo;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 模板参数表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
@RestController
@Api(tags = "模板参数管理")
@RequestMapping("template/param")
public class TemplateParametersController {

    @Autowired
    private TemplateParametersService templateParametersService;

    /**
     * 保存
     */
    @ApiOperation(value = "参数新增或修改")
    @PostMapping("/saveOrUpdate")
    public ReturnT<String> save(@Validated @RequestBody TemplateParamVo paramVo){
		templateParametersService.insertOrEdit(paramVo);
        return ReturnT.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "参数删除")
    @ApiImplicitParam(paramType = "query", name = "ids", dataType = "List<String>", value = "id")
    @PostMapping("/delete")
    public ReturnT<String> delete(@RequestParam("ids") List<String> ids){
		templateParametersService.removeByIds(ids);
        return ReturnT.success();
    }



}
