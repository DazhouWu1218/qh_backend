package com.njht.webyun.management.dataanalysis.systemvisit.service.impl;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.management.dataanalysis.systemvisit.constant.SystemVisitCountConstant;
import com.njht.webyun.management.dataanalysis.systemvisit.dao.SystemVisitCountDao;
import com.njht.webyun.management.dataanalysis.systemvisit.dto.SystemVisitDateCountDTO;
import com.njht.webyun.management.dataanalysis.systemvisit.entity.SystemVisitCountEntity;
import com.njht.webyun.management.dataanalysis.systemvisit.service.SystemVisitCountService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户访问统计信息
 *
 * @author zhouhouliang
 * @date 2021/7/14 9:01
 */
@Service
@Transactional
public class SystemVisitCountServiceImpl implements SystemVisitCountService {

    @Autowired
    SystemVisitCountDao systemVisitCountDao;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SystemVisitCountService systemVisitCountService;

    /**
     * 添加访问次数
     * 把redis中用户今天的访问次数+1
     */
    @SneakyThrows
    @Override
    public Long incrementCount() {

        String userId = String.valueOf(UserUtil.getCurrentUser().getUserId());
        final String dateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        String redisKey = SystemVisitCountConstant.SYSTEM_LOG_VISIT_COUNT_USER_COUNT_REDIS_SUFFIX + userId + ":" + dateStr;
        /* ------ 判断该条数据的缓存是否已经被预热 ------ */
        final String count = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(count)) {
            // 尝试从数据库中取出该条记录,放入redis
            final SystemVisitCountEntity systemVisitCountEntity = systemVisitCountService.selectByUserIdAndDate(userId, dateStr);
            if (systemVisitCountEntity != null) {
                stringRedisTemplate.opsForValue().set(redisKey, systemVisitCountEntity.getCount().toString());
                stringRedisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
            }
        }
        /* ------ 把redis中用户今天的访问次数+1 ------ */
        final Long increment = stringRedisTemplate.opsForValue().increment(redisKey, 1L);
        final Long expire = stringRedisTemplate.getExpire(redisKey);
        if (expire < 0) {
            stringRedisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
        }

        /* ------ 用户访问总量+1 ------ */
        incrementAllCount();

        return increment;
    }

    /**
     * 获取访问总量
     */
    @Override
    public Long selectAllCount() {
        /* 判断缓存是否预热 */
        final String redisKey = SystemVisitCountConstant.SYSTEM_LOG_VISIT_COUNT_ALL_HISTORY_COUNT_REDIS_KEY;
        String countInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(countInRedis)) {
            /* 加载缓存 */
            final Long allVisitCount = Optional.ofNullable(systemVisitCountDao.selectAllCount()).orElse(0L);
            stringRedisTemplate.opsForValue().set(redisKey, allVisitCount.toString());
        }
        return Long.valueOf(stringRedisTemplate.opsForValue().get(redisKey));
    }


    /**
     * 所有用户访问量+1
     *
     * @return
     */
    @Override
    public Long incrementAllCount() {
        /* 判断缓存是否预热 */
        final String redisKey = SystemVisitCountConstant.SYSTEM_LOG_VISIT_COUNT_ALL_HISTORY_COUNT_REDIS_KEY;
        String countInRedis = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(countInRedis)) {
            /* 加载缓存 */
            final Long allVisitCount = Optional.ofNullable(systemVisitCountDao.selectAllCount()).orElse(0L);
            stringRedisTemplate.opsForValue().set(redisKey, allVisitCount.toString());
        }
        return stringRedisTemplate.opsForValue().increment(redisKey);
    }

    /**
     * 根据 用户和时间,获取用户该日的访问记录
     *
     * @param userId
     * @param dateStr
     * @return
     */
    @Override
    @SneakyThrows
    public SystemVisitCountEntity selectByUserIdAndDate(String userId, String dateStr) {
        final Date date = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
        final SystemVisitCountEntity systemVisitCountEntity = systemVisitCountDao.selectByUserIdAndDateTime(userId, date);
        return systemVisitCountEntity;
    }

    /**
     * 更新或者插入一条新的用户访问数据
     *
     * @param userId
     * @param dateStr
     * @param count
     */
    @Override
    @SneakyThrows
    public void insertOrUpdateSystemVisitCount(String userId, String dateStr, Long count) {
        final Date date = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
        SystemVisitCountEntity systemVisitCountEntity = systemVisitCountDao.selectByUserIdAndDateTime(userId, date);
        if (systemVisitCountEntity == null) {
            // 没有信息，新增一条
            systemVisitCountDao.insertNew(count, new Date(), userId, date);
        } else {
            // 有信息,更新
            systemVisitCountEntity.setUpdateTime(new Date());
            systemVisitCountEntity.setCount(count);
            systemVisitCountEntity.setLastVisitTime(new Date());
            systemVisitCountDao.updateById(systemVisitCountEntity);
        }

    }

    /**
     * 用户访问统计
     * 周月年
     *
     * @return
     */
    @Override
    @SneakyThrows
    @Cacheable(value = SystemVisitCountConstant.SYSTEM_LOG_VISIT_COUNT_WEEK_MONTH_YEAR)
    public Map<String, List<SystemVisitDateCountDTO>> weekMonthYearStatistics() {
        /* 近一周 */
        final Calendar lastWeekCalendar = Calendar.getInstance();
        lastWeekCalendar.add(Calendar.DATE, -6);
        Date lastWeekDate = lastWeekCalendar.getTime();
        // 去掉时分秒
        lastWeekDate = DateUtils.parseDate(DateFormatUtils.format(lastWeekDate, "yyyy-MM-dd"), "yyyy-MM-dd");
        final Date currentDate = new Date();
        final String currentDateStr = DateFormatUtils.format(currentDate, "yyyy-MM-dd");
        List<SystemVisitDateCountDTO> weekSystemVisitDateCountDTOList = systemVisitCountDao.selectVisitCountByDay(lastWeekDate, currentDate)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());
        // 填充无数据时间段
        final LinkedHashMap<String, SystemVisitDateCountDTO> dateWeekDataMap = enumDayDataMap(DateFormatUtils.format(lastWeekDate, "yyyy-MM-dd"), currentDateStr, "yyyy-MM-dd", SystemVisitDateCountDTO.class);
        weekSystemVisitDateCountDTOList.forEach(systemVisitDateCountDTO -> {
            dateWeekDataMap.put(systemVisitDateCountDTO.getDate(), systemVisitDateCountDTO);
        });
        weekSystemVisitDateCountDTOList = new ArrayList<>(dateWeekDataMap.values());

        /* 近一月 */
        final Calendar lastMonthCalendar = Calendar.getInstance();
        lastMonthCalendar.add(Calendar.DATE, -30);
        Date lastMonthDate = lastMonthCalendar.getTime();
        final String lastMonthDateStr = DateFormatUtils.format(lastMonthDate, "yyyy-MM-dd");
        lastMonthDate = DateUtils.parseDate(lastMonthDateStr, "yyyy-MM-dd");
        List<SystemVisitDateCountDTO> monthSystemVisitDateCountDTOList = systemVisitCountDao.selectVisitCountByDay(lastMonthDate, currentDate)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());
        // 填充无数据时间段
        final LinkedHashMap<String, SystemVisitDateCountDTO> dateMonthDataMap = enumDayDataMap(lastMonthDateStr, currentDateStr, "yyyy-MM-dd", SystemVisitDateCountDTO.class);
        monthSystemVisitDateCountDTOList.forEach(systemVisitDateCountDTO -> {
            dateMonthDataMap.put(systemVisitDateCountDTO.getDate(), systemVisitDateCountDTO);
        });
        monthSystemVisitDateCountDTOList = new ArrayList<>(dateMonthDataMap.values());


        /* 近一年 */
        // 去年下月第一天
        final Calendar lastYearNextMonth = Calendar.getInstance();
        lastYearNextMonth.add(Calendar.YEAR, -1);
        lastYearNextMonth.add(Calendar.MONTH, 1);
        lastYearNextMonth.set(Calendar.DAY_OF_MONTH, 1);
        Date lastYearNextMonthDate = lastYearNextMonth.getTime();
        final String lastYearNextMonthDateStr = DateFormatUtils.format(lastYearNextMonthDate, "yyyy-MM-dd");
        lastYearNextMonthDate = DateUtils.parseDate(lastYearNextMonthDateStr, "yyyy-MM-dd");
        final String currentMonthStr = DateFormatUtils.format(currentDate, "yyyy-MM");
        final List<SystemVisitDateCountDTO> yearEveryDaySystemVisitDateCountDTOList = systemVisitCountDao.selectVisitCountByDay(lastYearNextMonthDate, currentDate)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());
        // 按月统计 yyyy-MM
        final ConcurrentHashMap<String, Long> monthCountMap = new ConcurrentHashMap<>();
        final Pattern pattern = Pattern.compile("(\\d{4}-\\d{1,2})-\\d{1,2}");
        yearEveryDaySystemVisitDateCountDTOList.stream().forEach(systemVisitDateCountDTO -> {
            final String dateStr = systemVisitDateCountDTO.getDate();
            final Matcher matcher = pattern.matcher(dateStr);
            matcher.find();
            final String yyyyMM = matcher.group(1);
            Long count = Optional.ofNullable(monthCountMap.get(yyyyMM)).orElse(0L);
            count += Long.parseLong(systemVisitDateCountDTO.getCount());
            monthCountMap.put(yyyyMM, count);
        });
        // 类型转换
        List<SystemVisitDateCountDTO> yearSystemVisitDateCountDTOList = new ArrayList<>();
        monthCountMap.forEach((yyyyMM, count) -> {
            yearSystemVisitDateCountDTOList.add(new SystemVisitDateCountDTO(yyyyMM, count.toString()));
        });
        // 填充无数据月份
        final LinkedHashMap<String, SystemVisitDateCountDTO> monthOfYearDataMap = enumMonthDataMap(DateFormatUtils.format(lastYearNextMonthDate, "yyyy-MM"), currentMonthStr, "yyyy-MM", SystemVisitDateCountDTO.class);
        yearSystemVisitDateCountDTOList.forEach(systemVisitDateCountDTO -> {
            monthOfYearDataMap.put(systemVisitDateCountDTO.getDate(), systemVisitDateCountDTO);
        });

        // 排序
        final List<SystemVisitDateCountDTO> sortedYearSystemVisitDateCountDTOList = monthOfYearDataMap.values().stream().sorted((o1, o2) -> {
            try {
                final Date date1 = DateUtils.parseDate(o1.getDate(), "yyyy-MM");
                final Date date2 = DateUtils.parseDate(o2.getDate(), "yyyy-MM");
                final Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date1);
                final Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date2);
                return calendar2.after(calendar1) ? -1 : 1;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());


        final Map<String, List<SystemVisitDateCountDTO>> resultMap = new LinkedHashMap<>();
        resultMap.put("week", weekSystemVisitDateCountDTOList);
        resultMap.put("month", monthSystemVisitDateCountDTOList);
        resultMap.put("year", sortedYearSystemVisitDateCountDTOList);

        return resultMap;
    }


    /**
     * 枚举出某段时间内所有的天(yyyy-MM-dd)
     *
     * @param startDateStr
     * @param endDateStr
     * @param clazz
     * @param <T>
     * @return
     */
    @SneakyThrows
    private static <T extends SystemVisitDateCountDTO> LinkedHashMap<String, T> enumDayDataMap(String startDateStr, String endDateStr, String dateFormat, Class<T> clazz) {
        // 起始和结束时间
        final Date startDate = DateUtils.parseDate(startDateStr, dateFormat);
        final Date endDate = DateUtils.parseDate(endDateStr, dateFormat);
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        final LinkedHashMap<String, T> dateDataMap = new LinkedHashMap<>();
        while (startCalendar.before(endCalendar)) {
            final String format = DateFormatUtils.format(startCalendar.getTime(), dateFormat);
            final T t = clazz.newInstance();
            t.setDate(format);
            dateDataMap.put(format, t);
            startCalendar.add(Calendar.DATE, 1);
        }
        // 最后一天也加入
        final String lastDateStr = DateFormatUtils.format(endCalendar.getTime(), dateFormat);
        final T t = clazz.newInstance();
        t.setDate(lastDateStr);
        dateDataMap.put(lastDateStr, t);
        return dateDataMap;
    }

    @SneakyThrows
    private static <T extends SystemVisitDateCountDTO> LinkedHashMap<String, T> enumMonthDataMap(String startMonthStr, String endMonthStr, String dateFormat, Class<T> clazz) {
        // 起始和结束时间
        final Date startDate = DateUtils.
                parseDate(startMonthStr, dateFormat);
        final Date endDate = DateUtils.parseDate(endMonthStr, dateFormat);
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        final LinkedHashMap<String, T> monthDataMap = new LinkedHashMap<>();
        while (startCalendar.before(endCalendar)) {
            final String format = DateFormatUtils.format(startCalendar.getTime(), dateFormat);
            final T t = clazz.newInstance();
            t.setDate(format);
            monthDataMap.put(format, t);
            startCalendar.add(Calendar.MONTH, 1);
        }
        // 最后一天也加入
        final String lastMonthStr = DateFormatUtils.format(endCalendar.getTime(), dateFormat);
        final T t = clazz.newInstance();
        t.setDate(lastMonthStr);
        monthDataMap.put(lastMonthStr, t);
        return monthDataMap;
    }


}
