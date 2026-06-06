package com.njht.webyun.publish.behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.publish.behavior.entity.BehaviorDownloadLogEntity;
import com.njht.webyun.publish.behavior.vo.CountInfoReqVo;
import com.njht.webyun.publish.behavior.vo.ProductCountReqVo;

import java.util.List;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-12-08 09:09:54
 */
public interface BehaviorDownloadLogService extends IService<BehaviorDownloadLogEntity> {


    /**
     * 统计用户注册量，用户访问量，数据下载量
     * @return
     */
    List<CountInfoReqVo> getStatisticCountInfo();

    /**
     * 查询产品统计信息
     * @return
     */
    List<ProductCountReqVo> queryProductCountInfoList();
}

