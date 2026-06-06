package com.njht.webyun.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/23 15:18
 * @Description: 日期转换工具类
 */
@Slf4j
public class DateFormatUtils {

    public static final String formatYY_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String formatYY_MM_dd = "yyyy-MM-dd";
    public static final String formatYY_MM = "yyyy-MM";
    public static final String formatYY_MM_dd_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String formatYYMMddHHmmss = "yyyyMMddHHmmss";
    public static final String formatYYYYMMdd = "yyyyMMdd";
    public static final String formatYYYYMM = "yyyyMM";
    public static final String formatHH = "HH";
    public static final String formatHHmm = "HHmm";
    public static String formatYYMMddHHmm= "yyyyMMddHHmm";


    public static String issueFormat(String issue){
        SimpleDateFormat sdf = new SimpleDateFormat(formatYYMMddHHmm);
        SimpleDateFormat sdf1 = new SimpleDateFormat(formatYY_MM_dd_ss);
        Date parse = null;
        try {
            parse = sdf.parse(issue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = sdf1.format(parse);
        return format;
    }

    /**
     * 将当前时间转换成字符串格式
     * @return
     */
    public static String currentDateToStr(String format){
        return dateToStr(new Date(),format);
    }

    /**
     * 日期转换成字符串格式
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

    /**
     * 获取字符串格式的时间戳
     * @return
     */
    public static String  currentTimeMillis(){
        long l = System.currentTimeMillis();
        String s = String.valueOf(l);
        return s;
    }

    /**
     * 获取一周之前的日期
     * @return
     */
    public static Date beforeAWeekDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-6);
        Date weekStartTime = calendar.getTime();
        return weekStartTime;
    }

    /**
     * 获取一个月之前的日期
     * @return
     */
    public static Date beforeAMonDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        calendar.add(Calendar.DAY_OF_MONTH,+1);
        Date monStartTime = calendar.getTime();
        return monStartTime;

    }

    /**
     * 获取一年之前的日期
     * @return
     */
    public static Date

    beforeAYearDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-11);
        Date yearStartTime = calendar.getTime();
        return yearStartTime;
    }

    /**
     * 天数 +1
     * @param c
     * @return
     */
    public static long getDateToMillis(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }

    /**
     * 月份 +1
     * @param c
     * @return
     */
    public static long getMonDateToMillis(Calendar c) {
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        return c.getTimeInMillis();
    }

    /**
     * 字符串格式的时间转换
     * @param timeStr
     * @param format
     * @param toFormat
     * @return
     */
    public static String strToDateStr(String timeStr, String format, String toFormat) {
        Date date = strToDate(timeStr,format);
        return dateToStr(date,toFormat);
    }

    /**
     * 时间转字符串
     * @param timeStr
     * @param format
     * @return
     */
    public static Date strToDate(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date time = null;
        try {
            time = simpleDateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 时间戳转成日期
     * @param time
     * @return
     */
    public static Date timeMillistoDate(String time) {
        return new Date(Long.valueOf(time));
    }

    /**
     * 获取指定格式的时间信息
     * @param date
     * @param format
     * @return
     */
    public static Date getCurrentDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String timeStr = sdf.format(date);
        return strToDate(timeStr,format);
    }

    /**
     * 结束时间 拼接时分秒 23:59:59
     * @param endTime 结束时间
     * @return
     */
    public static String setEndTime(String endTime) {
        // 默认前端传参格式
        // 判断字符串 时间格式 默认只处理 yyyy-MM-dd 格式
        int length = endTime.replace("-", "").length();
        if( length == 8 ) {
            log.info("===========>>>>>>>>>>前端传参-时间格式：{},开始处理=====",endTime);
            Date date = strToDate(endTime, DateFormatUtils.formatYY_MM_dd);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // 设置 年月日 为 ：23:59:59
            calendar.add(Calendar.DAY_OF_YEAR,1);
            calendar.add(Calendar.SECOND,-1);
            endTime = dateToStr(calendar.getTime(), DateFormatUtils.formatYY_MM_dd_ss);
            log.info("===========>>>>>>>>>>处理结束后的时间格式：{}=====",endTime);
        } else {
            //不是年月日 的格式 不进行处理
            log.info("===========>>>>>>>>>>前端传参-结束时间：{},不进行处理========",endTime);
        }
        return endTime;
    }
}
