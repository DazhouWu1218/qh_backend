package com.htht.executor.task.util;

import com.htht.executor.task.service.BaseProductHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 代国军
 * @description: 获取base具体的实现类对象
 * @date 2022/5/10 22:04
 */
@Component
public class ProductServiceContext {

    @Autowired
    private Map<String, BaseProductHandlerService> baseProductInterfaceMap;

    public BaseProductHandlerService getProductServiceImpl(String type){
        return baseProductInterfaceMap.get(type);
    }
}
