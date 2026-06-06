package com.njht.webyun.publish.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.publish.sys.entity.OrgEntity;

import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-30 20:05:56
 */
public interface OrgService extends IService<OrgEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过用户id查询对应的行政区域id
     * @param userId
     * @return
     */
    String queryRegionIdByUserId(Integer userId);
}

