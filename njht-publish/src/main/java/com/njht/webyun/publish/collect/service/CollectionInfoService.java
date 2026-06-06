package com.njht.webyun.publish.collect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.publish.collect.entity.CollectionInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:53:36
 */
public interface CollectionInfoService extends IService<CollectionInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存
     * @param ids
     * @param types
     */
    void saveInfo(String ids,String types);

    /**
     * 保存收藏信息
     * @param entity
     */
    void saveOneCollectInfo(CollectionInfoEntity entity);
}

