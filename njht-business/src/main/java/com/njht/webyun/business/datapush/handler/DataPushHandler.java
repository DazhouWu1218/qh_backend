package com.njht.webyun.business.datapush.handler;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.njht.entity.dataPush.HttpParamEntity;
import com.njht.entity.dataPush.SingleTaskExeEntity;
import com.njht.webyun.business.datapush.constant.EsConstant;
import com.njht.webyun.business.datapush.service.DataPushContext;
import com.njht.webyun.business.datapush.service.DataPushService;
import com.njht.webyun.business.sys.entity.DicEntity;
import com.njht.webyun.business.sys.service.DicService;
import com.njht.webyun.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 往天境推送系统数据（入口）
 */
@Component
@Slf4j
public class DataPushHandler {

    @Autowired
    private DataPushContext dataPushContext;

    @Autowired
    private DicService dicService;

    /**
     * 跨平台Http任务调用 (每天晚上12 点执行数据推送任务)
     * @throws Exception
     */
    @Scheduled(cron = "0 50 23 * * ? ")
    public void dataPushHandler () {
        // 从字典表获取ES类型
        List<DicEntity> typeList = dicService.getDataPushInfo(EsConstant.BUSINESS_DI_TYPE);
        // 封装数据返回结果
        log.info("数据推送开始执行......");
        Optional.ofNullable(typeList)
                .orElse(new ArrayList<>())
                // 处理不同的 数据并返回
                .forEach(this::executeParam);
    }

    /**
     * 处理请求参数
     * @param dicEntity
     * @return
     */
    private void executeParam(DicEntity dicEntity) {
        try {
            // 包含对应实现类再往下执行
            DataPushService nowDataPushService = dataPushContext.getDataPushService(dicEntity.getDicValue());
            if (nowDataPushService != null ) {
                // 获取推送数据
                List<?> pushDataList = nowDataPushService.execute();
                // 如果list长度大于 900 进行list拆分,分开推送
                List<? extends List<?>> partition = Lists.partition(pushDataList, 900);
                for (List<?> list : partition) {
                    log.info("参数封装完成");
                    // 将对象转换成map
                    List<Map<String,Object>> collect = Optional.ofNullable(list).orElse(new ArrayList<>())
                            .stream().map(item -> {
                                HttpParamEntity paramEntity = new HttpParamEntity(dicEntity.getDicKey(), dicEntity.getDicSymbol());
                                paramEntity.setFields(item);
                                return MapUtil.objectToMap(paramEntity);
                            }).collect(Collectors.toList());
                    // 推送数据
                    this.pushDataToEsByTJApi(collect,dicService.getDiUrl());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{},推送数据错误,错误信息:{}",dicEntity.getDicSymbol(),e.getMessage());
        }
    }


    /**
     * 数据推送
     * @param dataList
     * @return  返回格式位==>  ["32位UUID","32位UUID","32位UUID".....]
     */
    private String pushDataToEsByTJApi(List<Map<String, Object>> dataList,String url){
        log.info("开始推送数据......");
        log.info("参数：{}", JSON.toJSONString(dataList));
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        String s = null;
        try {
            s = HttpRequest.post(url).body(JSON.toJSONString(dataList)).addHeaders(headMap).execute().body();
            log.info("数据推送成功");
        } catch (HttpException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return s;
    }

    /**
     * 单个任务失败告警
     * @param singleTaskExeEntity
     * @return
     */
    public Boolean logAlarm(SingleTaskExeEntity singleTaskExeEntity) {
        // 从字典表获取ES类型
        List<DicEntity> typeList = dicService.getDataPushInfo(EsConstant.BUSINESS_EI_TYPE);
        DicEntity dicEntity = Optional.ofNullable(typeList).orElse(new ArrayList<>())
                .stream()
                .findFirst()
                .orElse(new DicEntity());
        HttpParamEntity httpParamEntity = new HttpParamEntity(dicEntity.getDicKey(),dicEntity.getDicSymbol());
        httpParamEntity.setFields(singleTaskExeEntity);
        Map<String, Object> stringObjectMap = MapUtil.objectToMap(httpParamEntity);
        log.info("单个任务失败告警....开始告警");
        String s = pushDataToEsByTJApi(Collections.singletonList(stringObjectMap),dicService.getDiUrl());
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return true;
    }

}
