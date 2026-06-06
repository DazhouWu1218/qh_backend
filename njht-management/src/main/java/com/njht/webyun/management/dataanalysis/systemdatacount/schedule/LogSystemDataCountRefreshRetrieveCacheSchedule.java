package com.njht.webyun.management.dataanalysis.systemdatacount.schedule;

import com.njht.webyun.management.dataanalysis.systemdatacount.constant.SystemDataCountConstant;
import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 各类数据的统计信息 定时刷新查询方法的缓存
 *
 * @author zhouhouliang
 */
@Slf4j
@Component
@Transactional
public class LogSystemDataCountRefreshRetrieveCacheSchedule {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Autowired
    LogSystemDataCountService logSystemDataCountService;

//    /**
//     * TODO
//     * 每10分钟存储一次redis中的
//     * 系统各类数据的接收发布信息
//     */
//    @Scheduled(cron = "0 0/10 * * * *")
//    public void archiveSystemDataCount() {
//        log.info("缓存入库:系统中各类数据接收发布信息");
//        final Set<String> keySet = Optional
//                .ofNullable(stringRedisTemplate.keys(SystemDataCountConstant.SYSTEM_DATA_COUNT_RECORD_REDIS_SUFFIX + "*"))
//                .orElseGet(HashSet::new);
//        final Pattern pattern = Pattern.compile("^" + SystemDataCountConstant.SYSTEM_DATA_COUNT_RECORD_REDIS_SUFFIX + "(\\d{4}-\\d{1,2}-\\d{1,2}):(.{1,100}?):(download|upload)$");
//        keySet.parallelStream().forEach(redisKey -> {
//            final Matcher matcher = pattern.matcher(redisKey);
//            matcher.find();
//            Date date;
//            try {
//                date = DateUtils.parseDate(matcher.group(1), "yyyy-MM-dd");
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//            final String categoryId = matcher.group(2);
//            final boolean isDownload = "download".equals(matcher.group(3));
//            final String dataSize = stringRedisTemplate.opsForValue().get(redisKey);
//            logSystemDataCountService.updateOrInsertSystemDataCount(isDownload, Long.valueOf(categoryId), date, Long.parseLong(dataSize));
//        });
//    }


    /**
     * 每20分钟刷新一次系统中缓存:
     *  接收/分发  周月年统计
     */
    @Scheduled(cron = "0 0/20 * * * *")
    public void refreshWeekMonthYearSystemDataCount() {

        stringRedisTemplate.delete(Optional.of(stringRedisTemplate.keys(SystemDataCountConstant.SYSTEM_DATA_COUNT_TIME_STATISTICS_METHOD + "*"))
                .orElseGet(HashSet::new));

        logSystemDataCountService.countWeek(true);
        logSystemDataCountService.countWeek(false);

        logSystemDataCountService.countMonth(true);
        logSystemDataCountService.countMonth(false);

        logSystemDataCountService.countYear(true);
        logSystemDataCountService.countYear(false);

    }

    /**
     * 每二十分钟刷新一次系统中缓存:
     * 接收/分发 总量
     */
    @Scheduled(cron = "0 0/20 * * * *")
    public void refreshCountAllHistory() {
        final Set<String> keys = Optional.ofNullable(stringRedisTemplate.keys(SystemDataCountConstant.SYSTEM_DATA_COUNT_ALL_HISTORY_COUNT_METHOD + "*")).orElseGet(HashSet::new);
        stringRedisTemplate.delete(keys);
        logSystemDataCountService.countAllHistory(true);
        logSystemDataCountService.countAllHistory(false);
    }

    /**
     * 每20分钟刷新一次系统中缓存:
     *  接收/分发  按照数据类型统计
     */
    @Scheduled(cron = "0 0/20 * * * *")
    public void refreshStatisticByDataType() {
        final Set<String> keys = Optional.ofNullable(stringRedisTemplate.keys(SystemDataCountConstant.SYSTEM_DATA_COUNT_COUNT_BY_DATA_TYPE_METHOD + "*")).orElseGet(HashSet::new);
        stringRedisTemplate.delete(keys);
        logSystemDataCountService.statisticByDataType(true);
        logSystemDataCountService.statisticByDataType(false);
    }


}
