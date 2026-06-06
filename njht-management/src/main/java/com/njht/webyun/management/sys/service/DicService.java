package com.njht.webyun.management.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.management.sys.entity.DicEntity;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-22 11:54:17
 */
public interface DicService extends IService<DicEntity> {

    /**
     * 下载url 存储到字典表中
     * @return
     */
    String selectDownUrl();
}

