package com.htht.executor.task.util;

import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author 代国军
 * @description: 获取base具体的实现类对象
 * @date 2022/5/10 22:04
 */
@Component
public class ProductServiceUtil {


    @Autowired
    private ProductServiceContext productServiceContext;

    /**
     * 获取 baseProductHandlerService 实现类对象
     * @param taskParam
     * @return
     */
    public BaseProductHandlerService getServiceInfo(TaskParam taskParam) {
        // 动态参数
        LinkedHashMap<String, Object> dyMap = taskParam.getDynamicMap();
        //“businessService的value”+"HandlerService"字符串拼接
        String businessName = BaseProductServiceConstant.Input_businessService;
        // 默认执行的bean为 baseBusinessHandlerService
        String businessService = BaseProductServiceConstant.BASE_DEFAULT_BUSINESS + BaseProductServiceConstant.BusinessServiceSuffix;
        if (dyMap.containsKey(businessName)) {
            businessName = (String) dyMap.get(businessName);
            if (StringUtils.isNotBlank(businessName)){
                businessService = businessName + BaseProductServiceConstant.BusinessServiceSuffix;
            }
        }
        BaseProductHandlerService productHandlerService = productServiceContext.getProductServiceImpl(businessService);
        if (productHandlerService == null) {
            throw new CommonException("参数businessService:"+businessName+"错误,没有对应的实现类方法");
        }
        return productHandlerService;
    }
}
