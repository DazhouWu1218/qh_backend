package com.htht.executor.statistics.base;


import com.htht.executor.fileStatistic.service.DataBaseInfoService;
import com.htht.executor.product.service.ProductService;
import com.htht.executor.statistics.constant.FileStatisticsTypeConstant;
import com.htht.executor.statistics.entity.CategoryProductDto;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/29 14:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductBaseCfgToDbService {

    @Autowired
    public ProductService productService;

    @Autowired
    private DataBaseInfoService dataBaseInfoService;


    /**
     * 递归寻找子节点id
     * @param dbCategoryList
     * @param idList
     * @return
     */
    private static List<String> getProductChildrenIdList(List<CategoryProductDto> dbCategoryList,List<String> parentIdList, final List<String> idList){

        for (final CategoryProductDto p:dbCategoryList){
            // 判断当前id是否为父Id,如果不是,则为子节点
            if(!parentIdList.contains(p.getCategoryId())) {
                idList.add(p.getCategoryId());
            }
        }
        return idList;
    }


    /**
     * product表中信息入库base_info方法具体实现
     * @param triggerParam
     * @return
     */
    public ReturnT execute(TriggerParam triggerParam) {
        ReturnT<String> result = new ReturnT<>();
        try {
            //取出产品树结构的所有分支中最后一个节点
            List<CategoryProductDto> productInfoList = Optional.ofNullable(productService.selectProductBaseCfgInfo(FileStatisticsTypeConstant.SOURCE_PRODUCT)).orElse(new ArrayList<>());
            // 过滤父Id信息
            List<String> parentIdList = Optional.of(productInfoList).orElse(new ArrayList<>())
                    .stream()
                    .map(CategoryProductDto::getParentId)
                    .distinct()
                    .collect(Collectors.toList());
            //递归查找最后一级叶子节点
            List<String> lastNodeCategoryIdList = new ArrayList<>();
            getProductChildrenIdList(productInfoList,parentIdList,lastNodeCategoryIdList);
            List<CategoryProductDto> lastNodeInfoList = productInfoList.stream().filter(item -> lastNodeCategoryIdList.contains(item.getCategoryId())).collect(Collectors.toList());
            //查询BASE已经存在的product配置信息，去重
            List<String> existBaseProductIdList = dataBaseInfoService.selectBaseInfoId(FileStatisticsTypeConstant.SOURCE_PRODUCT);
            lastNodeInfoList = lastNodeInfoList.stream().filter(item -> !existBaseProductIdList.contains(item.getProductId())).collect(Collectors.toList());
            //base表中批量插入product基本配置信息
            List<DataBaseInfoEntity> dataBaseInfoEntityList = new ArrayList<>();
            if(!lastNodeInfoList.isEmpty()) {
                XxlJobHelper.log("找到需要新增{}个Product配置信息",lastNodeInfoList.size());
                for (CategoryProductDto item : lastNodeInfoList) {
                    DataBaseInfoEntity dataBaseInfoEntity = new DataBaseInfoEntity();
                    dataBaseInfoEntity.setId(item.getProductId());
                    dataBaseInfoEntity.setVersion(0);
                    dataBaseInfoEntity.setName(item.getName());
                    dataBaseInfoEntity.setSumNum(0L);
                    dataBaseInfoEntity.setIdentify(FileStatisticsTypeConstant.SOURCE_PRODUCT);
                    dataBaseInfoEntity.setCorrectionTime(0);
                    //周期不分开
                    dataBaseInfoEntity.setCycle(item.getCycle());
                    dataBaseInfoEntityList.add(dataBaseInfoEntity);
                }
            }else {
                result.setMsg("没有需要新增的Product配置信息");
                XxlJobHelper.log("没有需要新增的Product配置信息");
            }
            if (!dataBaseInfoEntityList.isEmpty()) {
                dataBaseInfoService.insertAll(dataBaseInfoEntityList);
                result.setMsg("产品配置入库成功");
            }
            result.setCode(ReturnT.SUCCESS_CODE);
        }catch (Exception e){
            XxlJobHelper.log("出现异常：" + e);
            result.setCode(ReturnT.FAIL_CODE);
            throw e;
        }
        return result;
    }
}
