package com.htht.executor.fileStatistic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.entity.dataReport.DataReportEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/26 17:05
 */
@Repository
public interface DataReportDao extends BaseMapper<DataReportEntity> {

    /***
     * 添加数据入库
     * @param dataList
     */
    void insertList(@Param(value ="dataList") List<DataReportEntity> dataList);

    /**
     * 修改
     * @param updateReportList
     */
    void updateList(@Param(value ="updateReportList")List<DataReportEntity> updateReportList);

}
