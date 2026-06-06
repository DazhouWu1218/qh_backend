package com.njht.webyun.publish.behavior.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.publish.behavior.constant.BehaviorConstant;
import com.njht.webyun.publish.behavior.dao.BehaviorLogDao;
import com.njht.webyun.publish.behavior.dto.BehaviorLogDto;
import com.njht.webyun.publish.behavior.entity.BehaviorDownloadLogEntity;
import com.njht.webyun.publish.behavior.entity.BehaviorLogEntity;
import com.njht.webyun.publish.behavior.service.BehaviorDownloadLogService;
import com.njht.webyun.publish.behavior.service.BehaviorLogService;
import com.njht.webyun.publish.behavior.vo.BehaviorLogDownLoadReqVo;
import com.njht.webyun.publish.behavior.vo.BehaviorLogReqVo;
import com.njht.webyun.publish.behavior.vo.StatisticSearchVo;
import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.utils.DateFormatUtils;
import com.njht.webyun.utils.PageUtil;
import com.njht.webyun.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author daiguojun
 */
@Service("behaviorLogService")
@Slf4j
public class BehaviorLogServiceImpl extends ServiceImpl<BehaviorLogDao, BehaviorLogEntity> implements BehaviorLogService {

    @Autowired
    private BehaviorDownloadLogService behaviorDownloadLogService;

    @Override
    public PageUtils queryPage(StatisticSearchVo statisticSearchVo) {
        //用户访问行为相关信息
        Set<Integer> set = new TreeSet<>();
        // 用户行为为 3 时 查全部
        if(statisticSearchVo.getAction()!=null && !statisticSearchVo.getAction().equals(BehaviorConstant.THREE)){
            set.add(statisticSearchVo.getAction());
        }
        // roleId 为 -1 时查全部
        if(BehaviorConstant.MINUS_ONE.equals(statisticSearchVo.getRoleId())){
            statisticSearchVo.setRoleId(null);
        }
        //获取数据的返回结果，并设置返回给前端的结果 分页
        PageUtil.setPageAndSize(statisticSearchVo.getPage(),statisticSearchVo.getSize(),1,10);
        List<BehaviorLogDto> records =
                baseMapper.selectBehaviorLogByPage(statisticSearchVo.getOrgId(),statisticSearchVo.getRoleId(),statisticSearchVo.getSearch(),
                        statisticSearchVo.getStartTime(),statisticSearchVo.getEndTime(),set);
        PageUtils pageUtils = new PageUtils(new PageInfo<>(records));
        List<BehaviorLogReqVo> collect = records.stream().map(item -> {
            BehaviorLogReqVo reqVo = new BehaviorLogReqVo();
            BeanUtils.copyProperties(item,reqVo);
            reqVo.setTime(DateFormatUtils.dateToStr(item.getCreatedDate(),DateFormatUtils.formatYY_MM_dd_HH_mm));
            List<BehaviorDownloadLogEntity> downloadLogEntityList = item.getDownloadLogEntityList();
            List<BehaviorLogDownLoadReqVo> downLoadReqVoList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(downloadLogEntityList)){
                for(BehaviorDownloadLogEntity entity:downloadLogEntityList){
                    BehaviorLogDownLoadReqVo downLoadReqVo = new BehaviorLogDownLoadReqVo();
                    BeanUtils.copyProperties(entity,downLoadReqVo);
                    downLoadReqVo.setCycleName(CycleTypeEnum.getValue(entity.getCycle()));
                    downLoadReqVo.setCreatedDate(DateFormatUtils.dateToStr(entity.getCreatedDate(),DateFormatUtils.formatYY_MM_dd_HH_mm));
                    downLoadReqVo.setProductType(entity.getFileType());
                    downLoadReqVo.setProductTime(DateFormatUtils.strToDateStr(entity.getIssue(),
                            DateFormatUtils.formatYYMMddHHmm,DateFormatUtils.formatYY_MM_dd_HH_mm));
                    downLoadReqVoList.add(downLoadReqVo);
                }
                reqVo.setDownLoadReqVoList(downLoadReqVoList);
            }
            return reqVo;
        }).sorted(Comparator.comparing(BehaviorLogReqVo::getTime).reversed()).collect(Collectors.toList());

        pageUtils.setList(collect);
        return pageUtils;
    }


    /**
     * 按照期次入库
     * @param fileInfoEntityList
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "productCountInfoList",allEntries=true)
    public void saveProductDownLoadInfo(List<ProductFileInfoEntity> fileInfoEntityList,Integer userId,List<ProductInfoEntity> productInfoEntities,List<DataCategoryEntity> categoryEntities) {
        // 1.区分日志表和下载日志表（先往日志表里记录，判断日志表中是否有该用户当天记录，有修改时间,下载次数+1,没有新增）
        // 根据用户id以及下载行为还有日期判断日志表是否有记录
        BehaviorLogEntity entity = baseMapper.selectBehaviorLogByUserIdAndNowDate(userId);
        if(entity != null){
            // 用户当天下载日志信息不为null 修改下载次数 以及最后修改时间
            entity.setNum(entity.getNum()+1);
            this.updateById(entity);
            log.info("============用户下载记录添加日志：修改用户 [{}] 日志信息 当天已下载了[{}]次",userId,entity.getNum());
        }else{
            //行为1 代表下载
            entity = new BehaviorLogEntity();
            entity.setAction(1);
            entity.setCreatedBy(userId);
            entity.setLastUpdatedBy(userId);
            entity.setDeleted(0);
//            第一次新增下载次数为1
            entity.setNum(1);
            this.save(entity);
            log.info("============用户下载记录添加日志：新增用户 [{}] 日志信息 当天第1次下载",userId);
        }
        // 保存用户下载产品信息 （1-n）
        // 1.按照期次给数据归类
        Map<String, List<ProductFileInfoEntity>> map =
                fileInfoEntityList.stream().collect(Collectors.groupingBy(ProductFileInfoEntity::getProductInfoId));
        // 根据 id 分组
        Map<String, List<ProductInfoEntity>> productMap = productInfoEntities.stream().collect(Collectors.groupingBy(ProductInfoEntity::getId));
        // 3.统计大小
        List<BehaviorDownloadLogEntity> entityList = new ArrayList<>();
        BehaviorLogEntity finalEntity = entity;
        map.forEach((s, productFileInfoEntities) -> {
            ProductInfoEntity productInfoEntity = productMap.get(s).get(0);
            BehaviorDownloadLogEntity downloadLogEntity = new BehaviorDownloadLogEntity();
            BeanUtils.copyProperties(productInfoEntity,downloadLogEntity);
            //文件个数
            downloadLogEntity.setFileNum(productFileInfoEntities.size());
            //文件大小
            Long fileSize = productFileInfoEntities.stream().mapToLong(ProductFileInfoEntity::getFileSize).sum();
            downloadLogEntity.setFileSize(fileSize);
            //文件类型
            String fileType = productFileInfoEntities.stream().map(ProductFileInfoEntity::getFileType).distinct().collect(Collectors.joining(","));
            downloadLogEntity.setFileType(fileType);
            //改条数据所属分类 menu id
            downloadLogEntity.setProduceName(this.getNameByProductId(productInfoEntity.getTreeId(),productInfoEntity.getName(),categoryEntities));
            downloadLogEntity.setMenuId(this.getMenuIdByProductId(productInfoEntity.getProductId(),productInfoEntity.getTreeId(),categoryEntities));
            downloadLogEntity.setIssue(productFileInfoEntities.get(0).getIssue());
            downloadLogEntity.setBehaviorId(finalEntity.getBehaviorId());
            downloadLogEntity.setCreatedBy(userId);
            downloadLogEntity.setLastUpdatedBy(userId);
            downloadLogEntity.setDeleted(0);
            entityList.add(downloadLogEntity);
        });
        behaviorDownloadLogService.saveBatch(entityList);
        log.info("=================用户下载记录添加下载日志表： {} 下载了 {} 期产品==================",userId,entityList.size());
    }

    @Override
    public Integer queryViewCountInfo() {
        return baseMapper.selectViewCountInfo();
    }


    /**
     * 获取产品所属分类id
     * @param productId
     * @param treeId
     * @return
     */
    private String getMenuIdByProductId(String productId,String  treeId,List<DataCategoryEntity> categoryEntityList) {
        //通过productId 找到tree_id
//        ProductEntity productEntity = productService.getById(productId);
        productId = this.getMenuId(treeId,new ArrayList<>(),categoryEntityList).get(0);
        return productId;
    }

    /**
     * 获取分类id 信息
     * @param id
     * @return
     */
    private List<String> getMenuId(String id, List<String> treeIdList, List<DataCategoryEntity> list) {
        // 递归找到父id为0的节点id并返回
        String finalId = id;
        List<DataCategoryEntity> collect = list.stream().filter(dataCategoryEntity -> dataCategoryEntity.getId().equals(finalId)).collect(Collectors.toList());
        DataCategoryEntity entity = collect.get(0);
        //父id为0 返回
        if(!entity.getParentId().equals(BehaviorConstant.STR_ZERO)){
            getMenuId(entity.getParentId(),treeIdList,list);
        }else{
            treeIdList.add(entity.getId());
        }
        return treeIdList;
    }

    /**
     * 获取产品的父级名称
     *
     * @param id
     * @param name
     * @return
     */
    private String getNameByProductId(String id, String name,List<DataCategoryEntity> categoryEntityList) {
        DataCategoryEntity currentCategory = this.getCategoryInfoById(id,categoryEntityList);
        String parentName = this.getCategoryInfoById(currentCategory.getParentId(),categoryEntityList).getText();
        return parentName+"_"+name;
    }

    /**
     * 获取树节点
     * @param id
     * @param categoryEntityList
     * @return
     */
    private DataCategoryEntity getCategoryInfoById(String id, List<DataCategoryEntity> categoryEntityList) {
        return Optional.of(categoryEntityList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst().orElse(new DataCategoryEntity());
    }
}