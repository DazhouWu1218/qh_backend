package com.njht.webyun.management.satellite.service;

import com.njht.webyun.management.satellite.entity.HthtDmsSateDataInfo;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataInfoVO;
import com.njht.webyun.management.satellite.vo.PageInfo;
import com.njht.webyun.management.satellite.vo.SelectThematicParam;

import java.sql.Date;
import java.util.List;
import java.util.Map;


/**
 * service层
 *
 * @author miaoyu
 * @date 2020-05-25 03:19:51
 */
public interface HthtDmsSateDataInfoService {

    List<Map<String,Object>> selectSateField() throws Exception;

    HthtDmsSateDataInfo selectByPrimaryKey(String id);

    Integer deleteByPrimaryKey(Long id);

    Integer insert(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer insertSelective(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer insertSelectiveIgnore(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer updateByPrimaryKeySelective(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer updateByPrimaryKey(HthtDmsSateDataInfo hthtDmsSateDataInfo);

    Integer batchInsert(List<HthtDmsSateDataInfo> list);

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

    PageInfo<HthtDmsSateDataInfoVO> selectPagedOrderF(Integer pageNum, Integer pageSize, Double resolution
            , Date beginTime, Date endTime, String sort, List<Map> sateSensorId, Double cloud, List<Integer> dataType, Map geo, String regionId);

    Map<String,Object> sumArea(Double resolution
            , Date beginTime, Date endTime, List<Map> sateSensorId, Double cloud,List<Integer> satelliteType,Map geo,String regionId,Double area);

    List<Map<String,Object>> area(Double resolution
            , Date beginTime, Date endTime, List<Map> sateSensorId, Double cloud,List<Integer> satelliteType,Map geo,String regionId);

    Map<String, Object> areageo(Double resolution
            , Date beginTime, Date endTime, List<Map> sateSensorId, Double cloud,List<Integer> dataType,Map geo,String regionId);

    Map<String,Object> selectRegion(String id);
    /*<AUTOGEN--END>*/
    HthtDmsSateDataInfoVO queryById(String id);

    /**
     * 卫星数据下的专题数据查询
     * @param selectThematicParam
     * @return
     */

    PageInfo<Map<String, Object>> selectThematicPaged(SelectThematicParam selectThematicParam);
}
