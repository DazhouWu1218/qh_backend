package com.njht.webyun.management.dataanalysis.systeminformation.service.impl;

import com.njht.webyun.management.dataanalysis.systeminformation.entity.CimissGribDataFileInfoEntity;
import com.njht.webyun.management.dataanalysis.systeminformation.service.PretreatmentDataService;
import com.njht.webyun.management.satellite.service.HthtDmsSateDataFileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * (CimissGribDataFileInfo)表服务实现类
 * <p>
 * 预处理结果统计
 *
 * @author makejava
 * @since 2021-06-29 09:57:03
 */
@Slf4j
@Service
public class PretreatmentDataServiceImpl implements PretreatmentDataService {


    /**
     * 系统各类数据接收/发放统计
     * 周月年 统计
     * 缓存方法 redis前缀
     */
    public static final String PRETREATMENT_FILE_SIZE_COUNT_METHOD_REDIS_KEY = "systemLog:ServiceAndPretreatment:method:pretreatmentFileSizeCount";

//    @Autowired
//    private PretreatmentDataDao pretreatmentDataDao;

    @Autowired
    HthtDmsSateDataFileInfoService hthtDmsSateDataFileInfoService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 获取格点数据预处理结果文件大小
     * 带缓存的方法
     *
     * @return
     */
    @Override
    @Cacheable(PRETREATMENT_FILE_SIZE_COUNT_METHOD_REDIS_KEY)
    public Long getPretreatmentFileSizeCount() {
        log.info("执行预处理生产统计方法.");
        /* 格点数据 */
        log.info("开始统计格点数据预处理结果文件大小....");
//        Long pretreatmentGridCountLong = pretreatmentDataDao.countFileSizeByDataType(0);
        log.info("开始统计格点数据预处理结果文件大小....");
        /* 卫星数据 */
//        final Long pretreatmentSateCountLong = Optional.ofNullable(pretreatmentDataDao.countAllPretreatmentSateData()).orElse(0L);
        return 0L;
    }


    /**
     * 定时刷新预处理数据量统计
     */
//    @Scheduled(cron = "0 0/20 * * * *")
    @Override
    public void refreshPretreatmentFileSizeCount() {
        final Long count = getPretreatmentFileSizeCount();
        final String redisKey = PRETREATMENT_FILE_SIZE_COUNT_METHOD_REDIS_KEY;
        stringRedisTemplate.delete(redisKey);
        stringRedisTemplate.opsForValue().set(redisKey, count.toString());
        stringRedisTemplate.expire(redisKey, 20, TimeUnit.MINUTES);
    }


    @Autowired
    PretreatmentDataService pretreatmentDataService;

    /**
     * 补充cimiss_grib_data_file_info file_size 属性
     */
    @Override
    public void fillCmissGribDataFile() {
//        List<CimissGribDataFileInfoEntity> cimissGribDataFileInfoEntityList = pretreatmentDataDao.getEmptyFileSizeFilePath(0);
//        log.info("查询到{}条Cmiss 格点预处理数据", cimissGribDataFileInfoEntityList.size());
//        cimissGribDataFileInfoEntityList.parallelStream().forEach(cimissGribDataFileInfoEntity -> {
//            pretreatmentDataService.fillSigleGribDataFileSize(cimissGribDataFileInfoEntity);
//        });
    }

    /**
     * @param cimissGribDataFileInfoEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void fillSigleGribDataFileSize(CimissGribDataFileInfoEntity cimissGribDataFileInfoEntity) {
//        String filePath = cimissGribDataFileInfoEntity.getFilePath();
//        File file = new File(filePath);
//        if (file.exists()) {
//            long length = file.length();
//            pretreatmentDataDao.updateCimssGribDataFileSize(cimissGribDataFileInfoEntity.getId(), length);
//            log.info("更新文件[{}],大小[{}]", filePath, length);
//        }

    }
}
