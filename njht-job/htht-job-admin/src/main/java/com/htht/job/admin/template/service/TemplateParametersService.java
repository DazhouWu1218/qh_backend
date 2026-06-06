package com.htht.job.admin.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.job.admin.template.entity.TemplateEntity;
import com.htht.job.admin.template.entity.TemplateParametersEntity;
import com.htht.job.admin.template.vo.TemplateParamReqVo;
import com.htht.job.admin.template.vo.TemplateParamVo;

import java.util.List;

/**
 * 模板参数表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
public interface TemplateParametersService extends IService<TemplateParametersEntity> {

    /**
     * 添加模板参数相关信息
     * @param templateList
     */
    List<?> getParamListInfo(List<TemplateEntity> templateList);

    /**
     * 新增 或 修改
     * @param paramVo
     */
    void insertOrEdit(TemplateParamVo paramVo);

    /**
     * 任务调度 查询调度模板 和算法模板
     * @param id
     * @param num
     * @return
     */
    List<TemplateParamReqVo> getParamList(String id, int num);

    /**
     * 删除
     * @param id
     */
    void deleteByTemplateId(String id);
}

