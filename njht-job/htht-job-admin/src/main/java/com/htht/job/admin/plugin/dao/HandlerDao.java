package com.htht.job.admin.plugin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htht.job.admin.plugin.entity.HandlerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 插件管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:12:01
 */
@Mapper
@Repository
public interface HandlerDao extends BaseMapper<HandlerEntity> {

    /**
     * 查询执行器注册节点信息
     * @param handlerId
     * @return
     */
    String selectRegisterValueById(@Param("handlerId") String handlerId);
}
