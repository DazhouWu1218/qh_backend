package com.htht.executor.download.dao;

import com.htht.executor.download.entity.DownloadFileInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 数据下载表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-17 16:41:22
 */
@Mapper
public interface DownloadFileInfoDao extends BaseMapper<DownloadFileInfoEntity> {

    /**
     * 查询时间段范围内的数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<DownloadFileInfoEntity> selectFileListByTime(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 通过文件名删除文件
     * @param fileName
     */
    void deleteByFileName(@Param("fileName") String fileName);


    List<String> selectFileNameListByTime(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime
            ,@Param("bz") String bz);
}
