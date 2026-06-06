package com.htht.job.admin.param.dao;

import com.htht.job.core.biz.model.TaskParametersEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 09:56:31
 */
@Mapper
@Repository
public interface TaskParametersDao {

    /**
     * 通过jobId 获取要执行的任务参数
     * @param jobId
     * @return
     */
    TaskParametersEntity loadByJobId(@Param("jobId") Integer jobId);

    /**
     * 入库
     * @param taskParametersEntity
     */
    void insert(TaskParametersEntity taskParametersEntity);

    /**
     * 修改
     * @param taskParametersEntity
     */
    void update(TaskParametersEntity taskParametersEntity);

    /**
     * 通过jobId 删除
     * @param id
     */
    void deleteByJobId(@Param("jobId") int id);
}
