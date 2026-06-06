package com.htht.job.admin.plugin.controller;

import com.htht.job.admin.plugin.service.HandlerService;
import com.htht.job.admin.plugin.vo.HandlerSearchVo;
import com.htht.job.admin.plugin.vo.HandlerVo;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 * 插件管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:12:01
 */
@RestController
@RequestMapping("plugin")
@Api(tags = "插件管理")
public class HandlerController {
    @Autowired
    private HandlerService handlerService;

    /**
     * 树结构
     */
    @ApiOperation(value = "插件目录树")
    @ApiImplicitParam(paramType="query", name="type", dataType="String", required=true, value="0目录(插件管理页面用) 1目录+数据(算法管理页面用)")
    @GetMapping("/tree")
    public ReturnT<List<Tree>> tree(@RequestParam("type") String type){
        List<Tree> treeList = handlerService.queryTreeByType(type);

        return ReturnT.success(treeList);
    }


    /**
     * 信息列表
     */
    @ApiOperation( value = "根据目录id 查询对应的数据列表")
    @PostMapping("/list")
    public ReturnT<PageUtils> info(@Validated @RequestBody HandlerSearchVo handlerSearchVo){
        PageUtils pageUtils = handlerService.queryPage(handlerSearchVo);
        return ReturnT.success(pageUtils);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ReturnT<String> save(@Validated(SaveGroup.class)  @RequestBody HandlerVo handler){
		handlerService.saveHandler(handler);
        return ReturnT.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public ReturnT<String> update(@Validated(UpdateGroup.class) @RequestBody HandlerVo handler){
		handlerService.updateHandlerById(handler);
        return ReturnT.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除数据")
    @ApiImplicitParam(paramType="query", name="ids", dataType="String", required=true, value="ids批量删除")
    @PostMapping("/delete")
    public ReturnT<String> deleteData(@RequestParam(value = "ids") List<String> ids){
        if (handlerService.deleteData(ids)) {
            return ReturnT.success();
        } else {
            return ReturnT.failedMsg("删除失败");
        }

    }

    @ApiOperation(value = "删除节点")
    @ApiImplicitParam(paramType="query", name="id", dataType="String", required=true, value="id,不支持批量删除")
    @PostMapping("/deleteNode")
    public ReturnT<String> deleteNode(@RequestParam(value = "id") String id){
        if (Boolean.FALSE.equals(handlerService.deleteNode(id))) {
            return ReturnT.failedMsg("该节点下有数据,请先删除数据");
        } else {
            return ReturnT.success();
        }
    }

}
