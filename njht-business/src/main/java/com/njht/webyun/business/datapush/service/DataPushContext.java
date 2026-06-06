package com.njht.webyun.business.datapush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataPushContext {

    @Autowired
    private Map<String, DataPushService> dataPushMap;

    public DataPushService getDataPushService(String type){
        return dataPushMap.get(type);
    }


}
