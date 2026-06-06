package com.njht.webyun.business.datapush.service;


import java.util.List;

/**
 * @author 代国军
 * @description: 下载任务服务层
 * @date 2022/5/18 9:10
 */
public interface DataPushService {
    /**
     * 处理请求参数
     * @return
     */
    List<?> execute();

}
