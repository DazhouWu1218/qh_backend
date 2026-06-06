package com.njht.webyun.business.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.business.sys.entity.DicEntity;

import java.util.List;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-22 11:54:17
 */
public interface DicService extends IService<DicEntity> {

    /**
     * 从字典表中获取数据推送相关信息
     * @return
     */
    List<DicEntity>  getDataPushInfo(String type);

    /**
     * 获取数据推送url
     * @return
     */
    String getDiUrl();
}

