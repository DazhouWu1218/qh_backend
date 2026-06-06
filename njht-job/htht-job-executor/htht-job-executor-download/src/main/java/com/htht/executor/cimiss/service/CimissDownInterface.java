package com.htht.executor.cimiss.service;

import com.htht.executor.cimiss.bean.ResultBean;
import com.htht.job.core.entity.paramtemplate.CimissDownParam;

import java.text.ParseException;
import java.util.HashMap;

/**
 * @author daiguojun
 * @date 2022-08-09 9:41
 * 服务
 */
public interface CimissDownInterface {


    /**
     * cimiss数据汇集
     */
    void down();


    /**
     * 处理返回数据
     * @param cimissParam
     * @param retStr
     */
    void execute(CimissDownParam cimissParam,StringBuffer retStr);


    /**
     * 获取接口返回结果
     * @param rstData
     * @return
     */
    ResultBean getFileOrStationData(String rstData);


    /**
     * 获取时间点
     * @param times
     * @param timeKey
     * @return
     */
    String getCurrentDay(String times, String timeKey) throws ParseException;


    /**
     * 下载和入库 参数时间字段不一样
     * @param map
     * @param times
     */
    void setTimes(HashMap<String, String> map,String times);
}
