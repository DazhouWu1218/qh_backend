package com.njht.webyun.management.satellite.dao;


import com.github.pagehelper.Page;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataInfo;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataInfoVO;
import com.njht.webyun.management.satellite.vo.DataInfoStatics;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author miaoyu
 * @date 2020-05-25 03:19:51
 */
@Mapper
@Repository
public interface HthtDmsSateDataInfoDao {

    Page<HthtDmsSateDataInfo> selectPaged(RowBounds rowBounds);

    HthtDmsSateDataInfo selectByPrimaryKey(String id);

    Integer deleteByPrimaryKey(Long id);

    Integer insert(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer insertSelective(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer insertSelectiveIgnore(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer updateByPrimaryKeySelective(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer updateByPrimaryKey(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer batchInsert(List<HthtDmsSateDataInfo> list);

    Integer batchInsert1(List<HthtDmsSateDataInfo> list);

    Integer batchUpdate(List<HthtDmsSateDataInfo> list);

    /**
     * 存在即更新
     *
     * @param hthtDmsSateDataInfo
     * @return
     */
    Integer upsert(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    /**
     * 存在即更新，可选择具体属性
     *
     * @param hthtDmsSateDataInfo
     * @return
     */
    Integer upsertSelective(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    List<HthtDmsSateDataInfo> query(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Long queryTotal();

    Integer deleteBatch(List<Long> list);

    List<HthtDmsSateDataInfoVO> selectPagedOrder(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list
            , @Param("sorts") List sorts, @Param("cloud")Double cloud
            , @Param("dataType")Integer dataType, @Param("geostr")String geo, @Param("start")int start, @Param("end")int end);

    List<HthtDmsSateDataInfoVO> selectPagedOrderF(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list
            , @Param("sorts") List sorts, @Param("cloud")Double cloud,
                                                  @Param("dataType")List<Integer> dataType
          );

    List<HthtDmsSateDataInfoVO> selectByPngId(@Param("id") String id);
    
    int countHthtDmsSateDataInfo(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list, @Param("cloud")Double cloud
            ,@Param("dataType")Integer dataType,@Param("geostr")String geo);

    List<Map<String,String>> selectPolygen(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list
            , @Param("cloud")Double cloud
            ,@Param("dataType")Integer dataType,@Param("geostr")String geo);



    List<Map<String,Object>> selectSumArea(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list
            , @Param("cloud")Double cloud
            ,@Param("dataType")Integer dataType,@Param("geostr")String geo);

    List<Map<String,Object>> getArea(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list
            , @Param("cloud")Double cloud
            ,@Param("dataType")Integer dataType,@Param("geostr")String geo);

    List<String> getAreageo(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list
            , @Param("cloud")Double cloud
            ,@Param("dataType")Integer dataType);
    List<DataInfoStatics> getAllDataInfo(@Param("resolution")Double resolution, @Param("beginTime") Date beginTime
            , @Param("endTime") Date endTime, @Param("list") List<Map> list
            , @Param("cloud")Double cloud);
    List<String> union(@Param("geomjsona")String geomjsona,@Param("geomjsonb")String geomjsonb);

    List<String> selectRegionGeoStr(@Param("regionId")String regionId);

    List<Map<String,Object>> selectRegion(@Param("id")String id);

    List<Map<String,Object>> selectGeom(@Param("id")String id);

    Double areaPercent(@Param("covergeojson")String covergeojson,@Param("areageojson")String geojson);

    List<HthtDmsSateDataInfo>  findByCondition(Map<String, Object>  map);

    List<Map<String,Object>> selectSateField();

    List<SatelliteThematicDataInfoDTO> selectThematicByTreeId(@Param(value = "beginTimeStr") String beginTimeStr, @Param(value = "endTimeStr")String endTimeStr, @Param(value = "treeIds") List<String> treeIds);
}