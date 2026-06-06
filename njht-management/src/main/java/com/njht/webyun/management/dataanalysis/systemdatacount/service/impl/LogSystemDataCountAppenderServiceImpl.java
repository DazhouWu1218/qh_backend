package com.njht.webyun.management.dataanalysis.systemdatacount.service.impl;

import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountAppenderService;
import com.njht.webyun.management.dataanalysis.systemdatacount.service.LogSystemDataCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 记录系统中 各类数据的 接收/发布
 *
 * @author zhouhouliang
 */
@Service
@Transactional
@Slf4j
public class LogSystemDataCountAppenderServiceImpl implements LogSystemDataCountAppenderService {

    @Autowired
    LogSystemDataCountService logSystemDataCountService;

    /**
     * 累加系统中数据下载量
     *
     * @param dataName
     * @param dataSize
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long incrementSystemDataDownload(String dataName, Long dataSize) {
        try {
            return logSystemDataCountService.incrementSystemDataDownload(dataName, dataSize);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("记录系统数据:[" + dataName + "]的发布统计信息失败,exception:[" + e.getMessage() + "]");
        }
        return null;
    }
}
