package com.htht.job.admin.template.dao;

import com.htht.job.admin.template.entity.TemplateParametersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模板参数表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
@Mapper
public interface TemplateParametersDao extends BaseMapper<TemplateParametersEntity> {

    /**
     * 根据算法id 查询算法模板
     * @param id
     * @return
     */
    List<TemplateParametersEntity> selectTemplateParamListByAlgorithmId(@Param("id") String id);


    /**
     * 根据插件id 查询调度模板
     * @param id
     * @return
     */
    List<TemplateParametersEntity> selectTemplateParamListByPluginId(@Param("id") String id);

}
