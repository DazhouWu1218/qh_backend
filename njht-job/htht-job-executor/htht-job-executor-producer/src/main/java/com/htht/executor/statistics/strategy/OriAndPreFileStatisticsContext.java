package com.htht.executor.statistics.strategy;

import com.htht.executor.statistics.base.FileStatisticsJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author zhushizhen
 * @Date 2022-09-26 17:11
 **/
@Component
public class OriAndPreFileStatisticsContext {

    @Autowired
    private Map<String, FileStatisticsJobService> typeJobMap;

    public FileStatisticsJobService getStatisticsShardByType(String type){
        return typeJobMap.get(type);
    }

}
