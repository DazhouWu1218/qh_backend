package com.htht.executor.task.issue.time;


import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.issue.IssueTimeShard;
import com.htht.executor.task.util.CalendarUtil;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.MatchTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 历史时间产品批次
 * @author Administrator
 */
@Component(BaseProductServiceConstant.INDEX_TIME_HISTORY)
@Slf4j
public class HistoryIssueTimeShard implements IssueTimeShard {
    @Override
    public List<String> getIssueListByTimeRange(ProductParam productParam) {

        List<String> issues = new ArrayList<>();
        //产品周期
        String cycle = productParam.getCycle();
        String[] temp = productParam.getProductRangeDate().split(",");
        Date beginTime = DateUtil.strToDate(temp[0], DateConstant.YYYY_MM_DD_HH_MM_SS);
        Date endTime = DateUtil.strToDate(temp[1], DateConstant.YYYY_MM_DD_HH_MM_SS);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        while(calendar.getTimeInMillis() >= beginTime.getTime()){
            //对时间进行处理，根据不同的产品周期和输入的时间范围进行时间处理的操作
            String issue = MatchTime.matchIssue(calendar.getTime(), cycle);
            // 根据产品周期，切时间间隔
            calendar = CalendarUtil.calendarDealLast(calendar,cycle);
            if(issues.contains(issue)) continue;
            log.info("---ProductBaseService---当前issue="+issue);
            issues.add(issue);
        }
        Collections.sort(issues);
        return issues;
    }
}
