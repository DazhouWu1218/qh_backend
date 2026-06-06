package com.htht.job.core.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.*;
import java.time.ZoneId;
import java.util.*;

/**
 * 日期处理工具类
 */
public final class DateUtil {

	// ---------------------- format parse ----------------------
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 *
	 * @param dateDate
	 * @return
	 */
	public static synchronized String dateToStr(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(dateDate);
	}
	public static synchronized Date addHour(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return calendar.getTime();
	}

	/**
	 * 获取含年份日期目录
	 * @param path
	 * @param date
	 * @return
	 */
	public static String getYearAndMonthPathByDate(String path,Date date){
		if (path.contains("{yyyy}")) {
			String yearPath = DateUtil.formatDateTime(date, "yyyy");
			path = path.replace("{yyyy}", yearPath);
		}
		if (path.contains("{yyyyMM}")) {
			String monthPath = DateUtil.formatDateTime(date, "yyyyMM");
			path = path.replace("{yyyyMM}", monthPath);
		}
		if (path.contains("{yyyy-MM}")) {
			String monthPath = DateUtil.formatDateTime(date, "yyyy-MM");
			path = path.replace("{yyyy-MM}", monthPath);
		}
		if(path.contains("{") && path.contains("}")){
			String format = getPathFormat(path);
			return getPathByFormat(path, format, date);
		}
		return path;

	}
	public static SimpleDateFormat forMatter() {
		return new SimpleDateFormat("dd'/'MM'/'yyyy", Locale.SIMPLIFIED_CHINESE);
	}
	public static SimpleDateFormat forMatterDateTime() {
		return new SimpleDateFormat("dd'/'MM'/'yyyy' 'HH':'mm", Locale.SIMPLIFIED_CHINESE);
	}
	public static SimpleDateFormat forMatterDateTime2() {
		return new SimpleDateFormat("yyyy'/'MM'/'dd' 'HH':'mm':'ss", Locale.SIMPLIFIED_CHINESE);
	}

	/**
	 * Creates a new DateUtil object
	 */
	private DateUtil() {
	}

	/**
	 * Returns the date of the day in form of a String
	 * 
	 * @return The Date of the day in a "JJ/MM/AAAA" format
	 */
	private static synchronized String getCurrentDateString(
			SimpleDateFormat formatter) {
		return formatDateTime(new Date(), formatter);
	}

	private static synchronized String formatDateTime(
			Date targetDate, java.text.DateFormat formatter) {
		if (targetDate == null) {
			return "";
		}
		return formatter.format(targetDate);
	}

	public static synchronized String getCurrentDateString() {
		return getCurrentDateString(forMatter());
	}

	public static synchronized String getCurrentTimeString() {
		return getCurrentDateString(forMatterDateTime2());
	}

	public static synchronized String getCurrentDateString(String formatStr) {
		return formatDateTime(new Date(), formatStr);
	}
	public static synchronized String getCurrentDateStringWithOffset(String formatStr,Integer offset) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, offset);
		return formatDateTime(new Date(c.getTimeInMillis()), formatStr);
	}

	public static synchronized String formatDateTime(Date targetDate, String formatStr) {
		java.text.DateFormat dateFormater = new java.text.SimpleDateFormat(formatStr, Locale.SIMPLIFIED_CHINESE);
		return formatDateTime(targetDate, dateFormater);
	}
	public static synchronized String getCurrentDateStringWithHourOffset(String formatStr,Integer offset) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, offset);
		return formatDateTime(new Date(c.getTimeInMillis()), formatStr);
	}
	public static synchronized String formatDate(Date date, String formatDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
		return simpleDateFormat.format(date);
	}


	public static int getIntervalHours(Date startday, Date endday) {
		if (startday.after(endday)) {
			Date cal = startday;
			startday = endday;
			endday = cal;
		}
		long sl = startday.getTime();
		long el = endday.getTime();
		long ei = el - sl;
		return (int) (ei / (1000 * 60 * 60));
	}

	/**
	 * Converts a String date in a "jj/mm/aaaa" format in a java.sql.Date type
	 * date
	 * 
	 * @param strDate
	 *            The String Date to convert, in a date in the "jj/mm/aaaa"
	 *            format
	 * @return The date in form of a java.sql.Date type date
	 */
	public static synchronized java.sql.Date getDateSql(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		Date date = forMatter().parse(strDate, pos);

		if (date != null) {
			return new java.sql.Date(date.getTime());
		}

		return null;
	}

	/**
	 * Converts a String date in a "jj/mm/aaaa" format in a java.util.Date type
	 * date
	 * 
	 * @param strDate
	 *            The String Date to convert, in a date in the "jj/mm/aaaa"
	 *            format
	 * @return The date in form of a java.sql.Date tyep date
	 */
	public static synchronized Date getDate(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		return forMatter().parse(strDate, pos);
	}
	
	public static synchronized Date getDate(String strDate,String pattern ) throws ParseException {
		forMatter().applyPattern(pattern);
		return forMatter().parse(strDate);
	}

	public static synchronized Date addDate(Date date, int field, int amount) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}
	/**
	 * Converts a String date in a "jj/mm/aaaa" format in a java.sql.Timestamp
	 * type date
	 * 
	 * @param strDate
	 *            The String Date to convert, in a date in the "jj/mm/aaaa"
	 *            format
	 * @return The date in form of a java.sql.Date tyep date
	 */
	public static synchronized java.sql.Timestamp getTimestamp(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		Date date = forMatter().parse(strDate, pos);

		if (date != null) {
			return (new java.sql.Timestamp(date.getTime()));
		}

		return null;
	}

	
	/**
	 * Converts a String date in a "yyyy'/'MM'/'dd' 'HH':'mm'：'ss" format in a java.sql.Timestamp
	 * type date
	 * 
	 * @param strDate
	 *            The String Date to convert, in a date in the "yyyy'/'MM'/'dd' 'HH':'mm'：'ss"
	 *            format
	 * @return The date in form of a java.sql.Date tyep date
	 */
	public static synchronized java.sql.Timestamp getTimestamp2(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		Date date = forMatterDateTime2().parse(strDate, pos);
		if (date != null) {
			return (new java.sql.Timestamp(date.getTime()));
		}
		return null;
	}
	
	/**
	 * Converts a java.sql.Date type date in a String date with a "jj/mm/aaaa"
	 * format
	 * 
	 * @param date
	 *            java.sql.Date date to convert
	 * @return strDate The date converted to String in a "jj/mm/aaaa" format or
	 *         an empty String if the date is null
	 */
	public static synchronized String getDateString(java.sql.Date date) {
		if (date != null) {
			StringBuffer strDate = new StringBuffer();
			forMatter().format(date, strDate, new FieldPosition(0));

			return strDate.toString();
		}

		return "";
	}
	public static synchronized int getDateOnlyHour(Date date) {
		if (date != null) {
			Calendar instance = Calendar.getInstance();
			instance.setTime(date);
			return instance.get(Calendar.HOUR_OF_DAY);
		}
		
		return 0;
	}


	/**
	 * Converts une java.sql.Timestamp date in a String date in a "jj/mm/aaaa"
	 * format
	 * 
	 * @param date
	 *            java.sql.Timestamp date to convert
	 * @return strDate The String date in a "jj/mm/aaaa" format or the emmpty
	 *         String if the date is null
	 */
	public static synchronized String getDateString(java.sql.Timestamp date) {
		if (date != null) {
			StringBuffer strDate = new StringBuffer();
			forMatter().format(date, strDate, new FieldPosition(0));

			return strDate.toString();
		}

		return "";
	}


	/**
	 * Converts a java.util.Date date in a String date in a "jj/mm/aaaa" format
	 * 
	 * @param date
	 *            java.util.Date date to convert
	 * @return strDate A String date in a "jj/mm/aaaa" format or an empty String
	 *         if the date is null
	 */
	public static synchronized String getDateString(Date date) {
		if (date != null) {
			StringBuffer strDate = new StringBuffer();
			forMatter().format(date, strDate, new FieldPosition(0));

			return strDate.toString();
		}

		return "";
	}


	/**
	 * Converts a long value to a String date in a "jj/mm/aaaa hh:mm" format
	 * 
	 * @param lTime
	 *            The long value to convert
	 * @return The formatted string
	 */
	public static synchronized String getDateTimeString(long lTime) {
		StringBuffer strDate = new StringBuffer();
		forMatterDateTime().format(new Date(lTime), strDate,
				new FieldPosition(0));

		return strDate.toString();
	}

	public static synchronized Date strToDate(String strDate) {
		try {
			if(strDate.contains("'")) {
				strDate = strDate.replaceAll("'", "");
			}
			
			SimpleDateFormat ft = null;
			if(strDate.contains("-")) {
				ft = new SimpleDateFormat("yyyy-MM-dd");
				if (strDate.length() > 10) {
					ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}
			} else if(strDate.contains("/")) {
				ft = new SimpleDateFormat("yyyy/MM/dd");
				if (strDate.length() > 10) {
					ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				}
			} else if(strDate.contains("\\")) {
				ft = new SimpleDateFormat("yyyy\\MM\\dd");
				if (strDate.length() > 10) {
					ft = new SimpleDateFormat("yyyy\\MM\\dd HH:mm:ss");
				}
			} else {
				ft = new SimpleDateFormat("yyyyMMdd");
				if (strDate.length() > 10) {
					ft = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				}
			}
			Date d = ft.parse(strDate);
			return new java.sql.Date(d.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date(Calendar.getInstance().getTime().getTime());
		}
	}

	public static synchronized Date strToDate(String strDate,
			String formatStr) {
		if(strDate.contains("'")) {
			strDate = strDate.replace("'", "");
		}
		try {
			SimpleDateFormat ft = new SimpleDateFormat(formatStr);
			Date d = ft.parse(strDate);
			return new Date(d.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date(Calendar.getInstance().getTime().getTime());
		}
	}
	/**
	 * 时间差
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String getDateDifference(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return "";
		}
		long between = (endDate.getTime() - startDate.getTime()) / 1000; // 除以1000是为了转换成秒
		if (between == 0)
			between = 1;
		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
		long second1 = between % 60;
		StringBuffer dataString = new StringBuffer("");
		if (day1 > 0) {
			dataString.append(day1 + "天");
		}
		if (hour1 > 0) {
			dataString.append(hour1 + "小时");
		}
		if (minute1 > 0) {
			dataString.append(minute1 + "分");
		}
		if (second1 > 0) {
			dataString.append(second1 + "秒");
		}
		return dataString.toString();
	}
	
	public static String formatDateWithDay(int year,int dayOfYear,String format) {
		Calendar calendar =Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
		return formatDateTime(calendar.getTime(), format);
	}
	/**
	 * 文件名时间串转化为date
	 * @param timeString
	 * @return
	 */
	public  static Date  getDataFromFileName(String timeString){
		String year = timeString.substring(0, 4);
		String month = timeString.substring(4, 6);
		String day = timeString.substring(6, 8);
		String hour = timeString.substring(8, 10);
		String min = timeString.substring(10, 12);
		String second = timeString.substring(12, 14);
		String filesNameTime = "" + year + "-" + month + "-" + day + " " + hour
				+ ":" + min + ":" + second;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(filesNameTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;

	}

	/**
	 * 文件名时间串转化为date
	 * @param dateStr
	 * @param formatter
	 * @return
	 */
	public static long strDateToLong(String dateStr,String formatter){
		SimpleDateFormat sf = new SimpleDateFormat(formatter);
		Date date = new Date();
		try {
			date = sf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();

	}

	/**
	 * 复制文件名称
	 * @param beginTime
	 * @param fileTime
	 * @param endTime
	 * @return
	 */
	public static boolean CopByFileName(String beginTime,String fileTime,String endTime){
		Date begin= DateUtil.strToDate(beginTime, "yyyyMMddHHmmss");
		Date end= DateUtil.strToDate(endTime, "yyyyMMddHHmmss");
		Date ft= DateUtil.strToDate(fileTime, "yyyyMMddHHmmss");
		if(begin.getTime()<=ft.getTime() && ft.getTime()<=end.getTime()){
			return true;
		}
		return false;
	}
	public static String getPathByFormat(String path,String format,Date date){
		String time = formatDateTime(date, format);
		return path.replace("{"+format+"}", time);
	}
	public static String getPathFormat(String path){
		if(StringUtils.isBlank(path)){
			return "";
		}
		if(path.contains("{") && path.contains("}")){
			return path.substring(path.indexOf("{")+1,path.indexOf("}"));
		}
		return "";
	}
	public static String getPathByDate(String path,Date date){
		String format = "";
		String result = path;
		while (result.contains("{") && result.contains("}")) {
			format = getPathFormat(result);
			result = getPathByFormat(result, format, date);
		}

		return result.replace("\\","/");
	}

	public static void main(String[] args) {
		String path = "/opt/data/rawdata/{yyyyMM}/snows/actual/{yyyyMMdd}/";
		String pathByDate = getPathByDate(path, new Date());
		System.out.println(pathByDate);
	}
	/**
	 * 获取当前北京时间
	 * @return
	 */
	public static Date getBjTime() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        
			//北京时间
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			String time1 = sdf.format(new Date());

			return sdf1.parse(time1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 系统时间转北京时
	 * @param date 
	 * @return
	 */
	public static Date getSystemToBjTime(Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf_sys = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf_bj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 查看当前的时区
			ZoneId defaultZone = ZoneId.systemDefault();
			String dateStr = sdf.format(date);	
			//当前时区的时间
			sdf_sys.setTimeZone(TimeZone.getTimeZone(defaultZone));
			Date date_sys = sdf_sys.parse(dateStr);
			//北京时间
			sdf_bj.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			//时间转换
			String time1 = sdf_bj.format(date_sys);
			Date time2 = sdf.parse(time1);
			return time2;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String dateToStr(Date date, String s) {
		SimpleDateFormat sdf = new SimpleDateFormat(s);
		String format = sdf.format(date);
		return format;
	}


	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final ThreadLocal<Map<String, DateFormat>> dateFormatThreadLocal = new ThreadLocal<Map<String, DateFormat>>();
	private static DateFormat getDateFormat(String pattern) {
		if (pattern==null || pattern.trim().length()==0) {
			throw new IllegalArgumentException("pattern cannot be empty.");
		}

		Map<String, DateFormat> dateFormatMap = dateFormatThreadLocal.get();
		if(dateFormatMap!=null && dateFormatMap.containsKey(pattern)){
			return dateFormatMap.get(pattern);
		}

		synchronized (dateFormatThreadLocal) {
			if (dateFormatMap == null) {
				dateFormatMap = new HashMap<String, DateFormat>();
			}
			dateFormatMap.put(pattern, new SimpleDateFormat(pattern));
			dateFormatThreadLocal.set(dateFormatMap);
		}

		return dateFormatMap.get(pattern);
	}

	/**
	 * format datetime. like "yyyy-MM-dd"
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String formatDate(Date date) {
		return format(date, DATE_FORMAT);
	}

	/**
	 * format date. like "yyyy-MM-dd HH:mm:ss"
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String formatDateTime(Date date) {
		return format(date, DATETIME_FORMAT);
	}

	/**
	 * format date
	 *
	 * @param date
	 * @param patten
	 * @return
	 * @throws ParseException
	 */
	public static String format(Date date, String patten) {
		return getDateFormat(patten).format(date);
	}

	/**
	 * parse date string, like "yyyy-MM-dd HH:mm:s"
	 *
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateString){
		return parse(dateString, DATE_FORMAT);
	}

	/**
	 * parse datetime string, like "yyyy-MM-dd HH:mm:ss"
	 *
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateTime(String dateString) {
		return parse(dateString, DATETIME_FORMAT);
	}

	/**
	 * parse date
	 *
	 * @param dateString
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String dateString, String pattern) {
		try {
			Date date = getDateFormat(pattern).parse(dateString);
			return date;
		} catch (Exception e) {
			logger.warn("parse date error, dateString = {}, pattern={}; errorMsg = {}", dateString, pattern, e.getMessage());
			return null;
		}
	}


	// ---------------------- add date ----------------------

	public static Date addYears(final Date date, final int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	public static Date addMonths(final Date date, final int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date addDays(final Date date, final int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	public static Date addHours(final Date date, final int amount) {
		return add(date, Calendar.HOUR_OF_DAY, amount);
	}

	public static Date addMinutes(final Date date, final int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	private static Date add(final Date date, final int calendarField, final int amount) {
		if (date == null) {
			return null;
		}
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	/**
	 * 根据时间获取文件正则
	 * @param regex
	 * @param date
	 * @return
	 */
	public static String getRegexByDate(String regex, Date date) {
		return getPathByDate(regex,date);
	}
}
