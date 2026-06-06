package com.htht.executor.task.issue.time;


import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.issue.IssueTimeShard;
import com.htht.executor.task.util.CalendarUtil;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.util.MatchTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * 当前时间产品批次分片
 * @author Administrator
 */
@Component(BaseProductServiceConstant.INDEX_TIME_NOW)
@Slf4j
public class CurrentIssueTimeShard  implements IssueTimeShard {
    @Override
    public List<String> getIssueListByTimeRange(ProductParam productParam) {

        List<String> issues = new ArrayList<>();
        //产品周期
        String cycle = productParam.getCycle();
        //拿到实时数据的参数信息
        String productRangeDay = productParam.getProductRangeDay();
        //isEmpty不为空，返回false；为空，返回true
        //isNumeric为纯数字，返回true，不为纯数字返回false
        int issueCount = 0;
        int day = 0;
        String bz = null;
        try {
            if (StringUtils.isEmpty(productRangeDay) || !StringUtils.isNumeric(productRangeDay)) {
                productRangeDay = "1";
            }
            issueCount = Integer.parseInt(productRangeDay);
            // 有的产品实时处理的是N天前的数据
            bz = productParam.getBz();
            if (StringUtils.isEmpty(bz) || !StringUtils.isNumeric(bz)) {
                bz = "0";
            }
            day = Integer.parseInt(bz);
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }
        //一个获取时间等信息的工具类
        Calendar calendar = Calendar.getInstance();
        // 处理期次（有的产品实时拿到的是n天前的数据）
        for (int i=0;i<day;i++) {
            CalendarUtil.calendarDealLast(calendar, cycle);
        }
        while(true){
            //时间匹配
            String issue = MatchTime.matchIssue(calendar.getTime(), cycle);
            CalendarUtil.calendarDealLast(calendar, cycle);
            log.info("---ProductBaseService---当前issue="+issue);
            issues.add(issue);
            if(issues.size() >= issueCount) break;
        }
        Collections.sort(issues);
        return issues;
    }

}
