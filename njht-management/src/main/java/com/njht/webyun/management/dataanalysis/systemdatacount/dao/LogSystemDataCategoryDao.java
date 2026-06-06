package com.njht.webyun.management.dataanalysis.systemdatacount.dao;


import com.njht.webyun.management.dataanalysis.systemdatacount.entity.LogSystemDataCategoryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogSystemDataCategoryDao {

    Long getIdByDataType(@Param("dataType") String dataType);


    Long getIdByDataName(@Param("dataName") String dataName);

    List<LogSystemDataCategoryEntity> selectByAssertDownload(@Param("assertDownload") Boolean assertDownload);

}
