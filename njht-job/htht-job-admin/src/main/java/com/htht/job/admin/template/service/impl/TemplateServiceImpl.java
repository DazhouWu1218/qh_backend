package com.htht.job.admin.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.job.admin.algorithm.service.AlgorithmService;
import com.htht.job.admin.plugin.entity.HandlerEntity;
import com.htht.job.admin.plugin.service.HandlerService;
import com.htht.job.admin.template.constant.TemplateConstant;
import com.htht.job.admin.template.dao.TemplateDao;
import com.htht.job.admin.template.entity.TemplateEntity;
import com.htht.job.admin.template.service.TemplateParametersService;
import com.htht.job.admin.template.service.TemplateService;
import com.htht.job.admin.template.vo.TemplateVo;
import com.njht.entity.algorithm.AlgorithmEntity;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.enums.NumberEnum;
import com.njht.webyun.enums.TreeNodeEnum;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import com.njht.webyun.utils.TreeBuilderUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板管理表
 * @author daiguojun
 * @date 2022-06-29 09:14:41
 */
@Service("templateService")
public class TemplateServiceImpl extends ServiceImpl<TemplateDao, TemplateEntity> implements TemplateService {

    @Autowired
    private TemplateParametersService templateParametersService;

    @Autowired
    private HandlerService handlerService;

    @Autowired
    private AlgorithmService algorithmService;

    @Override
    public List<Tree> tree(String type) {
        List<TemplateEntity> dbFilterList = getTemplateEntityList(type);

        List<Tree> treeList = Optional.of(dbFilterList).orElse(new ArrayList<>())
                .stream()
                .map(item -> new Tree(item.getId(), item.getName(), item.getParentId()))
                .collect(Collectors.toList());
        return TreeBuilderUtil.buildTreeList(treeList);
    }

    private List<TemplateEntity> getTemplateEntityList(String type) {
        // 数据库过滤之后的集合信息
        List<TemplateEntity> dbFilterList = new ArrayList<>();
        // 根据不同的情况查询不同的目录以及数据信息
        if (Objects.equals(type,TemplateConstant.IDENTIFY_TWO)) {
            // 算法模板,查询标识identify 等于1的节点及其父节点信息
            this.getDataAndParentList(NumberEnum.NUMBER_1.getNum(),dbFilterList);
        } else if (Objects.equals(type, TemplateConstant.IDENTIFY_ONE)) {
            // 调度模板,查询标识等于0的所有节点及其父节点
           this.getDataAndParentList(NumberEnum.NUMBER_0.getNum(),dbFilterList);
        } else {
            // 只查询目录节点信息
            LambdaQueryWrapper<TemplateEntity> qw = new LambdaQueryWrapper<>();
            qw.eq(TemplateEntity::getType, TreeNodeEnum.CONTENT.getCode());
            dbFilterList = this.list(qw);
        }
        return dbFilterList;
    }

    @Override
    public List<String> tree(String type, String id) {
        List<TemplateEntity> dbFilterList = getTemplateEntityList(type);
        List<String> ids = new ArrayList<>();
        ids.add(id);
        extracted(id, dbFilterList, ids);
        Collections.reverse(ids);
        return ids;
    }

    private static List<String> extracted(String id, List<TemplateEntity> dbFilterList, List<String> ids) {
        List<String> parentId = Optional.of(dbFilterList).orElse(new ArrayList<>())
                .stream().filter(s-> s.getId().equals(id)).map(TemplateEntity::getParentId).collect(Collectors.toList());
        if (!parentId.isEmpty() && !parentId.get(0).equals("0")) {
            ids.add(parentId.get(0));
            extracted(parentId.get(0),dbFilterList,ids);
        }
        return ids;
    }

    /**
     * 查询对应标识节点以及相关父节点信息
     * @param num
     * @param dbFilterList
     * @return
     */
    private void getDataAndParentList(int num,List<TemplateEntity> dbFilterList) {
        // 数据库所有目录以及数据节点信息
        List<TemplateEntity> dbList = this.list();
        // 过滤出所有的 算法数据节点/调度数据节点
        List<TemplateEntity> dataList = Optional.ofNullable(dbList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item.getIdentify() == num)
                .collect(Collectors.toList());
        //递归寻找所有数据节点的父节点,并将结果存储到 dbFilterList 中
        for (TemplateEntity entity:dataList) {
            this.getParentList(entity.getId(),Optional.ofNullable(dbList).orElse(new ArrayList<>()),dbFilterList);
        }
    }

    /**
     * 递归查询父节点信息
     * @param id id
     * @param categoryEntities 树结构实体集
     * @param parentList  父id list
     */
    private void getParentList(final String id, final List<TemplateEntity> categoryEntities, final List<TemplateEntity> parentList) {
        //获取当前节点信息,id唯一
        final List<TemplateEntity> currentCategoryList = categoryEntities.stream().filter(dataCategoryEntity -> dataCategoryEntity.getId().equals(id)).collect(Collectors.toList());

        TemplateEntity categoryEntity = new TemplateEntity();
        if(CollectionUtils.isNotEmpty(currentCategoryList)){
            categoryEntity = currentCategoryList.get(0);
            if (parentList.contains(categoryEntity)) {
                return;
            }
            parentList.add(categoryEntity);
        }
        //递归继续寻找父节点
        if(!Objects.isNull(categoryEntity.getParentId()) && ! Objects.equals(TemplateConstant.CATEGORY_ZERO,categoryEntity.getParentId())) {
            getParentList(categoryEntity.getParentId(), categoryEntities, parentList);
        }
        // 父id为0为第一层 直接返回
    }

    @Override
    public PageUtils queryPage(String id, Integer identify,Integer page,Integer size) {
        PageEntity pageEntity = new PageEntity(page,size);
        //分页查询 该目录id下,对应模板的数据节点列表
        LambdaQueryWrapper<TemplateEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(TemplateEntity::getParentId,id).eq(TemplateEntity::getIdentify,identify).eq(TemplateEntity::getType,TreeNodeEnum.DATA.getCode());
        IPage<TemplateEntity> pageInfo = this.page(
                new Query<TemplateEntity>().getPage(pageEntity),
                qw
        );
        PageUtils pageUtils = new PageUtils(pageInfo);
        // 查询结果不为空,添加模板对应的模板参数
        if (!pageInfo.getRecords().isEmpty()) {
            // 获取模板对应的模板参数相关信息
            List<?> list = templateParametersService.getParamListInfo(pageInfo.getRecords());
            pageUtils.setList(list);
        }
        return pageUtils;
    }

    @Override
    public void insert(TemplateVo template) {
        TemplateEntity templateEntity = new TemplateEntity();
        BeanUtils.copyProperties(template,templateEntity);
        // 新增目录节点 type 为0
        templateEntity.setType(TreeNodeEnum.CONTENT.getCode());
        // 新增目录 标识 Identify -1
        templateEntity.setIdentify(TemplateConstant.MENU_IDENTIFY);
        this.save(templateEntity);
    }

    @Override
    public void edit(TemplateVo template) {
        TemplateEntity templateEntity = new TemplateEntity();
        templateEntity.setId(template.getId());
        templateEntity.setName(template.getName());
        this.updateById(templateEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        // 校验当前节点是否包含子节点
        LambdaQueryWrapper<TemplateEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(TemplateEntity::getParentId,id);
        List<TemplateEntity> templateEntities = this.list(qw);
        if (!templateEntities.isEmpty()) {
            throw new CommonException("当前节点包含子节点不能删除");
        }
        // 校验是否绑定算法
        List<AlgorithmEntity> algorithmEntities = algorithmService.queryByTemplateId(id);
        if (!algorithmEntities.isEmpty()) {
            throw new CommonException("当前模板与算法绑定不能删除");
        }

        // 校验是否绑定插件
        List<HandlerEntity> handlerEntities = handlerService.queryByTemplateId(id);
        if (!handlerEntities.isEmpty()) {
            throw new CommonException("当前模板与插件绑定不能删除");
        }
        // 删除当前id 对应的模板参数

        templateParametersService.deleteByTemplateId(id);
        return this.removeById(id);
    }


}