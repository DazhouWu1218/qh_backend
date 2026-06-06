package com.htht.job.admin.algorithm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.htht.job.admin.algorithm.service.AlgorithmService;
import com.htht.job.admin.algorithm.vo.AlgorithmVo;
import com.njht.entity.algorithm.AlgorithmEntity;
import com.njht.entity.base.BaseEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.enums.TreeNodeEnum;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.utils.StringUtils;
import com.njht.webyun.utils.TreeBuilderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 算法管理表
 * @author chenzedong
 * @email chenzedong@piesat.cn
 * @date 2022-05-30 13:06:10
 */
@Api(tags = "算法管理")
@RestController
@ResponseBody
@RequestMapping("/algorithm")
public class AlgorithmController {

    @Autowired
    private AlgorithmService algorithmService;

    /**
     * 所有算法信息 (数据量不大，查询出来在过滤处理)
     */
    @ApiOperation(value = "所有算法信息", notes = "所有算法信息")
    @GetMapping("/data/list")
    public ReturnT<List<AlgorithmEntity>> nodeList(){
        return ReturnT.success(algorithmService.list());
    }

    /**
     * 查询算法数据列表(分页查询)
     */
    @ApiOperation(value = "查询算法数据列表(分页查询)", notes = "查询算法数据列表(分页查询)")
    @PostMapping("/data/page")
    public ReturnT<PageUtils> list(@RequestBody AlgorithmVo algorithmVo){
        String parentId = algorithmVo.getParentId();
        // 根据parentId检索下级(递归进行)
        List<String> parentIds = new ArrayList<>();
        List<String> tempIds = Arrays.asList(parentId);
        getAlgorithmParentIds(parentIds,tempIds);
        if (!parentIds.isEmpty()) {
            parentId = StringUtils.joinWith(",",parentIds.toArray());
            algorithmVo.setParentId(parentId);
        }
        algorithmVo.setType(TreeNodeEnum.DATA.getCode());
        PageUtils page = algorithmService.queryPage(algorithmVo);
        return ReturnT.success(page);
    }

    /**
     * 目录节点递归方法
     * @param parentIds
     * @param tempIds
     * @return
     */
    private List<String> getAlgorithmParentIds(List<String> parentIds,List<String> tempIds){
        if (!tempIds.isEmpty()) {
            parentIds.addAll(tempIds);
            for (String parentId: tempIds) {
                tempIds = algorithmService.
                        list(new LambdaQueryWrapper<AlgorithmEntity>().eq(AlgorithmEntity::getParentId, parentId)
                                .eq(AlgorithmEntity::getType, TreeNodeEnum.CONTENT.getCode())).stream().map(BaseEntity::getId).collect(Collectors.toList());
                getAlgorithmParentIds(parentIds,tempIds);
            }
        }
        return tempIds;
    }

    /**
     * 查询算法目录树
     */
    @ApiOperation(value = "算法目录树结构", notes = "算法目录树结构")
    @GetMapping("/content/tree")
    public ReturnT contentTree(){
        List<Tree> algorithmList = algorithmService.list().stream()
                .filter(s-> TreeNodeEnum.CONTENT.getCode().equals(s.getType()))
                .sorted(Comparator.comparing(BaseEntity::getUpdateTime))
                .map(s -> {
            Tree node = new Tree();
            node.setValue(s.getId());
            node.setLabel(s.getName());
            node.setParentId(s.getParentId());
            return node;
        }).collect(Collectors.toList());
        List<Tree> algorithmTree = TreeBuilderUtil.buildTreeList(algorithmList);
        return ReturnT.success(algorithmTree);
    }

    /**
     * 查询算法结构树
     * @param modelId
     * @param groupId
     * @return
     */
    @ApiOperation(value = "算法结构树", notes = "算法结构树")
    @GetMapping("/tree")
    public ReturnT getAlgorithmTree(@RequestParam(value = "modelId") String modelId,
                                 @RequestParam(value = "groupId") String groupId){
        List<Tree> algorithmList = algorithmService.list(new LambdaQueryWrapper<AlgorithmEntity>()
                .eq(!Objects.isNull(modelId),AlgorithmEntity::getHandlerId,modelId)
                .eq(!Objects.isNull(groupId),AlgorithmEntity::getGroupId,groupId))
                .stream().map(s -> {
                    Tree node = new Tree();
                    node.setValue(s.getId());
                    node.setLabel(s.getName());
                    node.setType(s.getType());
                    node.setParentId(s.getParentId());
                    return node;
                }).collect(Collectors.toList());
        List<Tree> algorithmTree = TreeBuilderUtil.buildTreeList(algorithmList);
        return ReturnT.success(algorithmTree);
    }


    /**
     * 根据插件id加载节点列表
     */
    @ApiOperation(value = "根据插件id加载节点列表", notes = "根据插件id加载节点列表")
    @ApiImplicitParam(name = "handlerId",value = "插件id",required = true,dataType = "String")
    @GetMapping("/registryList")
    public ReturnT<List<String>> registerList(@RequestParam("handlerId") String handlerId){
        List<String> registerList = algorithmService.getRegisterList(handlerId);
        return ReturnT.success(registerList);
    }


    /**
     * 添加算法目录节点
     * @param algorithm
     * @return
     */
    @ApiOperation(value = "添加算法目录树节点", notes = "算法目录树结构")
    @PostMapping("/content/node")
    public ReturnT addContentTreeNode(@RequestBody AlgorithmVo algorithm){
        // 父节点id没有选择的情况下，默认为根节点
        if (Objects.isNull(algorithm.getParentId())){
            algorithm.setParentId("0");
        }
        AlgorithmEntity algorithmEntity = new AlgorithmEntity();
        BeanUtils.copyProperties(algorithm,algorithmEntity);
        algorithmEntity.setType(TreeNodeEnum.CONTENT.getCode());
        if (algorithmService.saveOrUpdate(algorithmEntity)) return ReturnT.success();
        return ReturnT.failedMsg("添加/更新失败");
    }


    /**
     * 删除目录节点
     * @param id
     * @return
     */
    @ApiOperation(value = "删除算法目录树节点", notes = "算法目录树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "目录节点id",required = true,dataType = "String")
    })
    @PostMapping("/content/node/{id}")
    public ReturnT deleteContentTreeNode(@PathVariable("id") String id){
        // 删除目录节点之前，查询下级是否存在节点，存在则不允许删除
        List<AlgorithmEntity> algorithmNodeList = algorithmService.
                list(new LambdaQueryWrapper<AlgorithmEntity>().eq(AlgorithmEntity::getParentId,id));
        if (algorithmNodeList.size()>0) {
            return ReturnT.failedMsg("存在下级节点,无法删除");
        }
        if (algorithmService.removeById(id)) {
            return ReturnT.success();
        }
        return ReturnT.failed("删除目录节点失败");
    }


    /**
     * 新增/更新算法
     * @param algorithm
     * @return
     */
    @ApiOperation(value = "新增/更新算法", notes = "新增/更新算法")
    @PostMapping("/data")
    public ReturnT saveDataNode(@Validated @RequestBody AlgorithmEntity algorithm){

        if (algorithmService.addOrEdit(algorithm)) {
            return ReturnT.success();
        }
        return ReturnT.failedMsg("添加/更新失败");
    }

    /**
     * 删除算法（支持批量删除）
     */
    @ApiOperation(value = "删除算法", notes = "删除算法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "删除id数组",required = true,dataType = "String[]")
    })
    @PostMapping ("/delete")
    public ReturnT delete(@RequestBody String[] ids){
		if(algorithmService.delete(Arrays.asList(ids))) {
            return ReturnT.success();
        }
        return ReturnT.failedMsg("删除失败");
    }

}
