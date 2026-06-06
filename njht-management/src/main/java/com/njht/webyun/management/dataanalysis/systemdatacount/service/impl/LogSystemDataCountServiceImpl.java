package com.njht.webyun.management.dataanalysis.systemdatacount.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.njht.webyun.management.dataanalysis.systemdatacount.constant.SystemDataCategoryConstant;
import com.njht.webyun.management.dataanalysis.systemdatacount.constant.SystemDataCountConstant;
import com.njht.webyun.management.dataanalysis.systemdatacount.dao.LogSystemDataCategoryDao;
import com.njht.webyun.management.dataanalysis.systemdatacount.dao.LogSystemDataCountDao;
import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCountByDayDTO;
import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCountDTO;
import com.njht.webyun.management.dataanalysis.systemdatacount.entity.LogSystemDataCategoryEntity;
import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountService;
import com.njht.webyun.management.dataanalysis.systemdatacount.utils.FileCountUtils;
import com.njht.webyun.management.dataanalysis.systemdatacount.vo.LogSystemDataByDataTypeVO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 记录系统数据的接收和下载数量
 *
 * @author zhouhouliang
 */
@Service
@Transactional
public class LogSystemDataCountServiceImpl implements LogSystemDataCountService {


    @Autowired
    LogSystemDataCountDao logSystemDataCountDao;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    LogSystemDataCategoryDao logSystemDataCategoryDao;

    @Autowired
    LogSystemDataCountService logSystemDataCountService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 累加系统各类数据的下载信息
     *
     * @param dataName
     * @param dataSize
     */
    @SneakyThrows
    @Override
    public Long incrementSystemDataDownload(String dataName, Long dataSize) {
        final String currentDateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        // 根据数据名,获取数据分类
        final Long categoryId = logSystemDataCountService.getCategoryId(true, dataName);
        /* ------ 累加 ------ */
        final Date currentDay = DateUtils.parseDate(currentDateStr, "yyyy-MM-dd");
        final LogSystemDataCountDTO logSystemDataCountDTO = logSystemDataCountDao.selectByCategoryAndDate(categoryId, currentDay);
        if (logSystemDataCountDTO == null) {
            logSystemDataCountDao.insertNew(categoryId, currentDay, dataSize);
            return dataSize;
        } else {
            final long count = Long.parseLong(logSystemDataCountDTO.getDataSize()) + dataSize;
            logSystemDataCountDao.updateDataSizeByCategoryIdAndDate(categoryId, currentDay, count);
            return count;
        }
    }

    /**
     * 更新或者新增 系统数据统计信息
     *
     * @param isDownload
     * @param categoryId
     * @param dateSize
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOrInsertSystemDataCount(Boolean isDownload, Long categoryId, Date date, Long dateSize) {
        // 查询库表中是否存在统计记录
        final LogSystemDataCountDTO logSystemDataCountDTO = logSystemDataCountDao.selectByCategoryAndDate(categoryId, date);
        if (logSystemDataCountDTO != null) {
            // 系统中有记录,更新
            logSystemDataCountDao.updateDataSizeByCategoryIdAndDate(categoryId, logSystemDataCountDTO.getDate(), dateSize);
        } else {
            // 系统中没有记录,新增
            logSystemDataCountDao.insertNew(categoryId, date, dateSize);
        }
    }


    /**
     * 统计近一周的数据
     *
     * @param isDownload
     * @return
     */
    @Override
    @SneakyThrows
    @Cacheable(value = SystemDataCountConstant.SYSTEM_DATA_COUNT_TIME_STATISTICS_METHOD + "countWeek", key = "#isDownload")
    public List<LogSystemDataCountByDayDTO> countWeek(boolean isDownload) {
        /* 开始时间 */
        final Calendar firstWeekCalendar = Calendar.getInstance();
        firstWeekCalendar.add(Calendar.DATE, -6);
        Date firstWeekDate = firstWeekCalendar.getTime();
        String firstWeekDateStr = DateFormatUtils.format(firstWeekDate, "yyyy-MM-dd");
        firstWeekDate = DateUtils.parseDate(firstWeekDateStr, "yyyy-MM-dd");
        // 去掉时分秒
        firstWeekDate = DateUtils.parseDate(DateFormatUtils.format(firstWeekDate, "yyyy-MM-dd"), "yyyy-MM-dd");
        /* 最后时间 */
        final Date endDate = new Date();
        final String endDateStr = DateFormatUtils.format(endDate, "yyyy-MM-dd");

        final List<LogSystemDataCountByDayDTO> logSystemDataCountByDayDTOS = logSystemDataCountDao.selectSystemDataCountByDay(isDownload, firstWeekDate, endDate)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());
        // 数据统计,没有的数据的天也要举出来,并赋值0
        final HashMap<String, LogSystemDataCountByDayDTO> oneWeekDataMap = enumDayDataMap(firstWeekDateStr, endDateStr, "yyyy-MM-dd", LogSystemDataCountByDayDTO.class);
        // 填充实际数据
        logSystemDataCountByDayDTOS.forEach(logSystemDataCountByDayDTO -> {
            oneWeekDataMap.put(logSystemDataCountByDayDTO.getDate(), logSystemDataCountByDayDTO);
        });
        return new ArrayList<>(oneWeekDataMap.values());
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
    private static <T extends LogSystemDataCountByDayDTO> LinkedHashMap<String, T> enumDayDataMap(String startDateStr, String endDateStr, String dateFormat, Class<T> clazz) {
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
    private static <T extends LogSystemDataCountByDayDTO> LinkedHashMap<String, T> enumMonthDataMap(String startMonthStr, String endMonthStr, String dateFormat, Class<T> clazz) {
        // 起始和结束时间
        final Date startDate = DateUtils.parseDate(startMonthStr, dateFormat);
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


    /**
     * 统计近一月的数据
     *
     * @param isDownload
     * @return
     */
    @Override
    @SneakyThrows
    @Cacheable(value = SystemDataCountConstant.SYSTEM_DATA_COUNT_TIME_STATISTICS_METHOD + "countMonth", key = "#isDownload")
    public List<LogSystemDataCountByDayDTO> countMonth(boolean isDownload) {
        final Calendar firstMonthCalendar = Calendar.getInstance();
        firstMonthCalendar.add(Calendar.DATE, -30);
        Date firstMonthDate = firstMonthCalendar.getTime();
        final String firstMonthDayStr = DateFormatUtils.format(firstMonthDate, "yyyy-MM-dd");
        firstMonthDate = DateUtils.parseDate(firstMonthDayStr, "yyyy-MM-dd");
        final Date endDate = new Date();
        final String endDateStr = DateFormatUtils.format(endDate, "yyyy-MM-dd");
        final List<LogSystemDataCountByDayDTO> logSystemDataCountByDayDTOS = logSystemDataCountDao.selectSystemDataCountByDay(isDownload, firstMonthDate, endDate)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());
        /* 数据统计,没有的数据的天也要举出来,并赋值0 */
        final LinkedHashMap<String, LogSystemDataCountByDayDTO> dateDataMap = enumDayDataMap(firstMonthDayStr, endDateStr, "yyyy-MM-dd", LogSystemDataCountByDayDTO.class);
        logSystemDataCountByDayDTOS.forEach(logSystemDataCountByDayDTO -> {
            dateDataMap.put(logSystemDataCountByDayDTO.getDate(), logSystemDataCountByDayDTO);
        });

        return new ArrayList<>(dateDataMap.values());
    }

    /**
     * 统计近一年的数据
     *
     * @param isDownload
     * @return
     */
    @Override
    @SneakyThrows
    @Cacheable(value = SystemDataCountConstant.SYSTEM_DATA_COUNT_TIME_STATISTICS_METHOD + "countYear", key = "#isDownload")
    public List<LogSystemDataCountByDayDTO> countYear(boolean isDownload) {
        // 去年下月第一天
        final Calendar lastYearNextMonth = Calendar.getInstance();
        lastYearNextMonth.add(Calendar.YEAR, -1);
        lastYearNextMonth.add(Calendar.MONTH, 1);
        lastYearNextMonth.set(Calendar.DAY_OF_MONTH, 1);
        Date lastYearNextMonthDate = lastYearNextMonth.getTime();
        lastYearNextMonthDate = DateUtils.parseDate(DateFormatUtils.format(lastYearNextMonthDate, "yyyy-MM-dd"), "yyyy-MM-dd");

        // 查出去年下月第一天到现在的数据量
        final Date currentDate = new Date();
        final String currentMonth = DateFormatUtils.format(currentDate, "yyyy-MM");
        final List<LogSystemDataCountByDayDTO> logSystemDataCountByDayDTOS = logSystemDataCountDao.selectSystemDataCountByDay(isDownload, lastYearNextMonthDate, currentDate)
                .stream().filter(Objects::nonNull).collect(Collectors.toList());

        // 按月统计 yyyy-MM
        final ConcurrentHashMap<String, Long> monthCountMap = new ConcurrentHashMap<>();
        final Pattern pattern = Pattern.compile("(\\d{4}-\\d{1,2})-\\d{1,2}");
        logSystemDataCountByDayDTOS.stream().forEach(logSystemDataCountByDayDTO -> {
            final String dateStr = logSystemDataCountByDayDTO.getDate();
            final Matcher matcher = pattern.matcher(dateStr);
            matcher.find();
            final String yyyyMM = matcher.group(1);
            Long count = Optional.ofNullable(monthCountMap.get(yyyyMM)).orElse(0L);
            count += Long.parseLong(logSystemDataCountByDayDTO.getCount());
            monthCountMap.put(yyyyMM, count);
        });
        /* ------- 类型转换 ------ */
        final List<LogSystemDataCountByDayDTO> yearLogSystemDataCountByDayDTOList = new ArrayList<>();
        monthCountMap.forEach((yyyyMM, count) -> {
            yearLogSystemDataCountByDayDTOList.add(new LogSystemDataCountByDayDTO(yyyyMM, count.toString()));
        });


        /* ----- 填充无数据的月份 ----- */
        final LinkedHashMap<String, LogSystemDataCountByDayDTO> monthDataMap = enumMonthDataMap(DateFormatUtils.format(lastYearNextMonthDate, "yyyy-MM"), currentMonth, "yyyy-MM", LogSystemDataCountByDayDTO.class);
        for (LogSystemDataCountByDayDTO logSystemDataCountByDayDTO : yearLogSystemDataCountByDayDTOList) {
            monthDataMap.put(logSystemDataCountByDayDTO.getDate(), logSystemDataCountByDayDTO);
        }

        /* ------ 排序 ------ */
        final List<LogSystemDataCountByDayDTO> sortedYearLogSystemDataCountByDayDTOList = monthDataMap.values().stream().sorted((o1, o2) -> {
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

        return sortedYearLogSystemDataCountByDayDTOList;
    }


    /**
     * 累计接收,累计分发
     *
     * @param isDownload
     * @return
     */
    @Override
    @Cacheable(value = SystemDataCountConstant.SYSTEM_DATA_COUNT_ALL_HISTORY_COUNT_METHOD, key = "#isDownload")
    public Long countAllHistory(boolean isDownload) {
        return logSystemDataCountDao.countAllHistory(isDownload);
    }


    /**
     * 系统中各类数据 按照数据类型统计汇总
     *
     * @param isDownload
     * @return
     */
    @Override
    @Cacheable(value = SystemDataCountConstant.SYSTEM_DATA_COUNT_COUNT_BY_DATA_TYPE_METHOD, key = "#isDownload")
    public List<LogSystemDataByDataTypeVO> statisticByDataType(Boolean isDownload) {
        final List<LogSystemDataCountDTO> logSystemDataCountDTOS = logSystemDataCountDao.statisticByDataType(isDownload);
        final List<LogSystemDataCountDTO> sortedLogSystemDataCountDTOS = logSystemDataCountDTOS
                .stream()
                // 排序
                .sorted((o1, o2) -> {
                    final Integer dataNameSort1 = o1.getDataNameSort();
                    final Integer dataTypeSort1 = o1.getDataTypeSort();
                    final Integer dataNameSort2 = o2.getDataNameSort();
                    final Integer dataTypeSort2 = o2.getDataTypeSort();

                    if (dataTypeSort2 > dataTypeSort1) {
                        // 先比较类型
                        return -1;
                    } else if (dataTypeSort2 == dataTypeSort1) {
                        if (dataNameSort2 > dataNameSort1) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        return 1;
                    }
                }).collect(Collectors.toList());


        // 按照data_type 归纳
        final Map<String, LogSystemDataByDataTypeVO> resultMap = new LinkedHashMap<>();
        sortedLogSystemDataCountDTOS.forEach(logSystemDataCountDTO -> {
            // 单位 byte-gb
            logSystemDataCountDTO.setDataSize(FileCountUtils.byteToGB(Long.valueOf(logSystemDataCountDTO.getDataSize())));
            // 归纳
            final LogSystemDataByDataTypeVO logSystemDataByDataTypeVO = Optional
                    .ofNullable(resultMap.get(logSystemDataCountDTO.getDataType()))
                    .orElseGet(() -> new LogSystemDataByDataTypeVO(logSystemDataCountDTO.getDataType(), new ArrayList<>()));
            logSystemDataByDataTypeVO.getLogSystemDataCountDTOS().add(logSystemDataCountDTO);
            resultMap.put(logSystemDataCountDTO.getDataType(), logSystemDataByDataTypeVO);
        });
        return new ArrayList<>(resultMap.values());
    }


    /**
     * 根据要素获取数据所属分类
     *
     * @param isDownload
     * @param dataName
     * @return
     */
    @Override
    @SneakyThrows
    @Cacheable(value = SystemDataCategoryConstant.SYSTEM_DATA_CATEGORY_CACHE_REDIS_SUFFIX, key = "#isDownload+'-'+#dataName")
    public Long getCategoryId(@NotNull boolean isDownload, @NotNull String dataName) {
        /* ------- 判断数据所属分类 ------- */
        // 尝试根据dataName 匹配分类
        Long categoryId = logSystemDataCategoryDao.getIdByDataName(dataName);
        if (categoryId == null) {
            // 不能完全匹配分类,尝试正则匹配
            List<LogSystemDataCategoryEntity> logSystemDataCategoryEntities = logSystemDataCategoryDao.selectByAssertDownload(isDownload);
            for (LogSystemDataCategoryEntity logSystemDataCategoryEntity : logSystemDataCategoryEntities) {
                final String matchRegexJson = logSystemDataCategoryEntity.getMatchRegexJson();
                if (StringUtils.isNotBlank(matchRegexJson)) {
                    final List<String> regexList = objectMapper.readValue(matchRegexJson, ArrayList.class);
                    for (String regex : regexList) {
                        final boolean find = Pattern.compile(regex).matcher(dataName).find();
                        if (find) {
                            categoryId = logSystemDataCategoryEntity.getId();
                            break;
                        }
                    }
                }
            }
            Assert.isTrue(categoryId != null, "数据[" + dataName + "]找不到分类");
        }
        return categoryId;
    }
}
