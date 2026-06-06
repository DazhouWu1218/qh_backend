package com.njht.webyun.publish.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.publish.feign.DataCenterFeignService;
import com.njht.webyun.publish.index.vo.IndexProductCategoryReqVo;
import com.njht.webyun.publish.index.vo.IndexProductReqVo;
import com.njht.webyun.publish.product.dao.DataCategoryDao;
import com.njht.webyun.publish.product.entity.ProductEntity;
import com.njht.webyun.publish.product.service.DataCategoryService;
import com.njht.webyun.publish.product.service.ProductService;
import com.njht.webyun.publish.product.vo.HotProductInfoReqVo;
import com.njht.webyun.publish.product.vo.ProductTree;
import com.njht.webyun.utils.TreeBuilderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("dataCategoryService")
@Slf4j
@DS(value = DbConstant.MYSQL_1)
public class DataCategoryServiceImpl extends ServiceImpl<DataCategoryDao, DataCategoryEntity> implements DataCategoryService {

    private static final String CATEGORY_ZERO = "0";

    @Autowired
    private ProductService productService;

    @Autowired
    private DataCategoryService dataCategoryService;

    @Autowired
    private DataCenterFeignService dataCenterFeignService;

    @Override
    public List<CommonEntity> queryPage() {
        final List<DataCategoryEntity> categoryEntities = this.categoryList();
        // 拿到parentId
        final Set<String> parentSet = categoryEntities.stream().map(DataCategoryEntity::getParentId).collect(Collectors.toSet());
        return categoryEntities.stream()
                .filter(item -> !parentSet.contains(item.getId()))
                .map(dataCategoryEntity -> {
            final CommonEntity commonEntity = new CommonEntity();
            commonEntity.setLabel(dataCategoryEntity.getText());
            commonEntity.setValue(dataCategoryEntity.getId());
            return commonEntity;
        }).collect(Collectors.toList());
    }

    private List<DataCategoryEntity> categoryList(){
        //查询分类集合信息
        final Integer userId = UserUtil.getCurrentUser().getUserId();
        final List<DataCategoryEntity> categoryEntities = this.dataCategoryService.selectTreeList(userId);
        // 过滤掉最后一层数据
        //1.查询出id为父id的所有树结构id
        List<String> parentIds = Optional.ofNullable(categoryEntities).orElse(new ArrayList<>())
                .stream()
                .map(DataCategoryEntity::getParentId)
                .distinct()
                .collect(Collectors.toList());
        //2.过滤掉第三层
        return categoryEntities.stream().filter(item -> parentIds.contains(item.getId())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<ProductTree> getCategoryTree() {
        final List<DataCategoryEntity> categoryEntityArrayList = this.categoryList();
        //构建产品树结构
        return buildTreeList(categoryEntityArrayList);
    }

    /**
     * 处理id 的方法，返回product表中的产品id
     * （1.树结构id，2.产品id）
     * 1.监测预测在树结构中的最后一级
     * 2.树结构改掉之后监测预测在product_info中（用新支撑平台之后）
     * 3.如果树结构中没有产品id 查询productid
     * 4.取树结构的最后一级id，然后在product 表中查出产品id
     * @param id
     * @return
     */
    @Override
    public Map<String,List<String>> getProductIdAndNames(final String id) {
        final LinkedHashMap<String,List<String>> map = new LinkedHashMap<>(16);
        final Integer userId = UserUtil.getCurrentUser().getUserId();
        final List<DataCategoryEntity> categoryEntities = this.dataCategoryService.selectTreeList(userId);
        //1.判断树结构中没有id直接返回（说明id是product_info 表中的监测预测）
        final List<String> idList = categoryEntities.stream().map(DataCategoryEntity::getId).collect(Collectors.toList());
        if(idList.contains(id)){
            //2.递归寻找 该id下的子节点信息
            final List<DataCategoryEntity> childCategoryList = getChildrenList(id,categoryEntities,new ArrayList<>());
            //3.递归寻找当前节点以及父节点信息
            final List<DataCategoryEntity> parentCategoryList = getParentList(id,categoryEntities,new ArrayList<>());
            //根据父子节点构建树结构
            childCategoryList.addAll(parentCategoryList);
            final List<ProductTree> trees = buildTreeList(childCategoryList);
            //根据构建的树结构获取名称信息以及子集id信息
            final List<String> categoryIds = getProductChildrenIdList(trees, new ArrayList<>());
            //获取产品id
            final List<DataCategoryEntity> collect = categoryEntities.stream().filter(dataCategoryEntity -> categoryIds.get(0).equals(dataCategoryEntity.getId())).collect(Collectors.toList());
            final List<String> productNameList =  Arrays.asList(collect.get(0).getMenu().split(">"));
            map.put("ids",categoryIds);
            map.put("names",productNameList);
        }else{
            //拿到treeId 并 获取父级名称
            final ProductEntity product = this.productService.getById(id);
            List<DataCategoryEntity> collect = new ArrayList<>();
            if(!Objects.isNull(product)) {
                collect = Optional.of(categoryEntities).orElse(new ArrayList<>())
                        .stream().filter(dataCategoryEntity -> Objects.equals(product.getTreeId(), dataCategoryEntity.getId())).collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(collect)){
                final List<String> productNameList =  Arrays.asList(collect.get(0).getMenu().split(">"));
                map.put("names",productNameList);
            }
        }
//        获取产品id及分类信息
        return map;
    }

    @Override
//    @Cacheable(value = "productTreeList",key = "#root.method.name"+"+#userId")
    public List<DataCategoryEntity> selectTreeList(final Integer userId) {
        return dataCenterFeignService.categoryList("product",null).getData();
    }

    @Override
    public List<IndexProductCategoryReqVo> getFirstCategoryInfo() {
        //  首页默认查全部一级分类相关信息 (-1 默认角色，查一级树结构)
        final List<DataCategoryEntity> categoryEntities = this.queryTreeListByRoleId(-1);
        return categoryEntities.stream()
                        .filter(dataCategoryEntity -> dataCategoryEntity.getParentId().equals(CATEGORY_ZERO))
                        .map(dataCategoryEntity -> {
                            final IndexProductCategoryReqVo reqVo = new IndexProductCategoryReqVo();
                            reqVo.setName(dataCategoryEntity.getText());
                            reqVo.setProductId(dataCategoryEntity.getId());
                            return reqVo;
                        })
                        .collect(Collectors.toList());
    }

    @Override
    public IndexProductReqVo getProductNameAndId(final String productId) {
        // 第四级
        final DataCategoryEntity category = this.getById(productId);
        String name = category.getText();
        // 第三级
        final DataCategoryEntity parentCategory = this.getById(category.getParentId());
        final IndexProductReqVo reqVo = new IndexProductReqVo();
        if(parentCategory != null) {
            name = parentCategory.getText() + name;
        }
        reqVo.setProductId(productId);
        reqVo.setTitle(name);
        return reqVo;
    }

    @Override
    public Map<String, String> getParentNameMapInfo(final List<String> idList) {
        final Integer userId = UserUtil.getCurrentUser().getUserId();
        final List<DataCategoryEntity> categoryEntities = this.dataCategoryService.selectTreeList(userId);
        final Map<String,String> map = new ConcurrentHashMap<>(16);
        for (String id:idList) {
            List<DataCategoryEntity> dataCategoryEntities = Optional.of(categoryEntities).orElse(new ArrayList<>());
            Optional<DataCategoryEntity> first = dataCategoryEntities.stream().filter(item -> item.getId().equals(id)).findFirst();
            DataCategoryEntity currentCategory = first.orElseGet(DataCategoryEntity::new);
            Stream<DataCategoryEntity> parent = dataCategoryEntities.stream().filter(item -> item.getId().equals(currentCategory.getParentId()));
            DataCategoryEntity parentCategory = parent.findFirst().orElseGet(DataCategoryEntity::new);
            if (parentCategory.getText() == null) {
                continue;
            }
            map.put(id,parentCategory.getText());
        }
        return map;
    }


    /**
     * 递归查询父节点信息
     * @param id id
     * @param categoryEntities 树结构实体
     * @param parentList  父id list
     * @return
     */
    private static List<DataCategoryEntity> getParentList(final String id, final List<DataCategoryEntity> categoryEntities, final List<DataCategoryEntity> parentList) {
        //获取当前节点信息,id唯一
        final List<DataCategoryEntity> currentCategoryList = categoryEntities.stream().filter(dataCategoryEntity -> dataCategoryEntity.getId().equals(id)).collect(Collectors.toList());
        DataCategoryEntity categoryEntity = new DataCategoryEntity();
        if(CollectionUtils.isNotEmpty(currentCategoryList)){
            categoryEntity = currentCategoryList.get(0);
            parentList.add(categoryEntity);
        }
        //递归继续寻找父节点
        if(!Objects.isNull(categoryEntity.getParentId()) && !CATEGORY_ZERO.equals(categoryEntity.getParentId())) {
            getParentList(categoryEntity.getParentId(), categoryEntities, parentList);
        }
        //父id为0为第一层 直接返回
        return parentList;
    }

    /**
     * 递归寻找子节点信息
     * @param id
     * @param categoryEntities
     * @param childrenList
     * @return
     */
    private static List<DataCategoryEntity> getChildrenList(final String id, final List<DataCategoryEntity> categoryEntities, final List<DataCategoryEntity> childrenList) {
        //查询父id为 输入id的分类信息
        final List<DataCategoryEntity> collect = categoryEntities.stream()
                //过滤完如果是空直接返回
                .filter(dataCategoryEntity -> id.equals(dataCategoryEntity.getParentId())).collect(Collectors.toList());
        //如果不为空 进入循环 子节点为空直接返回
        if(!CollectionUtils.isEmpty(collect)) {
            for (final DataCategoryEntity categoryEntity : collect) {
                childrenList.add(categoryEntity);
                getChildrenList(categoryEntity.getId(), categoryEntities, childrenList);
            }
        }
        return childrenList;
    }

    /**
     * 构建树结构
     * @param categoryEntityArrayList
     * @return
     */
    private static List<ProductTree> buildTreeList(final List<DataCategoryEntity> categoryEntityArrayList) {
        final List<ProductTree> collect = categoryEntityArrayList.stream().map(item -> {
            final String label = item.getText();
            final String value = item.getId();
            final String parentId = item.getParentId();
            final String imgUrl = item.getImgUrl();
            return new ProductTree(value, label, parentId,imgUrl);
        }).collect(Collectors.toList());

        //构建树结构
        return TreeBuilderUtil.buildTreeList(collect);
    }

    /**
     * 递归寻找节点信息,并将信息放到list中
     * @param categoryEntities
     * @param nameList
     * @param id
     * @return
     */
    private static List<String> getProductNameList(final List<DataCategoryEntity> categoryEntities, final List<String> nameList, final String id) {
        final List<DataCategoryEntity> dataCategoryEntities =
                categoryEntities.stream().filter(dataCategoryEntity -> id.equals(dataCategoryEntity.getId())).collect(Collectors.toList());
        final String name = dataCategoryEntities.get(0).getText();
        if(!dataCategoryEntities.get(0).getParentId().equals(CATEGORY_ZERO)){
            nameList.add(name);
            getProductNameList(categoryEntities,nameList,dataCategoryEntities.get(0).getParentId());
        }
        return nameList;
    }

    /**
     * 递归寻找子节点id
     * @param productTreeList
     * @param idList
     * @return
     */
    private static List<String> getProductChildrenIdList(final List<? extends Tree> productTreeList, final List<String> idList){
        for (final Tree p:productTreeList){
            if(CollectionUtils.isEmpty(p.getChildren())) {
                idList.add(p.getValue());
            }
            getProductChildrenIdList(p.getChildren(),idList);
        }
        return idList;
    }


    @Override
    public List<HotProductInfoReqVo> getHotProductInfo() {
        // 查询树集合并过滤出热门产品
        List<DataCategoryEntity> dbList = dataCenterFeignService.categoryList("product", null).getData();
        List<DataCategoryEntity> list = Optional.ofNullable(dbList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item.getIsHot() == 1)
                .collect(Collectors.toList());
        //添加返回信息
        return list.stream().map(item -> {
            final HotProductInfoReqVo reqVo = new HotProductInfoReqVo();
            reqVo.setProductId(item.getId());
            reqVo.setImgUrl(item.getHotImgUrl());
            reqVo.setProductName(item.getText());
            return reqVo;
        }).collect(Collectors.toList());
    }


    @Override
    public List<DataCategoryEntity> queryTreeListByRoleId(final Integer roleId) {
        return this.baseMapper.selectTreeListByRoleId(roleId);
    }

    @Override
    public List<DataCategoryEntity> getIndexProductInfo() {
//        查询首页产品信息
        final QueryWrapper<DataCategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_index",1);
        queryWrapper.last("limit 3");
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public List<Tree> getCategoryTreeByTreeKey(String type) {
        List<DataCategoryEntity> dataCategoryEntities = dataCenterFeignService.categoryList(type,null).getData();
        List<Tree> collect = Optional.ofNullable(dataCategoryEntities).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    Tree tree = new Tree();
                    tree.setValue(item.getId());
                    tree.setLabel(item.getText());
                    tree.setParentId(item.getParentId());
                    // imgUrl 作为 类型字段,用来放 东部农业区 定制（項目上线后属性扩展，用了一个废弃的功能字段）
                    tree.setType(item.getImgUrl());
                    return tree;
                }).collect(Collectors.toList());
        return TreeBuilderUtil.buildTreeList(collect);
    }

}