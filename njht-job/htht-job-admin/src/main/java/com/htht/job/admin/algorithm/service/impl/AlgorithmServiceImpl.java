package com.htht.job.admin.algorithm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.job.admin.algorithm.dao.AlgorithmDao;
import com.htht.job.admin.algorithm.service.AlgorithmService;
import com.htht.job.admin.algorithm.vo.AlgorithmReqVo;
import com.htht.job.admin.algorithm.vo.AlgorithmVo;
import com.htht.job.admin.dispatch.service.XxlJobService;
import com.htht.job.admin.plugin.entity.HandlerEntity;
import com.htht.job.admin.plugin.service.HandlerService;
import com.njht.entity.algorithm.AlgorithmEntity;
import com.njht.entity.base.BaseEntity;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.enums.TreeNodeEnum;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import com.njht.webyun.utils.StringUtils;
import com.njht.webyun.utils.TreeBuilderUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 算法管理表
 * @author daiguojun
 * @date 2022-05-30 13:06:10
 */
@Service("algorithmService")
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmDao, AlgorithmEntity> implements AlgorithmService {

    private static final String CATEGORY_ZERO = "0";

    @Autowired
    private HandlerService handlerService;

    @Override
    public PageUtils queryPage(AlgorithmVo algorithmVo) {
        String[] parentIds = new String[]{};
        // 分页对象
        PageEntity pageEntity = new PageEntity(algorithmVo.getPage(),algorithmVo.getSize());
        if (!Objects.isNull(algorithmVo.getParentId())) {
            parentIds = algorithmVo.getParentId().split(",");
        }
        //查询条件
        LambdaQueryWrapper<AlgorithmEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlgorithmEntity::getType,algorithmVo.getType())
                .in(!Objects.isNull(algorithmVo.getParentId()),AlgorithmEntity::getParentId,parentIds)
                .like(!Objects.isNull(algorithmVo.getName())&& StringUtils.isNotBlank(algorithmVo.getName()),AlgorithmEntity::getName,algorithmVo.getName());

        IPage<AlgorithmEntity> page = this.page(
                new Query<AlgorithmEntity>().getPage(pageEntity),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AlgorithmEntity> entityList = page.getRecords();
        List<AlgorithmReqVo> collect = Optional.ofNullable(entityList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    AlgorithmReqVo reqVo = new AlgorithmReqVo();
                    BeanUtils.copyProperties(item,reqVo);
                    reqVo.setHandlerName(handlerService.getHandlerById(item.getHandlerId()).getModelName());
                    reqVo.setHandlerIdArr(handlerService.getHandlerParentIdArr(item.getHandlerId()));
                    return reqVo;
                }).collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public List<Tree> queryAlgorithmList(String groupId, String handlerId) {
        // 如果 执行器id 和 插件id 都为空 返回null
        if (StringUtils.isEmpty(groupId) || StringUtils.isEmpty(handlerId)) {
            return new ArrayList<>();
        }
        // 所有算法信息
        List<AlgorithmEntity> dbList = this.list();
        // 过滤 出groupId 和 handlerId 相关信息 (只有树结构的最底层和执行器以及插件id关联,构建树结构需要知道父级相关信息)
        List<AlgorithmEntity> childrenList = Optional.ofNullable(dbList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> this.isAlgorithm(groupId, handlerId, item))
                .collect(Collectors.toList());
        List<AlgorithmEntity> algorithmList = new ArrayList<>();
        for (AlgorithmEntity item:childrenList) {
            // 构建树结构该需要根据最底层id 找到相关联的父级id
            this.getParentList(item.getId(), Optional.ofNullable(dbList).orElse(new ArrayList<>()),algorithmList);
        }
        //构建树结构
        List<Tree> treeList = algorithmList.stream().map(s -> {
            Tree node = new Tree();
            node.setValue(s.getId());
            node.setLabel(s.getName());
            node.setType(s.getType());
            node.setParentId(s.getParentId());
            return node;
        }).collect(Collectors.toList());
        return TreeBuilderUtil.buildTreeList(treeList);

    }

    @Override
    public String queryAddressListByJobId(int id) {
        return baseMapper.selectAddressListByJobId(id);
    }

    @Override
    public List<String> getRegisterList(String handlerId) {
        // 根据插件id 查询插件关联的执行器节点
        return handlerService.queryRegisterListByHandlerId(handlerId);
    }

    @Override
    public List<String> queryAlgorithmIdList(String algorithmId) {
        if (StringUtils.isEmpty(algorithmId)) {
            return new ArrayList<>();
        }
        List<AlgorithmEntity> entityList = this.list();
        List<AlgorithmEntity> parentEntityList =  new ArrayList<>();
        // 递归查询父节点
        this.getParentList(algorithmId,entityList,parentEntityList);
        // 获取所有id 并返回
        List<String> collect = Optional.of(parentEntityList)
                .orElse(new ArrayList<>())
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }

    @Override
    public boolean addOrEdit(AlgorithmEntity algorithm) {
        algorithm.setType(TreeNodeEnum.DATA.getCode());
        String handlerId = algorithm.getHandlerId();
        HandlerEntity handlerEntity = handlerService.getById(handlerId);
        Integer groupId = Optional.ofNullable(handlerEntity).orElse(new HandlerEntity()).getGroupId();
        algorithm.setGroupId(groupId);
        return this.saveOrUpdate(algorithm);
    }

    @Override
    public List<AlgorithmEntity> queryByTemplateId(String id) {
        LambdaQueryWrapper<AlgorithmEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(AlgorithmEntity::getTemplateId,id);
        return this.list(qw);
    }



    @Autowired
    private XxlJobService xxlJobService;
    @Override
    public boolean delete(List<String> ids) {
        // 校验该插件是否绑定任务
        List<String> jobList = xxlJobService.queryJobIdListByAlgorithmIds(ids);
        if (!jobList.isEmpty()) {
            throw new CommonException("插件与任务关联,不能被删除");
        }
        return this.removeByIds(ids);
    }

    @Override
    public Integer queryCountByGroupId(int id) {
        LambdaQueryWrapper<AlgorithmEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(AlgorithmEntity::getGroupId,id);
        return this.count(qw);
    }

    /**
     * 根据插件 id ，执行器id 过滤对应信息
     * @param groupId
     * @param handlerId
     * @param item
     * @return
     */
    private boolean isAlgorithm(String groupId, String handlerId, AlgorithmEntity item) {
        boolean flag = false;
        if (item.getGroupId() != null && StringUtils.isNotEmpty(item.getHandlerId())) {
            flag = item.getGroupId().equals(Integer.parseInt(groupId)) && item.getHandlerId().equals(handlerId);
        }
        return flag;
    }


    /**
     * 递归查询父节点信息
     * @param id id
     * @param categoryEntities 树结构实体集
     * @param parentList  父id list
     */
    private void getParentList(final String id, final List<AlgorithmEntity> categoryEntities, final List<AlgorithmEntity> parentList) {
        //获取当前节点信息,id唯一
        final AlgorithmEntity categoryEntity =
                categoryEntities.stream()
                                .filter(dataCategoryEntity -> dataCategoryEntity.getId().equals(id))
                                .findAny()
                                .orElse(new AlgorithmEntity());
        if (parentList.contains(categoryEntity)) {
            return;
        }
        parentList.add(categoryEntity);
        //递归继续寻找父节点
        if(!Objects.isNull(categoryEntity.getParentId()) && !CATEGORY_ZERO.equals(categoryEntity.getParentId())) {
            getParentList(categoryEntity.getParentId(), categoryEntities, parentList);
        }
        // 父id为0为第一层 直接返回
    }

}