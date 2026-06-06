package com.njht.webyun.publish.product.util;

import com.njht.webyun.utils.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class IssueUtil {



    public static final String mark = "FIRE_H8";
    public static final String mark1 = "DST_H8";

    public static String getIssueByMark(String issue,String proMark){
        // H8 火点 期次时间+8
        if(Objects.equals(mark,proMark) || Objects.equals(mark1,proMark)){
            Date date = DateFormatUtils.strToDate(issue,DateFormatUtils.formatYYMMddHHmm);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR,8);
            return DateFormatUtils.dateToStr(calendar.getTime(),DateFormatUtils.formatYYMMddHHmm);
        } else {
            return issue;
        }
    }


    public static void main(String[] args) {
        String issue = "202001010000";
        String issueByMark = getIssueByMark(issue, mark);
        System.out.println(issueByMark);
    }
}
