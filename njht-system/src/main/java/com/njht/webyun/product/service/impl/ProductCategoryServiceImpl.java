package com.njht.webyun.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.product.constant.ProductConstant;
import com.njht.webyun.product.dao.DataCategoryDao;
import com.njht.webyun.product.dao.ProductDao;
import com.njht.webyun.product.dto.DataCategoryDto;
import com.njht.webyun.product.entity.DataCategoryEntity;
import com.njht.webyun.product.entity.ProductBaseEntity;
import com.njht.webyun.product.entity.ProductEntity;
import com.njht.webyun.product.service.ProductCategoryService;
import com.njht.webyun.product.vo.ProductInfoTree;
import com.njht.webyun.product.vo.ProductVO;
import com.njht.webyun.system.model.sysDic.SysDic;
import com.njht.webyun.system.service.inf.SysDicService;
import com.njht.webyun.utils.TreeBuilderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springfox.documentation.annotations.Cacheable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 11:17
 * @Description: 树结构
 */
@Service("productCategoryService")
@Slf4j
@DS(value = DbConstant.MYSQL_1)
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private DataCategoryDao dataCategoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private SysDicService sysDicService;

    @Override
    @Cacheable(value = "system-category-list")
    public List<ProductInfoTree> getProductInfoTreeList() {
        //集合查所有信息，不区分是否热门 是否首页轮播
        List<DataCategoryDto> categoryEntityList = dataCategoryDao.selectCategoryAndProductList(null,null,null);
        // category对象集合 转换成 Tree 对象集合
        List<ProductInfoTree> collect = productCategoryService.categoryListToTreeList(categoryEntityList);
        return TreeBuilderUtil.buildTreeList(collect);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "system-category-list")
    public void editProductInfo(ProductVO productVO) {
        if (StringUtils.isEmpty(productVO.getId())) {
            throw new CommMsgException("修改id不能为空");
        }
        /** 树结构信息修改*/
        DataCategoryEntity dataCategoryEntity = this.getDataCategoryInfo(productVO);
        dataCategoryEntity.setUpdateTime(new Date());
        dataCategoryDao.update(dataCategoryEntity);
        log.info("============>>>>>>>>>>树结构信息修改成功==============");

        /** 产品信息修改*/
        ProductEntity productEntity = this.getProductInfo(productVO);
        productEntity.setUpdateTime(new Date());
        productDao.update(productEntity);
        log.info("============>>>>>>>>>>产品信息修改成功==============");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "system-category-list")
    public void addProductInfo(ProductVO productVO) {
        /** 1.树结构信息入库 */
        log.info("===============>>>>>>>开始入库===========");
        DataCategoryEntity dataCategoryEntity = this.getDataCategoryInfo(productVO);
        // 对象中添加 id,创建修改信息等基本属性
        this.setBaseInfo(dataCategoryEntity);
        // 根据patent Id 获取父级分级信息
        String menuInfo = this.getMenuAndTextInfoById(productVO.getParentId());
        dataCategoryEntity.setMenu(menuInfo);

        //入库
        dataCategoryDao.insert(dataCategoryEntity);
        log.info("============>>>>>>>>>>树结构信息入库成功==============");

        /** 2.产品新增*/
        ProductEntity productEntity = this.getProductInfo(productVO);
        this.setBaseInfo(productEntity);
        // 树结构id
        productEntity.setTreeId(dataCategoryEntity.getId());
        productDao.insert(productEntity);
        log.info("============>>>>>>>>产品信息入库成功=================");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "system-category-list")
    public void delProductInfo(String id) {
        /**删除树结构本身以及对应的子集信息*/
        List<DataCategoryEntity> childList = dataCategoryDao.selectChildCategoryListById(id);

        if (childList.isEmpty()) {
            // 删除 （如果该产品目录下有具体产品则不能删除）
            Integer count = productDao.selectCountByProductIdList(Collections.singletonList(id));
            if (count != 0) {
                throw new CommMsgException("该目录级别下存在产品数据，不能被删除");
            }
            // 获取id 以及 子集信息 根据id删除树结构信息
            dataCategoryDao.deleteByIds(Collections.singletonList(id));
            log.info("============>>>>>>>>>>>树结构信息删除成功==============");
            productDao.deleteByIds(Collections.singletonList(id));
            log.info("============>>>>>>>>>>>产品信息删除成功==============");
        } else {
            throw new CommMsgException("该目录存在子级,请先删除子级目录");
        }
    }

    @Override
    public List<ProductInfoTree> categoryListToTreeList(List<DataCategoryDto> categoryEntityList) {
        return Optional.ofNullable(categoryEntityList)
                .orElse(new ArrayList<>())
                .stream().map(dataCategoryDto -> {
                    ProductInfoTree tree = new ProductInfoTree();
                    tree.setValue(dataCategoryDto.getId());
                    tree.setLabel(dataCategoryDto.getText());
                    // 处理周期信息
                    if (dataCategoryDto.getCycle()!=null) {
                        List<String> cycleList = Arrays.asList(dataCategoryDto.getCycle().split(","));
                        tree.setCycleList(cycleList);
                    }
                    BeanUtils.copyProperties(dataCategoryDto, tree);
                    return tree;
                }).collect(Collectors.toList());
    }

    @Override
    public List<CommonEntity> getCycleList() {
        List<CommonEntity> commonEntityList = new ArrayList<>();
        // 将枚举信息塞到对象中并返回给前端
        for (CycleTypeEnum st : CycleTypeEnum.values()) {
            CommonEntity entity = new CommonEntity(st.getKey(),st.getValue());
            commonEntityList.add(entity);
        }
        return commonEntityList;
    }

    @Override
    public List<CommonEntity> getIdentifyList() {
        List<CommonEntity> commonEntityList = new ArrayList<>();
        // 将枚举信息塞到对象中并返回给前端
        SysDic dic = new SysDic();
        dic.setDicType("CATEGORY_TREE");
        List<SysDic> dicList = new ArrayList<>();
        try {
            dicList = sysDicService.getValuesByType(dic);
        } catch (Exception e) {
            throw new CommMsgException(e.getMessage());
        }

        for (SysDic item:dicList) {
            CommonEntity entity = new CommonEntity(item.getDicKey(),item.getDicValue());
            commonEntityList.add(entity);
        }
        return commonEntityList;
    }


    /**
     * 获取产品相关信息
     * @param productVO 前端传参
     * @return
     */
    private ProductEntity getProductInfo(ProductVO productVO) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productVO,productEntity);
        String cycle = String.join(",", Optional.ofNullable(productVO.getCycleList()).orElse(new ArrayList<>()));
        productEntity.setCycle(cycle);
        return productEntity;
    }

    /**
     * 从前端传递参数中获取树结构的基本信息
     * @param productVO 前端传参
     * @return
     */
    private DataCategoryEntity getDataCategoryInfo(ProductVO productVO) {
        DataCategoryEntity dataCategoryEntity = new DataCategoryEntity();
        BeanUtils.copyProperties(productVO, dataCategoryEntity);
        dataCategoryEntity.setText(productVO.getName());
        return dataCategoryEntity;
    }

    /**
     * 对象中存储对象基本信息
     * @param t
     * @param <T>
     */
    private <T> void setBaseInfo(T t) {
        String id = UUID.randomUUID().toString().replace("-","");
        Date date = new Date();
        // 第一次新增，版本为0
        ProductBaseEntity base = new ProductBaseEntity(id,date,date,0,",");
        BeanUtils.copyProperties(base,t);
    }


    /**
     * 获取父id 相关的树结构信息
     * @param id
     * @return
     */
    public String getMenuAndTextInfoById(String id) {
        // 返回值
        StringBuilder menuLevel = new StringBuilder();
        if (StringUtils.isEmpty(id) || Objects.equals(id,ProductConstant.PRODUCT_BASE_ID)){
            //新增根节点时,menu为空
            return menuLevel.toString();
        }
        // 根据id查询父结构相关信息
        DataCategoryEntity dataCategoryEntity = dataCategoryDao.selectMenuInfoById(id);
        String menu = dataCategoryEntity.getMenu();
        String text = dataCategoryEntity.getText();
        if ( !StringUtils.isEmpty(menu) ) {
            menuLevel.append(menu);
            menuLevel.append(">");
        }
        menuLevel.append(text);
        return menuLevel.toString();
    }

}
