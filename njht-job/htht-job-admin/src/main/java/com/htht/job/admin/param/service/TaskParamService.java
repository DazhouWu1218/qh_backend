package com.htht.job.admin.param.service;

import com.htht.job.admin.dispatch.vo.JobReqVo;

/**
 * @author 代国军
 * @description: 任务参数接口
 * @date 2022/6/23 10:11
 */
public interface TaskParamService {

    /**
     * 修改 任务关联的参数
     * @param infoDto
     */
    void updateParams(JobReqVo infoDto);

    /**
     * 新增 任务关联任务参数
     * @param infoDto
     */
    void saveParams(JobReqVo infoDto);

    /**
     * 删除
     * @param id
     */
    void deleteByJobId(int id);
}
