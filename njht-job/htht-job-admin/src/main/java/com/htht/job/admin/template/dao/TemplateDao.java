package com.htht.job.admin.template.dao;

import com.htht.job.admin.template.entity.TemplateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模板管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
@Mapper
public interface TemplateDao extends BaseMapper<TemplateEntity> {
	
}
