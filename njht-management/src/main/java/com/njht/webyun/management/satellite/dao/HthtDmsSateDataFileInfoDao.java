package com.njht.webyun.management.satellite.dao;


import com.github.pagehelper.Page;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataFileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 *
 * @author 苗宇
 * @date 2020-05-25 03:19:50

 */
@Mapper
public interface HthtDmsSateDataFileInfoDao {

    Page<HthtDmsSateDataFileInfo> selectPaged(RowBounds rowBounds);

    HthtDmsSateDataFileInfo selectByPrimaryKey(String id);

    List<HthtDmsSateDataFileInfo> selectByForeignKey(@Param("fid")String fid);

    Integer deleteByPrimaryKey(String id);

    Integer insert(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    Integer insertSelective(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    Integer insertSelectiveIgnore(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    Integer updateByPrimaryKeySelective(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    Integer updateByPrimaryKey(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    Integer batchInsert(List<HthtDmsSateDataFileInfo> list);

    Integer batchInsert1(List<HthtDmsSateDataFileInfo> list);

    Integer batchUpdate(List<HthtDmsSateDataFileInfo> list);

    /**
     * 存在即更新
     *
     * @param hthtDmsSateDataFileInfo
     * @return
     */
    Integer upsert(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    /**
     * 存在即更新，可选择具体属性
     *
     * @param hthtDmsSateDataFileInfo
     * @return
     */
    Integer upsertSelective(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    List<HthtDmsSateDataFileInfo> query(HthtDmsSateDataFileInfo hthtDmsSateDataFileInfo);

    Long queryTotal();

    Integer deleteBatch(List<String> list);

    /*<AUTOGEN--END>*/

}