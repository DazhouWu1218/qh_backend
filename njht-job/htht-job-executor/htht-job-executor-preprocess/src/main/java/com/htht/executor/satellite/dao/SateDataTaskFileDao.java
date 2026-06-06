package com.htht.executor.satellite.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htht.executor.satellite.entity.SateDataTaskFileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 卫星数据在入库流程中,文件的记录表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-19 11:40:19
 */
@Mapper
public interface SateDataTaskFileDao extends BaseMapper<SateDataTaskFileEntity> {


    /**
     * 查询文件名称
     * @param satelliteId
     * @param sensorId
     * @param resolution
     * @return
     */
    List<String> selectFileNameBySatellite(@Param("satelliteId") String satelliteId,
                                           @Param("sensorId")String sensorId,
                                           @Param("resolution")Double resolution);
}
