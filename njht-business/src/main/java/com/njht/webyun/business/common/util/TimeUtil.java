package com.njht.webyun.business.common.util;

import com.njht.webyun.business.index.constant.IndexConstant;
import com.njht.webyun.business.index.vo.TimeParam;
import com.njht.webyun.utils.DateUtils;

import java.util.Date;
import java.util.Objects;

/**
 * @author daiguojun
 * @date 2022-08-16 18:05
 * 时间工具类
 */
public class TimeUtil {

    public static TimeParam getTimeInfo(String type){
        //type 1-本日，2-本周，3-本月
        Date beginDate;
        Date endDate;
        if(Objects.equals(type, IndexConstant.INDEX_TWO)){
            //默认查询本周
            beginDate = DateUtils.getBeginDayOfWeek();
            endDate = DateUtils.getEndDayOfWeek();
        }else if (Objects.equals(type,IndexConstant.INDEX_THREE)){
            beginDate = DateUtils.getBeginDayOfMonth();
            endDate = DateUtils.getEndDayOfMonth();
        }else {
            beginDate = DateUtils.getDayBegin();
            endDate = DateUtils.getDayEnd();
        }
        TimeParam timeParam = new TimeParam();
        timeParam.setBeginDate(beginDate);
        timeParam.setEndDate(endDate);
        return timeParam;
    }
}
