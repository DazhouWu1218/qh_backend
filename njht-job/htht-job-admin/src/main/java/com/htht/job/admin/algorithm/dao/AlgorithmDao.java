package com.htht.job.admin.algorithm.dao;

import com.njht.entity.algorithm.AlgorithmEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 算法管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:06:10
 */
@Mapper
@Repository
public interface AlgorithmDao extends BaseMapper<AlgorithmEntity> {

    /**
     * 查询算法绑定的执行器节点
     * @param jobId
     * @return
     */
    String selectAddressListByJobId(@Param("jobId") int jobId);


    /**
     * 根据算法 id 查询已绑定的执行节点信息
     * @param parameterId
     * @return
     */
    String selectAddressListById(@Param("parameterId") String parameterId);
}
