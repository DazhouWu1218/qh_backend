package com.htht.executor.task.util;

import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.PreDataParam;
import com.htht.job.core.util.CalendarUtil;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.MatchTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
     * @param preDataParam
     * @return
     */
    public static List<String> getIssueList(PreDataParam preDataParam) {
        List<String> issues = new ArrayList<>();

        String dateType = preDataParam.getDateType();
        if(NOW.equals(dateType)){
            Integer i = preDataParam.getRangeDay();
            for(int j=0;j<i;j++){
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -j);
                String issue = MatchTime.matchIssue(calendar.getTime(),DateConstant.COOD);
                issues.add(issue);
            }
        }
        if(HISTORY.equals(dateType)){
            String[] temp = preDataParam.getTimeRange().split(",");
            Date beginTime = DateUtil.strToDate(temp[0], DateConstant.YYYY_MM_DD_HH_MM_SS);
            Date endTime = DateUtil.strToDate(temp[1], DateConstant.YYYY_MM_DD_HH_MM_SS);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            while(calendar.getTimeInMillis() >= beginTime.getTime()){
                //对时间进行处理，根据不同的产品周期和输入的时间范围进行时间处理的操作
                String issue = MatchTime.matchIssue(calendar.getTime(), DateConstant.COOD);
                // 根据产品周期，切时间间隔
                calendar = CalendarUtil.calendarDealLast(calendar,DateConstant.COOD);
                if(issues.contains(issue)){
                    continue;
                }
                issues.add(issue);
            }
        }
        return issues;
    }
}
