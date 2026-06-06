package com.njht.webyun.publish.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.publish.product.dao.ProductDao;
import com.njht.webyun.publish.product.dao.ProductInfoDao;
import com.njht.webyun.publish.product.entity.ProductEntity;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.publish.product.service.DataCategoryService;
import com.njht.webyun.publish.product.service.ProductService;
import com.njht.webyun.publish.product.vo.ProductCycleReqVo;
import com.njht.webyun.publish.product.vo.ProductSearchVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品服务
 * @author
 */
@Service("productService")
@DS(value = DbConstant.MYSQL_1)
public class ProductServiceImpl extends ServiceImpl<ProductDao, ProductEntity> implements ProductService {

    @Autowired
    private DataCategoryService dataCategoryService;

    @Autowired
    private ProductInfoDao productInfoDao;

    /**
     * 拿到productId 获取查询条件
     * //id为菜单最后一级的id
     * 1.存在子菜单的情况，拿子菜单的id查产品id
     * 2.没有子菜单，直接拿id查产品id
     * @param id
     * @return
     */
    @Override
    public List<ProductSearchVo> getProductSearchInfo(String id) {
        //获取最后一级id，以及每一级的产品名称
        Map<String, List<String>> map = dataCategoryService.getProductIdAndNames(id);
        //根据最后一级id拿到产品分类，数据源相关信息并返回
        List<String> idList = map.get("ids");
        if(CollectionUtils.isNotEmpty(idList)){
            // 分类id
            idList =  this.getProductIdList(idList);
        }else{
            idList= Arrays.asList(id);
        }
        map.put("ids",idList);
        return this.getSearchInfos(map);
    }

    @Override
    public List<String> getProductIdList(List<String> idList) {
        QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("tree_id",idList);
        List<ProductEntity> productEntityList = this.list(queryWrapper);
        //产品id
        return productEntityList.stream().map(ProductEntity::getId).collect(Collectors.toList());
    }



    /**
     * 根据最后一级id拿到产品分类，数据源相关信息并返回
     * @param map
     * @return
     */
    private List<ProductSearchVo> getSearchInfos(Map<String, List<String>> map) {
        //获取所有产品id信息
        List<String> idList = map.get("ids");
        //根据产品 id 查询周期以及对应的数据源信息
        List<ProductInfoEntity> searchInfoList = productInfoDao.selectCycleAndNameInfo(idList);
        //根据productId分组，把数据源塞进去
        Map<String, List<ProductInfoEntity>> productMap
                = searchInfoList.stream().collect(Collectors.groupingBy(ProductInfoEntity::getProductId));
        //设置返回信息·
        List<ProductSearchVo> productSearchVos = new ArrayList<>();
        productMap.forEach((key, value) -> {
            ProductSearchVo productSearchVo = new ProductSearchVo();

            productSearchVo.setId(key);
            productSearchVo.setName(value.get(0).getName());
            // 数据源集合
            List<String> satelliteList = value.stream().map(ProductInfoEntity::getSatellite).distinct().collect(Collectors.toList());
            // 周期集合
            List<ProductCycleReqVo> cycleList = this.getCycleList(value);
            productSearchVo.setCycleList(cycleList);
            productSearchVo.setDataSourceList(satelliteList);
            productSearchVos.add(productSearchVo);
        });

        return productSearchVos;
    }

    /**
     * 获取周期集合
     * @param value
     * @return
     */
    private List<ProductCycleReqVo> getCycleList(List<ProductInfoEntity> value) {
        // 周期先去重
        List<String> cycleList = value.stream().map(ProductInfoEntity::getCycle).distinct().collect(Collectors.toList());
        // 根据权重排序并返回
        return Optional.of(cycleList)
                .orElse(new ArrayList<>())
                .stream()
                .map(cycle -> new ProductCycleReqVo(CycleTypeEnum.getValue(cycle),cycle,CycleTypeEnum.getSort(cycle)))
                .sorted(Comparator.comparing(ProductCycleReqVo::getSort))
                .collect(Collectors.toList());
    }

}