package com.njht.webyun.business.report.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.entity.base.BaseEntity;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.entity.dataReport.DataReportEntity;
import com.njht.entity.product.ProductEntity;
import com.njht.webyun.business.common.util.TimeUtil;
import com.njht.webyun.business.feign.DataCenterFeignService;
import com.njht.webyun.business.index.constant.IndexConstant;
import com.njht.webyun.business.index.vo.TimeParam;
import com.njht.webyun.business.report.constant.ReportConstant;
import com.njht.webyun.business.report.dao.DataReportDao;
import com.njht.webyun.business.report.enums.ReportEnum;
import com.njht.webyun.business.report.service.DataReportService;
import com.njht.webyun.business.report.vo.*;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.enums.IdentifyTypeEnum;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 数据下载信息统计表
 * @author daiguojun
 * @date 2022-08-16 15:49:24
 */
@Service("dataReportService")
@DS(value = DbConstant.MYSQL_1)
public class DataReportServiceImpl extends ServiceImpl<DataReportDao, DataReportEntity> implements DataReportService {

    @Autowired
    private DataCenterFeignService dataCenterFeignService;

    @Override
    public List<DataReportEntity> dataRate(String type, String identify) {
        // 查询数据
        LambdaQueryWrapper<DataReportEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataReportEntity::getIdentify,identify);
        queryParam(qw,type);
        return list(qw);
    }

    @Override
    public PageUtils queryDataReportList(DataDetailReportVo dataDetailReportVo) {
        // 分页查询数据信息
        LambdaQueryWrapper<DataReportEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataReportEntity::getDataId,dataDetailReportVo.getId());
        queryParam(qw,dataDetailReportVo.getType());
        IPage<DataReportEntity> page = page(
                new Query<DataReportEntity>().getPage(dataDetailReportVo),
                qw
        );
        // 分页对象
        PageUtils pageUtils = new PageUtils(page);
        // 返回结果添加 名称，数据完整比率等信息
        List<DataDetailReportReqVo> collect = Optional.ofNullable(page.getRecords()).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    DataDetailReportReqVo reqVo = new DataDetailReportReqVo();
                    BeanUtils.copyProperties(item, reqVo);
                    // 获取当前期次文件到报率 (当前个数/总个数)
                    String rate = String.format("%.5f", (double) item.getFileNum() / (double) item.getSumNum());
                    reqVo.setRate(rate);
                    reqVo.setName(item.getDataName());
                    reqVo.setStatusName(ReportEnum.getDesc(reqVo.getStatus()));
                    // 计算数据 大小 单位MB
                    reqVo.setFileSize(getFileSize(item.getFileSize()));
                    return reqVo;
                })
                .sorted(Comparator.comparing(DataDetailReportReqVo::getIssue).thenComparing(DataReportReqVo::getName).reversed())
                .collect(Collectors.toList());

        pageUtils.setList(collect);
        return pageUtils;
    }

    /**
     * 抽取公共部分查询
     * @param qw
     */
    private static void queryParam(LambdaQueryWrapper<DataReportEntity> qw, String type) {
        // 获取开始结束时间 (如果type 为空只查询当天信息)
        TimeParam timeInfo = TimeUtil.getTimeInfo(type);
        qw.between(BaseEntity::getUpdateTime,timeInfo.getBeginDate(),timeInfo.getEndDate());
        qw.orderByDesc(BaseEntity::getUpdateTime);
    }

    @Override
    public PageUtils queryDataProducerList(ProductReportVo reportVo) {
        // 查询数据报告信息
        LambdaQueryWrapper<DataReportEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataReportEntity::getStatus,reportVo.getStatus()).eq(DataReportEntity::getIdentify, reportVo.getType());
        if ( !StringUtils.isEmpty(reportVo.getProductId()) ) {
            // 获取改id 对应的子集产品id
            List<String> idList = getChildIdListByParentId(reportVo.getProductId());
            if (!idList.isEmpty()) qw.in(DataReportEntity::getDataId, idList);
        }
        // 默认查询当天信息
        queryParam(qw, IndexConstant.INDEX_ONE);
        IPage<DataReportEntity> page = page(
                new Query<DataReportEntity>().getPage(reportVo),
                qw
        );
        PageUtils pageUtils = new PageUtils(page);
        // 添加数据返回结果
        List<ProductReportReqVo> collect = Optional.ofNullable(page.getRecords()).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    ProductReportReqVo reportReqVo = new ProductReportReqVo();
                    BeanUtils.copyProperties(item, reportReqVo);
                    reportReqVo.setCycleName(CycleTypeEnum.getValue(item.getCycle()));
                    String rate = String.format("%.5f", (double) item.getFileNum() / (double) item.getSumNum());
                    reportReqVo.setRate(rate);
                    // 添加数据名称
                    reportReqVo.setName(item.getDataName());
                    return reportReqVo;
                })
                .sorted(Comparator.comparing(ProductReportReqVo::getIssue).thenComparing(DataReportReqVo::getName).reversed())
                .collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public List<CommonEntity> queryProductList() {
        List<DataCategoryEntity> entityList = dataCenterFeignService.categoryList(ReportConstant.DATA_PRODUCT, ReportConstant.CATEGORY_PARENT_ID).getData();
        List<CommonEntity> collect = Optional.ofNullable(entityList).orElse(new ArrayList<>())
                .stream()
                .map(item -> new CommonEntity(item.getId(), item.getText()))
                .collect(Collectors.toList());
        collect.add(0,new CommonEntity(ReportConstant.CATEGORY_PARENT_ID,ReportConstant.CATEGORY_ALL_NAME));
        return collect;
    }

    @Override
    public List<DataReportEntity> queryDataList() {
        // 根据标识查询当天的数据
        LambdaQueryWrapper<DataReportEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(DataReportEntity::getIdentify, IdentifyTypeEnum.DATA_COLLECTION.getKey());
        queryParam(qw,IndexConstant.INDEX_ONE);
        return list(qw);
    }

    @Override
    public List<DataReportEntity> queryDiProductList() {
        LambdaQueryWrapper<DataReportEntity> qw = new LambdaQueryWrapper<>();
        qw.and(itemQw -> itemQw.eq(DataReportEntity::getIdentify, IdentifyTypeEnum.BUSINESS_PRODUCT.getKey())
                .or().eq(DataReportEntity::getIdentify,IdentifyTypeEnum.PRE_DATA.getKey()));
        queryParam(qw,IndexConstant.INDEX_ONE);
        return list(qw);
    }

    /**
     * 获取子集id集合
     * @param parentId
     * @return
     */
    private List<String> getChildIdListByParentId(String parentId) {
        // 数据中心拿出数据并根据id 过滤出子集信息
        List<DataCategoryEntity> dbCategoryList = dataCenterFeignService.categoryList(ReportConstant.DATA_PRODUCT, null).getData();
        // 获取产品基本信息集合
        List<ProductEntity> productEntityList = dataCenterFeignService.productList().getData();
        // 获取parentId 获取数据信息
        return this.getChildrenIdList(parentId, dbCategoryList,productEntityList, new ArrayList<>());

    }
    /**
     * 递归寻找子节点信息
     * @param id
     * @param categoryEntities
     * @param childIdList
     * @return
     */
    private List<String> getChildrenIdList(final String id, final List<DataCategoryEntity> categoryEntities, List<ProductEntity> productEntityList,final List<String> childIdList) {
        //查询父id为 输入id的分类信息
        final List<DataCategoryEntity> collect = categoryEntities.stream()
                //过滤完如果是空直接返回
                .filter(dataCategoryEntity -> id.equals(dataCategoryEntity.getParentId())).collect(Collectors.toList());
        //如果不为空 进入循环 子节点为空 将节点添加到childIdList并返回
        if(!CollectionUtils.isEmpty(collect)) {
            for (final DataCategoryEntity categoryEntity : collect) {
                getChildrenIdList(categoryEntity.getId(), categoryEntities,productEntityList, childIdList);
            }
        } else {
            // 根据treeId获取productId
            String productId
                    = productEntityList.stream().filter(item -> item.getTreeId().equals(id)).findFirst().orElse(new ProductEntity()).getId();
            childIdList.add(productId);
        }
        return childIdList;
    }

    /**
     * 文件大小转换 将byte 转换成mb
     * @param fileSize
     * @return
     */
    public static String getFileSize(Long fileSize) {
        BigDecimal byteBigDecimal = new BigDecimal(fileSize);
        BigDecimal gbBigDecimal = byteBigDecimal.divide(new BigDecimal(1024L * 1024L), 2, BigDecimal.ROUND_FLOOR);
        if (gbBigDecimal.equals(new BigDecimal(0))) return "0.00";
        return gbBigDecimal.toString();
    }


}