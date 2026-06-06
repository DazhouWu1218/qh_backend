package com.htht.executor.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.executor.sys.entity.DicEntity;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-22 11:54:17
 */
public interface DicService extends IService<DicEntity> {

    /**
     * satellitePath 为系统根目录字典表配置, 方便nginx代理路径配置，绝对路径变为相对路径
     * @return
     */
    String findSateUrl();
}

