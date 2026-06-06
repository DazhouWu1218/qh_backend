package com.htht.job.admin.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.job.admin.template.entity.TemplateEntity;
import com.htht.job.admin.template.vo.TemplateVo;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.utils.PageUtils;

import java.util.List;

/**
 * 模板管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-29 09:14:41
 */
public interface TemplateService extends IService<TemplateEntity> {

    /**
     * 树结构
     * @return
     * @param type
     */
    List<Tree> tree(String type);

    /**
     * 根据id高亮显示树结构的节点
     * @param type
     * @param id
     * @return
     */
    List<String> tree(String type,String id);

    /**
     * 模板列表
     * @param id
     * @param identify
     * @return
     */
    PageUtils queryPage(String id, Integer identify,Integer page,Integer size);

    /**
     * 新增
     * @param template
     */
    void insert(TemplateVo template);

    /**
     * 修改
     * @param template
     */
    void edit(TemplateVo template);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(String id);
}

