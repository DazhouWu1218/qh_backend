package com.htht.job.admin.template.controller;

import com.htht.job.admin.template.service.TemplateService;
import com.htht.job.admin.template.vo.TemplateVo;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 模板管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
@RestController
@RequestMapping("/template")
@Api(tags = "模板管理")
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    /**
     * 目录树
     */
    @ApiOperation(value = "目录树结构")
    @ApiImplicitParam(paramType = "query", name = "type", dataType = "String", value = "标识（0模板目录 1调度模板目录+数据(绑定插件用) 2算法模板目录+数据（绑定算法用）")
    @PostMapping("/tree")
    public ReturnT<List<Tree>> tree(@RequestParam("type")String type){
        List<Tree> treeList = templateService.tree(type);
        return ReturnT.success(treeList);
    }

    /**
     * 根据模板id查询目录树
     */
    @ApiOperation(value = "根据模板id查询目录树")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", value = "算法模板id"),
            @ApiImplicitParam(paramType = "query", name = "type", dataType = "String", value = "标识（0模板目录 1调度模板目录+数据(绑定插件用) 2算法模板目录+数据（绑定算法用）")
    })
    @GetMapping("/tree")
    public ReturnT<List<String>> treeById(@RequestParam("type")String type,@RequestParam("id")String id){
        List<String> treeList = templateService.tree(type,id);
        return ReturnT.success(treeList);
    }


    /**
     * 列表
     */
    @ApiOperation(value = "模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", value = "目录id"),
            @ApiImplicitParam(paramType = "query", name = "identify", dataType = "Integer", value = "标识（0调度模板 1算法模板）"),
            @ApiImplicitParam(paramType = "query", name = "page", dataType = "Integer", value = "第几页"),
            @ApiImplicitParam(paramType = "query", name = "size", dataType = "Integer", value = "每页大小")
    })
    @PostMapping("/list")
    public ReturnT<PageUtils> list(@RequestParam("id") String id, @RequestParam("identify") Integer identify,
                                  @RequestParam(value = "page",defaultValue = "1") Integer page,
                                   @RequestParam(value = "size",defaultValue = "10") Integer size){
        PageUtils pageUtils = templateService.queryPage(id,identify,page,size);
        return ReturnT.success(pageUtils);
    }

    /**
     * 保存目录
     */
    @ApiOperation(value = "目录新增")
    @PostMapping("/save")
    public ReturnT<String> save(@Validated(SaveGroup.class) @RequestBody TemplateVo template){
		templateService.insert(template);
        return ReturnT.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "目录修改(只支持对名称进行修改)")
    @PostMapping("/update")
    public ReturnT<String> update(@Validated(UpdateGroup.class) @RequestBody TemplateVo template){
		templateService.edit(template);
        return ReturnT.success();
    }

    /**
     * 删除目录或数据
     */
    @ApiOperation(value = "删除目录")
    @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", value = "id")
    @PostMapping("/delete")
    public ReturnT<String> deleteNode(@RequestParam("id") String id){
		if (!templateService.delete(id)) {
		    return ReturnT.failedMsg("删除失败");
        }
        return ReturnT.success();
    }

}
