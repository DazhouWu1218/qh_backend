package com.htht.executor.task.util;

import com.alibaba.fastjson.JSON;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.MatchTime;

import java.util.*;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/24 11:32
 * @Description:
 */
public class ShardUtil {
    public static final String NOW = "now";
    public static final String HISTORY = "history";
    /**
     * 根据期次以及时间生成期次的集合
     * @param params
     * @param fixmap
     * @param dymap
     * @return
     */
    public static List<String> getIssueList(String params, LinkedHashMap fixmap, LinkedHashMap dymap) {
        ProductParam productParam = JSON.parseObject(params, ProductParam.class);
        List<String> issues = new ArrayList<>();

        String dateType = productParam.getDateType();
        if(NOW.equals(dateType)){
            String productRangeDay = productParam.getProductRangeDay();
            Integer i = Integer.valueOf(productRangeDay);
            for(int j=0;j<i;j++){
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -j);
                String issue = MatchTime.matchIssue(calendar.getTime(), productParam.getCycle());
                issues.add(issue);
            }
        }
        if(HISTORY.equals(dateType)){
            String[] temp = productParam.getProductRangeDate().split(",");
            Date beginTime = DateUtil.strToDate(temp[0], DateConstant.YYYY_MM_DD_HH_MM_SS);
            Date endTime = DateUtil.strToDate(temp[1], DateConstant.YYYY_MM_DD_HH_MM_SS);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            while(calendar.getTimeInMillis() >= beginTime.getTime()){
                //对时间进行处理，根据不同的产品周期和输入的时间范围进行时间处理的操作
                String issue = MatchTime.matchIssue(calendar.getTime(), productParam.getCycle());
                // 根据产品周期，切时间间隔
                calendar = CalendarUtil.calendarDealLast(calendar,productParam.getCycle());
                if(issues.contains(issue)){
                    continue;
                }
                issues.add(issue);
            }
        }
        return issues;
    }
}
