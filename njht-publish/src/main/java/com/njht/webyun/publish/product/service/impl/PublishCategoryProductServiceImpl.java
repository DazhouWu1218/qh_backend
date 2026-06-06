package com.njht.webyun.publish.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.publish.product.dao.PublishCategoryProductDao;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.publish.product.entity.PublishCategoryProductEntity;
import com.njht.webyun.publish.product.service.ProductFileInfoService;
import com.njht.webyun.publish.product.service.ProductInfoService;
import com.njht.webyun.publish.product.service.PublishCategoryProductService;
import com.njht.webyun.publish.product.util.IssueUtil;
import com.njht.webyun.publish.product.vo.*;
import com.njht.webyun.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @author daiguojun
 * @date 2022-08-18 16:51:47
 */
@Service("publishCategoryProductService")
public class PublishCategoryProductServiceImpl extends ServiceImpl<PublishCategoryProductDao, PublishCategoryProductEntity> implements PublishCategoryProductService {

    @Autowired
    private ProductInfoService productInfoService;

    @Override
    public CategoryProductReqVo getSearchInfo(String id) {
        // 返回结果
        CategoryProductReqVo reqVo = new CategoryProductReqVo();
        // 查询treeId 为 id 的数据信息
        LambdaQueryWrapper<PublishCategoryProductEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(PublishCategoryProductEntity::getTreeId,id);
        List<PublishCategoryProductEntity> dbList = this.list(qw);
        // 返回结果非空校验
        if ( dbList.isEmpty() ) {
            return reqVo;
        }

        // 类型信息
        List<String> typeList = dbList.stream().filter(item -> !StringUtils.isEmpty(item.getType()))
                .map(PublishCategoryProductEntity::getType).distinct().collect(Collectors.toList());
        reqVo.setTypeList(Optional.of(typeList).orElse(new ArrayList<>()));
        // 名称信息
        List<String> nameList = dbList.stream().filter(item -> !StringUtils.isEmpty(item.getName()))
                .map(PublishCategoryProductEntity::getName).distinct().collect(Collectors.toList());
        reqVo.setNameList(Optional.of(nameList).orElse(new ArrayList<>()));
        // 周期信息
        List<String> cycleList = dbList.stream().map(PublishCategoryProductEntity::getCycle).distinct().collect(Collectors.toList());
        List<CommonEntity> cycleNameList = Optional.of(cycleList).orElse(new ArrayList<>())
                .stream()
                .map(item -> new CommonEntity(item, CycleTypeEnum.getValue(item)))
                .collect(Collectors.toList());

        reqVo.setCycleList(cycleNameList);
        reqVo.setId(id);
        return reqVo;
    }

    @Override
    public PageUtils getMap(CategoryProductSearchVo searchVo) {
        // 查询数据信息
        LambdaQueryWrapper<PublishCategoryProductEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(PublishCategoryProductEntity::getTreeId,searchVo.getId())
                .eq(PublishCategoryProductEntity::getCycle,searchVo.getCycle());
        if (!StringUtils.isEmpty(searchVo.getType())) {
            qw.eq(PublishCategoryProductEntity::getType,searchVo.getType());
        }
        if (!StringUtils.isEmpty(searchVo.getName())) {
            qw.eq(PublishCategoryProductEntity::getName,searchVo.getName());
        }
        PublishCategoryProductEntity entity = this.getOne(qw);

        // productId 为空时返回空map
        if (StringUtils.isEmpty(entity.getProductId())) {
            throw new CommonException("productId为空");
        }

        // 获取查询条件并封装
        ProductInfoVo productInfoVo = new ProductInfoVo();
        productInfoVo.setId(entity.getProductId());
        productInfoVo.setCycleList(Collections.singletonList(entity.getCycle()));
        productInfoVo.setDataSourceList(Collections.singletonList(entity.getSatellite()));
        productInfoVo.setRegionId(searchVo.getRegionId());
        // 只展示已发布的产品信息
        productInfoVo.setIsShow(1);
        productInfoVo.setStartTime(searchVo.getStartTime());
        productInfoVo.setEndTime(searchVo.getEndTime());
        productInfoVo.setPage(searchVo.getPage());
        productInfoVo.setSize(searchVo.getSize());
        // 查询数据并返回
        IPage<ProductInfoEntity> page = productInfoService.getProductInfoEntityIPage(productInfoVo, new PageEntity(searchVo.getPage(), searchVo.getSize()));
        return this.getProductFileInfo(page);
    }


    @Autowired
    private ProductFileInfoService productFileInfoService;

    /**
     * 添加文件返回结果
     * @param page
     */
    private PageUtils getProductFileInfo(IPage<ProductInfoEntity> page) {
        // 分页集合
        PageUtils pageUtils = new PageUtils(page);
        // 产品集合
        List<ProductInfoEntity> records = page.getRecords();
        if (records.isEmpty()) {
            return pageUtils;
        }
        Map<String,List<ProductFileBaseReqVo>>  map = productFileInfoService.getFileInfoListByIds(records);

        List<ProductInfoReqVo> collect = Optional.ofNullable(page.getRecords()).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    ProductInfoReqVo reqVo = new ProductInfoReqVo();
                    BeanUtils.copyProperties(item, reqVo);
                    String issue = IssueUtil.getIssueByMark(reqVo.getIssue(),reqVo.getMark());
                    reqVo.setIssue(issue);
                    reqVo.setDataSource(item.getSatellite());
                    reqVo.setCycleName(CycleTypeEnum.getValue(item.getCycle()));
                    reqVo.setFileInfoList(map.get(item.getId()));
                    return reqVo;
                }).collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }
}