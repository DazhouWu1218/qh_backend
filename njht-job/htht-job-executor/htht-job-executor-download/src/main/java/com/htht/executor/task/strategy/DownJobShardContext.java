package com.htht.executor.task.strategy;

import com.htht.executor.task.service.BaseDownJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DownJobShardContext {

    @Autowired
    private Map<String, BaseDownJobService> downJobMap;

    public BaseDownJobService getDownJobShardByType(String type){
        return downJobMap.get(type);
    }
}
