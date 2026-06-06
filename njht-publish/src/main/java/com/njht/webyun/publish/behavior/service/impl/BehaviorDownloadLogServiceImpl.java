package com.njht.webyun.publish.behavior.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.entity.base.BaseEntity;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.publish.behavior.constant.BehaviorConstant;
import com.njht.webyun.publish.behavior.dao.BehaviorDownloadLogDao;
import com.njht.webyun.publish.behavior.entity.BehaviorDownloadLogEntity;
import com.njht.webyun.publish.behavior.service.BehaviorDownloadLogService;
import com.njht.webyun.publish.behavior.service.BehaviorLogService;
import com.njht.webyun.publish.behavior.vo.BehaviorProductCycleReqVo;
import com.njht.webyun.publish.behavior.vo.BehaviorProductInfoReqVo;
import com.njht.webyun.publish.behavior.vo.CountInfoReqVo;
import com.njht.webyun.publish.behavior.vo.ProductCountReqVo;
import com.njht.webyun.publish.feign.DataCenterFeignService;
import com.njht.webyun.publish.product.constant.ProductConstant;
import com.njht.webyun.publish.product.service.DataCategoryService;
import com.njht.webyun.publish.sys.service.UserService;
import com.njht.webyun.utils.DateFormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Administrator
 */
@Service("behaviorDownloadLogService")
public class BehaviorDownloadLogServiceImpl extends ServiceImpl<BehaviorDownloadLogDao, BehaviorDownloadLogEntity> implements BehaviorDownloadLogService {

    @Autowired
    private DataCategoryService dataCategoryService;

    @Autowired
    private DataCenterFeignService dataCenterFeignService;

    @Autowired
    private UserService userService;

    @Autowired
    private BehaviorLogService behaviorLogService;


    @Override
    public List<CountInfoReqVo> getStatisticCountInfo() {
        // 0-用户访问量，1-数据下载量，2-用户注册量 （区分用户注册量和其他）
        List<CountInfoReqVo> reqVos = new ArrayList<>();

        //用户访问量
        Integer viewCount = behaviorLogService.queryViewCountInfo();
        CountInfoReqVo viewCountReqVo = this.getCountReqVo(viewCount, BehaviorConstant.ZERO);
        reqVos.add(viewCountReqVo);

        // 用户下载量
        Integer fileCount = baseMapper.selectFileCountInfo();
        CountInfoReqVo downLoadCountReqVo = this.getCountReqVo(fileCount, BehaviorConstant.ONE);
        reqVos.add(downLoadCountReqVo);

        // 统计用户注册量
        Integer userCount=  userService.count();
        CountInfoReqVo reqVo = this.getCountReqVo(userCount,BehaviorConstant.TWO);
        reqVos.add(reqVo);

        return reqVos;
    }

    /**
     * 根据数据库查询结果封装返回参数
     * @param count
     * @param one
     * @return
     */
    private CountInfoReqVo getCountReqVo(Integer count, Integer one) {
        return CountInfoReqVo.builder().count(count)
                .type(one)
                .name(BehaviorConstant.ACTION_TYPE.get(one)).build();
    }

    @Override
//    @Cacheable(value = "productCountInfoList",key = "#root.method.name")
    public List<ProductCountReqVo> queryProductCountInfoList() {
        List<ProductCountReqVo> list = new ArrayList<>();
        // 获取最近 年月周时间
        Date endTime = new Date();
        //年
        Date yearStartTime = DateFormatUtils.beforeAYearDate();
        //查询下载相关的信息
        List<BehaviorDownloadLogEntity>  dbDownLoadList= baseMapper.selectFileNumList(yearStartTime,endTime);
        // 有权限的分类信息
        List<DataCategoryEntity> categoryDbList = dataCenterFeignService.categoryList(ProductConstant.PRODUCT_TREE_KEY, null).getData();
        // 数据库 树结构id集合
        List<String> treeIdList = Optional.ofNullable(categoryDbList).orElse(new ArrayList<>())
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        // 过滤掉树结构里面没有的信息
        List<BehaviorDownloadLogEntity> behaviorDownloadLogEntityList = Optional.ofNullable(dbDownLoadList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> treeIdList.contains(item.getMenuId()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(behaviorDownloadLogEntityList)){
            return new ArrayList<>();
        }
        //产品分类相关信息
        List<String> idList = behaviorDownloadLogEntityList.stream().distinct().map(BehaviorDownloadLogEntity::getMenuId).collect(Collectors.toList());
        Map<String,List<DataCategoryEntity>> categoryMap =
                categoryDbList.stream()
                        .filter(dataCategoryEntity -> idList.contains(dataCategoryEntity.getId()))
                        .collect(Collectors.groupingBy(DataCategoryEntity::getId));

        // 数据根据产品分类分组
        Map<String, List<BehaviorDownloadLogEntity>> map = behaviorDownloadLogEntityList.stream().collect(Collectors.groupingBy(BehaviorDownloadLogEntity::getMenuId));
        //年相关数据
        //获取x轴相关信息
        List<String> yearTimeList = this.getXTimeList(BehaviorConstant.STR_YEAR,yearStartTime);
        ProductCountReqVo yearReqVo = this.getProductCountReqVo(map,yearTimeList, BehaviorConstant.STR_YEAR,categoryMap);
        list.add(yearReqVo);

        //月
        Date monStartTime = DateFormatUtils.beforeAMonDate();
        List<String> monTimeList = this.getXTimeList(BehaviorConstant.STR_MON,monStartTime);
        ProductCountReqVo monReqVo = this.getProductCountReqVo(map,monTimeList, BehaviorConstant.STR_MON,categoryMap);
        list.add(monReqVo);

        //周
        Date weekStartTime = DateFormatUtils.beforeAWeekDate();
        List<String> weekTimeList = this.getXTimeList(BehaviorConstant.STR_WEEK,weekStartTime);
        ProductCountReqVo weekReqVo = this.getProductCountReqVo(map,weekTimeList, BehaviorConstant.STR_WEEK,categoryMap);
        list.add(weekReqVo);

        return list;
    }

    /**
     * 按周，月，年查询统计结果
     * @param map
     * @param timeList
     * @param mark
     * @return
     */
    private ProductCountReqVo getProductCountReqVo(Map<String, List<BehaviorDownloadLogEntity>> map, List<String> timeList,String mark,
                                                   Map<String,List<DataCategoryEntity>> categoryMap) {
        ProductCountReqVo productCountReqVo = new ProductCountReqVo();
        productCountReqVo.setMark(mark);
        productCountReqVo.setTimeList(timeList);
        List<BehaviorProductInfoReqVo> list = new ArrayList<>();
        List<BehaviorProductCycleReqVo> cycleList = new ArrayList<>();
        map.forEach((s, behaviorLogEntities) -> {
            BehaviorProductInfoReqVo reqVo = new BehaviorProductInfoReqVo();
            reqVo.setId(s);
            reqVo.setName(categoryMap.get(s).get(0).getText());
            //y轴对应信息
            List<Integer> yList = this.getYList(behaviorLogEntities,timeList,mark);
            reqVo.setYList(yList);
            BehaviorProductCycleReqVo cycleReqVo = new BehaviorProductCycleReqVo();
            cycleReqVo.setId(s);
            cycleReqVo.setName(reqVo.getName());
            Integer collect = yList.stream().collect(Collectors.summingInt(Integer::intValue));
            cycleReqVo.setCount(Long.valueOf(collect));
            list.add(reqVo);
            cycleList.add(cycleReqVo);
        });
        productCountReqVo.setYList(list);
        productCountReqVo.setCycleList(cycleList);
        return productCountReqVo;
    }

    /**
     * 获取数据y轴对应信息
     * @param behaviorLogEntities
     * @param timeList
     * @return
     */
    private List<Integer> getYList(List<BehaviorDownloadLogEntity> behaviorLogEntities, List<String> timeList,String mark) {
        //根据时间分组,计算对应时间段文件个数
        Map<String, List<BehaviorDownloadLogEntity>> map;
        if(mark.equals(BehaviorConstant.STR_YEAR)){
            map = behaviorLogEntities.stream().collect(Collectors.groupingBy(item -> {
                Date createdDate = item.getCreatedDate();
                //按月分组
                return DateFormatUtils.dateToStr(createdDate, DateFormatUtils.formatYY_MM);
            }));
        }else {
            map = behaviorLogEntities.stream().collect(Collectors.groupingBy(item ->
                //按天分组
               DateFormatUtils.dateToStr(item.getCreatedDate(), DateFormatUtils.formatYY_MM_dd)
            ));
        }
        //遍历x轴时间信息，填充y轴值，没有补充0
        Map<String, List<BehaviorDownloadLogEntity>> finalMap = map;
        List<Integer> yList = new ArrayList<>();
        timeList.forEach(s -> {
            Integer i = Optional.ofNullable(finalMap.get(s)).orElse(new ArrayList<>()).stream().collect(Collectors.summingInt(BehaviorDownloadLogEntity::getFileNum));
            yList.add(i);
        });
        return yList;
    }

    /**
     * 获取x轴对应坐标信息
     * @param mark
     * @param startTime
     * @return
     */
    private List<String> getXTimeList(String mark,Date startTime) {
        //年只拿到月份，月和周的具体到某一天
        List<String> issueList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date endTime = calendar.getTime();
        calendar.setTime(startTime);
        if(mark.equals(BehaviorConstant.STR_YEAR)){
            for (long d = startTime.getTime(); d <= endTime.getTime(); d = DateFormatUtils.getMonDateToMillis(calendar) ) {
                Date date = new Date(d);
                String s = DateFormatUtils.dateToStr(date, DateFormatUtils.formatYY_MM);
                issueList.add(s);
            }
        }else {
            for (long d = calendar.getTimeInMillis(); d <= endTime.getTime(); d = DateFormatUtils.getDateToMillis(calendar) ) {
                Date date = new Date(d);
                String s = DateFormatUtils.dateToStr(date, DateFormatUtils.formatYY_MM_dd);
                issueList.add(s);
            }
        }
        return issueList;
    }

}