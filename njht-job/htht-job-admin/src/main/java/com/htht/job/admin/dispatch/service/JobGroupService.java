package com.htht.job.admin.dispatch.service;

import com.htht.job.admin.dispatch.core.model.XxlJobGroup;
import com.htht.job.admin.dispatch.vo.JobGroupVo;
import com.htht.job.core.biz.model.ReturnT;
import com.njht.webyun.utils.PageUtils;

import java.util.Map;

/**
 * @author 代国军
 * @description: 执行器service
 * @date 2022/5/30 14:45
 */
public interface JobGroupService {

    /**
     * 分页查询 执行器列表
     * @param jobGroupVo
     * @return
     */
    PageUtils queryPageList(JobGroupVo jobGroupVo);

    /**
     * 新增
     * @param xxlJobGroup
     * @return
     */
    ReturnT<String> insert(XxlJobGroup xxlJobGroup);

    /**
     * 编辑
     * @param xxlJobGroup
     * @return
     */
    ReturnT<String> edit(XxlJobGroup xxlJobGroup);

    /**
     * 删除
     * @param id
     * @return
     */
    ReturnT<String> delete(int id);

    /**
     * 查询执行器节点信息
     * @param id
     * @return
     */
    XxlJobGroup loadById(int id);

    /**
     * 地址信息
     * @return
     */
    Map<String, Object> addressInfo();

    /**
     * 通过id 查询名称
     * @param groupId
     * @return
     */
    String getNameById(Integer groupId);
}
