package com.htht.job.core.util;


import com.htht.job.core.constant.DateConstant;

import java.util.Calendar;

/**
 * 产品生产 日期工具类
 */
public class CalendarUtil {
    private CalendarUtil() {

    }

    /**
     * 根据产品周期，处理日期变更规则
     * @param calendar
     * @param cycle
     * @return
     */
    public static  Calendar calendarDealLast(Calendar calendar, String cycle) {
        if (DateConstant.COOD.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } else if (DateConstant.COTD.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.DAY_OF_YEAR, -10);
        } else if (DateConstant.COFD.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.DAY_OF_YEAR, -5);
        } else if (DateConstant.COAM.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.MONTH, -1);
        } else if (DateConstant.COAQ.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.MONTH, -3);
        } else if (DateConstant.COAY.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.YEAR, -1);
        } else if (DateConstant.COTM.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.MINUTE, -10);
        } else if (DateConstant.COOH.equalsIgnoreCase(cycle)) {
            calendar.add(Calendar.HOUR_OF_DAY, -1);
        } else {
            calendar.add(Calendar.YEAR, -1);
        }
        return calendar;
    }

    /**
     * 文件时间与当前时间对比，一小时之前的文件返回false，一小时之后的文件返回true。
     *
     * @param fileTime
     * @param i
     * @return
     */
    public static boolean timeDetail(Long fileTime, Integer i) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -i);
        long timeInMillis = c.getTimeInMillis();
        if (fileTime - timeInMillis > 0) {
            return true;
        } else {
            return false;
        }
    }
}
