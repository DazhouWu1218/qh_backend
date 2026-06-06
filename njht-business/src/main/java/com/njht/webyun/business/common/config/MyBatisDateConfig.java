package com.njht.webyun.business.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/24 11:30
 * @Description: 自动填充创建时间，修改时间
 */
@Component
public class MyBatisDateConfig implements MetaObjectHandler {

    /**
     * 新增時間填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdDate",new Date(),metaObject);
        this.setFieldValByName("lastUpdatedDate",new Date(),metaObject);
    }

    /**
     * 修改时间填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("lastUpdatedDate",new Date(),metaObject);

    }

}
