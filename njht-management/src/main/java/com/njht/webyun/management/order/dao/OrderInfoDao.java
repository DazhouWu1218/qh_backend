package com.njht.webyun.management.order.dao;


import com.njht.webyun.management.basis.entity.BasisDataInfo;
import com.njht.webyun.management.business.entity.ProductFileInfo;
import com.njht.webyun.management.business.entity.ProductInfo;
import com.njht.webyun.management.dataanalysis.systemdatacount.entity.LogSystemDataCountEntity;
import com.njht.webyun.management.order.entity.OrderInfo;
import com.njht.webyun.management.order.entity.OrderListInfo;
import com.njht.webyun.management.order.entity.OrderMessageDTO;
import com.njht.webyun.management.order.entity.SateDataOrderEntity;
import com.njht.webyun.management.satellite.entity.HthtDmsSateDataFileInfo;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dgj
 */
@Repository
public interface OrderInfoDao {

    /**
     * 查询所有审核通过的订单
     * @return
     */
    List<OrderInfo> getOrderInfoList();

    /**
     * 改变订单的状态
     * @param id
     * @param status
     * @return
     */
    Integer updateStatus(@Param("id") int id,@Param("status") int status);

    /**
     * 查询同一订单有几种类型的产品
     * @param orderId
     * @return
     */
    List<String> getOrderIdentifier(@Param("orderId")String orderId);

    /**
     * 获取订单中的业务产品id
     * @param orderId
     * @param s
     * @return
     */
    List<ProductInfo> getProductInfoId(@Param("orderId") String orderId, @Param("identifier") String s);

    /**
     * 获取订单中的业务产品信息
     * @param productInfoId
     * @return
     */
    List<ProductFileInfo> getProductInfoList(@Param("productInfoId") String productInfoId);


    /**
     * 修改下载路径
     * @param orderId
     * @param toString
     * @return
     */
    Integer updateDownLoadPath(@Param("orderId") String orderId, @Param("toString")String toString);

    /**
     * 卫星数据
     * @param orderId
     * @param identifier
     * @return
     */
    List<SateDataOrderEntity> getSateDateInfo(@Param("orderId")String orderId, @Param("identifier")String identifier);

    /**
     * 获取卫星数据文件信息
     * @param id
     * @return
     */
    HthtDmsSateDataFileInfo getSateDateFileInfo(@Param("id") String id);


    /**
     * 购物车基础数据
     * @param orderId
     * @param s
     * @return
     */
    List<BasisDataInfo> getBasicDataList(@Param("orderId")String orderId, @Param("identifier")String s);

    /**
     * 专题数据下载
     * @param orderId
     * @param identifier
     * @return
     */
    List<OrderListInfo> getOrderListInfo(@Param("orderId") String orderId, @Param("identifier") String identifier);

    /**
     * 专题数据下载路径
     * @param dataId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<String> getThematicDataInfo(@Param("dataId") String dataId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 用户消息提醒
     * @param userName
     * @return
     */
    List<OrderMessageDTO> getOrderMessage(@Param("userName")String userName);

    /**
     * 根据id修改订单消息提醒的状态
     * @param id
     */
    void updateOrderMessageInfo(@Param("id") String id,@Param("status")Integer i);


    /**
     * 查询加入购物车的卫星专题数据信息
     * @param collect
     * @return
     */
    List<SatelliteThematicDataInfoEntity> getSatelliteThematicDataInfo(@Param("collect")ArrayList<String> collect);

    /**
     * 业务产品名称
     * @param id
     * @return
     */
    String getBusinessProductName(String id);

    /**
     * 专题数据名称
     * @param dataId
     * @return
     */
    String getThematicInfo(String dataId);

    /**
     * 查询数据对应的统计分析的类型
     * @param dataName
     * @param dataType
     * @return
     */
    Integer getSysDataCategoryId(@Param("dataName")String dataName, @Param("dataType")String dataType);

    /**
     * 查询数据信息
     * @param categoryId
     * @param date
     * @return
     */
    LogSystemDataCountEntity selectDataInfo(@Param("id") Integer categoryId, @Param("date") String date);

    void insertDataInfo(LogSystemDataCountEntity logSystemDataCountEntity);

    void updateDataInfo(Long id, Long dataSize);


}
