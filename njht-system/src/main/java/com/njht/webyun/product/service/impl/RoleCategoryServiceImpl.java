package com.njht.webyun.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.enums.NumberEnum;
import com.njht.webyun.product.constant.ProductConstant;
import com.njht.webyun.product.dao.DataCategoryDao;
import com.njht.webyun.product.dao.RoleCategoryDao;
import com.njht.webyun.product.entity.DataCategoryEntity;
import com.njht.webyun.product.entity.RoleCategoryEntity;
import com.njht.webyun.product.service.RoleCategoryService;
import com.njht.webyun.product.vo.CategoryStatusVo;
import com.njht.webyun.product.vo.CategoryVo;
import com.njht.webyun.product.vo.ProductRoleTree;
import com.njht.webyun.utils.TreeBuilderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 18:52
 * @Description: 实现类
 */
@Service("roleCategoryService")
@DS(value = DbConstant.MYSQL_1)
public class RoleCategoryServiceImpl implements RoleCategoryService {


    @Autowired
    private DataCategoryDao dataCategoryDao;

    @Autowired
    private RoleCategoryDao roleCategoryDao;

    @Override
    public List<ProductRoleTree> getCategoryTree(Integer roleId) {
        // 获取所有的产品树列表
        List<DataCategoryEntity> categoryEntityList = dataCategoryDao.selectCategoryList();
        //获取角色对应产品id信息
        List<String> categoryIdList = dataCategoryDao.selectCategoryIdList(roleId);

        //将产品树中存在的数据id的状态设置为1，不存在的数据状态id设置为0
        List<ProductRoleTree> productTreeList = Optional.of(categoryEntityList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    ProductRoleTree productTree = new ProductRoleTree(item.getId(), item.getText(), item.getParentId());
                    //设置勾选状态，角色有的数据 状态勾选为1，否则勾选为0
                    this.setTreeStatus(categoryIdList, productTree);
                    return productTree;
                }).collect(Collectors.toList());

        return TreeBuilderUtil.buildTreeList(productTreeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleCategory(CategoryVo categoryVo) {
        List<CategoryStatusVo> categoryStatusList = categoryVo.getGetCategoryStatusList();
        //状态为1 的 新增 为0的删除
        this.insertOrDeleteCategoryRoleInfo(categoryStatusList,categoryVo.getRoleId());
    }

    /**
     * 新增或者删除 树结构信息
     * @param categoryStatusList
     * @param roleId
     */
    private void insertOrDeleteCategoryRoleInfo(List<CategoryStatusVo> categoryStatusList, Integer roleId) {
        //数据库所有节点id,parentId信息
        List<DataCategoryEntity> dbCategoryList = this.getDbDataCategoryEntities();
        // 角色当前拥有的树id信息
        List<String> authCategoryIdList = roleCategoryDao.selectCategoryIdList(Collections.singletonList(roleId));
        /** 过滤出 勾选状态为1 且数据库表中不存在的数据新增 */
        List<String> insertCategoryIdList = Optional.ofNullable(categoryStatusList).orElse(new ArrayList<>()).stream()
                .filter(item -> ProductConstant.PRODUCT_STATUS_ONE.equals(item.getStatus()) && !authCategoryIdList.contains(item.getCategoryId()))
                .map(CategoryStatusVo::getCategoryId)
                .collect(Collectors.toList());
        // 递归查找父节点 添加到 insertCategoryList 中
        this.getInsertIdList(insertCategoryIdList,dbCategoryList,authCategoryIdList);
        // 新增
        if (!insertCategoryIdList.isEmpty()) {
            List<RoleCategoryEntity> insertRoleCategoryList = insertCategoryIdList.stream().map(item -> new RoleCategoryEntity(roleId, item)).distinct().collect(Collectors.toList());
            roleCategoryDao.insertCategoryRoleList(insertRoleCategoryList);
        }
        // 添加 新增之后的id 到categoryList (该集合表示当前角色 所有有权限的节点)
        authCategoryIdList.addAll(insertCategoryIdList);
        /** 过滤出 勾选状态为0 且数据库表中存在的数据删除 */
        List<String> delCategoryIdList= Optional.ofNullable(categoryStatusList).orElse(new ArrayList<>()).stream()
                .filter(item -> ProductConstant.PRODUCT_STATUS_ZERO.equals(item.getStatus()) && authCategoryIdList.contains(item.getCategoryId()))
                .map(CategoryStatusVo::getCategoryId)
                .collect(Collectors.toList());
        // 递归查找 需要删除的父节点, 添加到del集合中
        this.getDeleteIdList(delCategoryIdList,dbCategoryList,authCategoryIdList);
        // 删除
        if (!delCategoryIdList.isEmpty()) {
            roleCategoryDao.deleteCategoryRoleList(roleId,delCategoryIdList);
        }

    }

    /**
     * 获取需要删除且没选中的节点（父节点）
     * @param delCategoryIdList 要删除的节点集合
     * @param dbCategoryList 数据库所有树节点及其父节点
     * @param authIdList 当前角色有权限的Id集合
     */
    private void getDeleteIdList(List<String> delCategoryIdList, List<DataCategoryEntity> dbCategoryList,List<String> authIdList) {
        List<String> parentIdList = new ArrayList<>(delCategoryIdList);
        // 递归查找每个节点的父节点,当前父节点没有有权限的子节点的添加到删除集合中
        for (String item:parentIdList) {
            this.getDelParentIdList(item,dbCategoryList,delCategoryIdList,authIdList);
        }
    }

    /**
     * 将要删除的父节点信息添加到集合中
     * @param item
     * @param dbCategoryList
     * @param delCategoryIdList
     * @param authIdList
     */
    private void getDelParentIdList(String item, List<DataCategoryEntity> dbCategoryList, List<String> delCategoryIdList,List<String> authIdList) {
        String parentId = this.getParentId(item, dbCategoryList);
        // 不是根节点,且已选节点不包含子节点
        if (!Objects.equals(parentId, String.valueOf(NumberEnum.NUMBER_0.getNum())) && this.isEmptyAuthChildIdList(parentId,dbCategoryList,delCategoryIdList,authIdList)) {
            delCategoryIdList.add(parentId);
            this.getDelParentIdList(parentId,dbCategoryList,delCategoryIdList,authIdList);
        }
    }

    /**
     * 判断当前id 是否包含有权限的子节点
     * @param parentId
     * @param dbCategoryList
     * @param delCategoryIdList
     * @param authIdList
     * @return
     */
    private boolean isEmptyAuthChildIdList(String parentId, List<DataCategoryEntity> dbCategoryList, List<String> delCategoryIdList, List<String> authIdList) {
        // 过滤掉要删除的子节点
        List<String> currentAuthIdList = Optional.of(authIdList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> !delCategoryIdList.contains(item))
                .collect(Collectors.toList());
        // 查询当前父id 是否有没有权限的子节点
        List<String> childIdList = Optional.of(dbCategoryList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> Objects.equals(parentId, item.getParentId()) && currentAuthIdList.contains(item.getId()))
                .map(DataCategoryEntity::getId)
                .collect(Collectors.toList());
        return childIdList.isEmpty();
    }

    /**
     * 获取需要新增且没选中的的父节点信息
     * @param insertCategoryIdList
     * @param dbCategoryList
     */
    private void getInsertIdList(List<String> insertCategoryIdList, List<DataCategoryEntity> dbCategoryList,List<String> authCategoryIdList) {
        List<String> parentIdList = new ArrayList<>(insertCategoryIdList);
        for ( String item:parentIdList) {
            this.getInsertParentIdList(item,dbCategoryList,insertCategoryIdList,authCategoryIdList);
        }
    }

    /**
     * 递归查找没有选中的父节点
     * @param currentId
     * @param dbCategoryList
     * @param insertCategoryIdList
     * @param authCategoryIdList
     */
    private void getInsertParentIdList(String currentId, List<DataCategoryEntity> dbCategoryList,List<String> insertCategoryIdList,List<String> authCategoryIdList) {
        String parentId = this.getParentId(currentId, dbCategoryList);
        // 不是根节点,且该节点还未新增
        if (!Objects.equals(parentId, String.valueOf(NumberEnum.NUMBER_0.getNum()))
                && !insertCategoryIdList.contains(parentId)
                && !authCategoryIdList.contains(parentId)) {
            insertCategoryIdList.add(parentId);
            this.getInsertParentIdList(parentId,dbCategoryList,insertCategoryIdList,authCategoryIdList);
        }
    }

    /**
     * 获取节点父节点Id
     * @param item
     * @param dbCategoryList
     * @return
     */
    private String getParentId(String item, List<DataCategoryEntity> dbCategoryList) {
        // 获取当前节点父节点,判断权限集合是否包含父节点,没有新增,直到父节点为0 时终止
        return Optional.of(dbCategoryList).orElse(new ArrayList<>())
                .stream()
                .filter(dbItem -> Objects.equals(dbItem.getId(), item))
                .findFirst().orElse(new DataCategoryEntity()).getParentId();
    }

    /**
     * 从数据库查询所有的节点信息
     * @return
     */
    private List<DataCategoryEntity> getDbDataCategoryEntities() {
        return dataCategoryDao.selectCategoryList();
    }

    /**
     * 设置产品的勾选状态
     * @param categoryIdList 该角色可查看产品
     * @param productTree 当前产品
     */
    private void setTreeStatus(List<String> categoryIdList, ProductRoleTree productTree) {
        if(!categoryIdList.isEmpty()&&categoryIdList.contains(productTree.getValue())) {
            productTree.setStatus(ProductConstant.PRODUCT_STATUS_ONE);
        } else {
            productTree.setStatus(ProductConstant.PRODUCT_STATUS_ZERO);
        }
    }
}
