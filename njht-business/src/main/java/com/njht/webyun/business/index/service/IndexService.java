package com.njht.webyun.business.index.service;

import com.njht.entity.xxljob.CountInfo;
import com.njht.webyun.business.index.vo.MonitorReqVo;

import java.util.List;

/**
 * @author 代国军
 * @description: 首页service
 * @date 2022/7/26 15:15
 */
public interface IndexService {

    /**
     * 首页任务监控
     * @param type
     * @return
     */
    List<MonitorReqVo> queryProductMonitorByType(String type);

    /**
     * 首页信息统计
     * @return
     */
    CountInfo count();


}
