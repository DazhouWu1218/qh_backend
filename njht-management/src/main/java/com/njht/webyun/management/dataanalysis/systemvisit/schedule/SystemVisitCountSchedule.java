package com.njht.webyun.management.dataanalysis.systemvisit.schedule;

import com.njht.webyun.management.dataanalysis.systemvisit.constant.SystemVisitCountConstant;
import com.njht.webyun.management.dataanalysis.systemvisit.service.SystemVisitCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class SystemVisitCountSchedule {

    @Autowired
    SystemVisitCountService systemVisitCountService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 定时将redis中的用户访问计数信息存储到数据库中
     */
    @Scheduled(cron = "0 0/10 * * * *")
    public void archiveSystemVisitCount() {
        final Set<String> keySet = Optional
                .ofNullable(stringRedisTemplate.keys(SystemVisitCountConstant.SYSTEM_LOG_VISIT_COUNT_USER_COUNT_REDIS_SUFFIX + "*"))
                .orElseGet(HashSet::new);
        final Pattern pattern = Pattern.compile("^" + SystemVisitCountConstant.SYSTEM_LOG_VISIT_COUNT_USER_COUNT_REDIS_SUFFIX + "(.{0,100}?):(\\d{4}-\\d{1,2}-\\d{1,2})$");
        keySet.parallelStream().forEach(redisKey -> {
            final Matcher matcher = pattern.matcher(redisKey);
            matcher.find();
            final String userId = matcher.group(1);
            final String dateStr = matcher.group(2);
            final String count = stringRedisTemplate.opsForValue().get(redisKey);
            systemVisitCountService.insertOrUpdateSystemVisitCount(userId, dateStr, Long.parseLong(count));
        });
    }


    /**
     * 用户访问
     * 周月年统计信息刷新
     */
    @Scheduled(cron = "0 0/20 * * * *")
    public void refreshSystemVisitWeekMonthYearStatistics() {
        stringRedisTemplate.delete(Optional.ofNullable(stringRedisTemplate.keys(SystemVisitCountConstant.SYSTEM_LOG_VISIT_COUNT_WEEK_MONTH_YEAR + "*")).orElseGet(HashSet::new));
        systemVisitCountService.weekMonthYearStatistics();
    }

}
