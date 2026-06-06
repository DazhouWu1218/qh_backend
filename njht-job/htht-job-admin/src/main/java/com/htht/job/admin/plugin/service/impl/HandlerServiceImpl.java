package com.htht.job.admin.plugin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.htht.job.admin.dispatch.service.JobGroupService;
import com.htht.job.admin.dispatch.service.XxlJobService;
import com.htht.job.admin.plugin.dao.HandlerDao;
import com.htht.job.admin.plugin.entity.HandlerEntity;
import com.htht.job.admin.plugin.service.HandlerService;
import com.htht.job.admin.plugin.vo.HandlerReqVo;
import com.htht.job.admin.plugin.vo.HandlerSearchVo;
import com.htht.job.admin.plugin.vo.HandlerVo;
import com.htht.job.admin.plugin.vo.PluginTaskReqVo;
import com.htht.job.admin.template.service.TemplateService;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.enums.NumberEnum;
import com.njht.webyun.enums.TreeNodeEnum;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import com.njht.webyun.utils.TreeBuilderUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 插件管理表
 * @author daiguojun
 * @date 2022-05-30 13:12:01
 */
@Service("handlerService")
public class HandlerServiceImpl extends ServiceImpl<HandlerDao, HandlerEntity> implements HandlerService {

    @Autowired
    private JobGroupService jobGroupService;

    @Autowired
    private TemplateService templateService;

    @Override
    public PageUtils queryPage(HandlerSearchVo handlerSearchVo) {
        LambdaQueryWrapper<HandlerEntity> qw = new LambdaQueryWrapper<>();
        // 1.查询id下 所有数据节点id信息
        String parentId = handlerSearchVo.getId();
        if (!StringUtils.isEmpty(parentId)) {
            List<String> idList = this.getChildDataId(parentId);
            if (idList.isEmpty()) {
                // 没有子节点
                return new PageUtils(new PageInfo<>(new ArrayList<>()));
            }
            // 查询该id下所有的数据节点信息
            qw.in(HandlerEntity::getId,idList);
        }
        qw.eq(HandlerEntity::getType, TreeNodeEnum.DATA.getCode());
        // 名称不为空，查询带上名称
        if (StringUtils.isNotEmpty(handlerSearchVo.getName())) {
            qw.like(HandlerEntity::getModelName,handlerSearchVo.getName());
        }
        // 标识不为空，查询带上标识
        if (StringUtils.isNotEmpty(handlerSearchVo.getIdentify())) {
            qw.like(HandlerEntity::getModelIdentify,handlerSearchVo.getIdentify());
        }
        qw.orderByDesc(HandlerEntity::getUpdateTime);
        IPage<HandlerEntity> pageInfo = this.page(new Query<HandlerEntity>().getPage(handlerSearchVo), qw);
        // 设置返回结果
        PageUtils pageUtils = new PageUtils(pageInfo);
        List<HandlerReqVo> collect = Optional.ofNullable(pageInfo.getRecords()).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    HandlerReqVo reqVo = new HandlerReqVo();
                    BeanUtils.copyProperties(item, reqVo);
                    if (reqVo.getGroupId() != null && reqVo.getGroupId() != 0) {
                        reqVo.setGroupName(jobGroupService.getNameById(reqVo.getGroupId()));
                    }
                    if(StringUtils.isNotEmpty(reqVo.getTemplateId())) {
                        reqVo.setTemplateName(templateService.getById(reqVo.getTemplateId()).getName());
                    }
                    return reqVo;
                }).collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    /**
     * 查询数据节点信息
     * @param parentId
     * @return
     */
    private List<String> getChildDataId(String parentId) {
        // 数据库所有目录节点以及对应数据节点信息
        List<HandlerEntity> dbList = this.list();
        // 过滤出当前目录id下所有的数据节点id 信息
        List<String> idList = new ArrayList<>();
        this.dataIdList(parentId,dbList,idList);
        return idList;
    }

    /**
     * 递归 查找子节点信息
     * @param parentId
     * @param dbList
     * @param idList
     */
    private void dataIdList(String parentId, List<HandlerEntity> dbList, List<String> idList) {
        List<HandlerEntity> childrenList = Optional.ofNullable(dbList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> parentId.equals(item.getParentId()))
                .collect(Collectors.toList());
        //如果不为空 进入循环 子节点为空直接返回
        if(!CollectionUtils.isEmpty(childrenList)) {
            for (final HandlerEntity item : childrenList) {
                if (Objects.equals(TreeNodeEnum.DATA.getCode(),item.getType())){
                    idList.add(item.getId());
                }
                dataIdList(item.getId(), dbList, idList);
            }
        }
    }

    @Override
    public List<Tree> queryTreeByType(String type) {
        // type = 0 只查目录, type = 1 查询目录+数据
        LambdaQueryWrapper<HandlerEntity> qw = new LambdaQueryWrapper<>();
        if (Objects.equals(TreeNodeEnum.CONTENT.getCode(),type)){
            qw.eq(HandlerEntity::getType,type);
        }
        // 查询所有的数据信息
        List<HandlerEntity> list = this.list(qw);
        List<Tree> treeList = Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    Tree tree = new Tree();
                    tree.setParentId(item.getParentId());
                    tree.setLabel(item.getModelName());
                    tree.setValue(item.getId());
                    return tree;
                }).collect(Collectors.toList());

        return TreeBuilderUtil.buildTreeList(treeList);
    }

    @Override
    public void saveHandler(HandlerVo handler) {
        HandlerEntity entity = new HandlerEntity();
        BeanUtils.copyProperties(handler,entity);
        this.save(entity);
    }

    @Override
    public void updateHandlerById(HandlerVo handler) {
        HandlerEntity entity = new HandlerEntity();
        BeanUtils.copyProperties(handler,entity);
        // groupId 防止绑定执行器被更新为0
        if (entity.getGroupId() <= NumberEnum.NUMBER_0.getNum()) {
            // 设置为null 更新时不会更新groupId 字段
            entity.setGroupId(null);
        }
        this.updateById(entity);
    }

    @Override
    public List<PluginTaskReqVo> queryPluginListByGroupId(String groupId, String handlerId) {
        LambdaQueryWrapper<HandlerEntity> qw = new LambdaQueryWrapper<>();
        List<HandlerEntity> list;
        if (StringUtils.isNotEmpty(handlerId)) {
            // 根据handlerId只能查询到一条数据
            qw.eq(HandlerEntity::getId,handlerId);
            list = this.list(qw);
        } else if (StringUtils.isNotEmpty(groupId)) {
            // 根据groupId 可以查询到该执行器下面所有的插件信息
            qw.eq(HandlerEntity::getGroupId,Integer.parseInt(groupId)).eq(HandlerEntity::getType,TreeNodeEnum.DATA.getCode());
            list = this.list(qw);
        } else {
            // 不传参 默认返回为空集合
            list = new ArrayList<>();
        }
        return Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .map(item -> new PluginTaskReqVo(item.getId(),item.getModelName(),item.getModelIdentify()))
                .collect(Collectors.toList());
    }

    @Override
    public HandlerEntity getHandlerById(String handlerId) {
        LambdaQueryWrapper<HandlerEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(HandlerEntity::getId,handlerId);
        return this.getOne(qw);
    }

    @Override
    public List<String> queryRegisterListByHandlerId(String handlerId) {
        String registerValue = baseMapper.selectRegisterValueById(handlerId);
        if (StringUtils.isEmpty(registerValue)){
            return new ArrayList<>();
        }
        return Arrays.asList(registerValue.split(","));
    }

    @Override
    public Boolean deleteNode(String id) {
        Boolean flag = true;
        // 查询该节点下是否有数据
        LambdaQueryWrapper<HandlerEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HandlerEntity::getParentId,id);
        List<HandlerEntity> list = this.list(queryWrapper);
        if(!list.isEmpty()){
            // 该id 下面有子节点，不能删除
            flag = false;
        } else {
            // 删除只删除当前节点，不删除子节点信息
            flag = this.removeById(id);
        }

        return flag;
    }

    @Override
    public List<String> getHandlerParentIdArr(String handlerId) {
        List<HandlerEntity> entityList = this.list();
        List<HandlerEntity> parentEntityList =  new ArrayList<>();
        // 递归查询父节点
        this.getParentList(handlerId,entityList,parentEntityList);
        // 获取所有id 并返回
        List<String> collect = Optional.of(parentEntityList)
                .orElse(new ArrayList<>())
                .stream()
                .map(HandlerEntity::getId)
                .collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }

    @Override
    public List<HandlerEntity> queryByTemplateId(String id) {
        LambdaQueryWrapper<HandlerEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(HandlerEntity::getTemplateId,id);
        return this.list(qw);
    }


    @Autowired
    private XxlJobService xxlJobService;

    @Override
    public boolean deleteData(List<String> ids) {
        // 校验该插件是否绑定任务
        List<String> jobList = xxlJobService.queryJobIdListByHandlerIds(ids);
        if (!jobList.isEmpty()) {
            throw new CommonException("插件与任务关联,不能被删除");
        }
        return this.removeByIds(ids);
    }

    @Override
    public Integer queryCountByGroupId(int id) {
        LambdaQueryWrapper<HandlerEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(HandlerEntity::getGroupId,id);
        return this.count(qw);
    }

    /**
     * 递归查找 插件父节点
     * @param id
     * @param entityList
     * @param parentList
     */
    private void getParentList(String id, List<HandlerEntity> entityList, List<HandlerEntity> parentList) {

        //获取当前节点信息,id唯一
        final HandlerEntity categoryEntity =
                entityList.stream()
                        .filter(dataCategoryEntity -> dataCategoryEntity.getId().equals(id))
                        .findAny()
                        .orElse(new HandlerEntity());
        if (parentList.contains(categoryEntity)) {
            return;
        }
        parentList.add(categoryEntity);
        //递归继续寻找父节点
        if(!Objects.isNull(categoryEntity.getParentId()) && !TreeNodeEnum.CONTENT.getCode().equals(categoryEntity.getParentId())) {
            getParentList(categoryEntity.getParentId(), entityList, parentList);
        }
        // 父id为0为第一层 直接返回
    }
}