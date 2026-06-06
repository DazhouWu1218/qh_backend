package com.htht.executor.fileStatistic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangjin
 * @email jiangjin@piesat.cn
 * @date 2022/9/23 16:05
 */
@Repository
public interface DataBaseInfoDao extends BaseMapper<DataBaseInfoEntity> {

    void insertAll(@Param("dataBaseInfoEntityList")List<DataBaseInfoEntity> dataBaseInfoEntityList);

}
