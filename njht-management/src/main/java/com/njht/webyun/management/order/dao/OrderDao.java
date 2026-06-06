package com.njht.webyun.management.order.dao;

import com.njht.webyun.management.order.entity.OrderFileInfo;
import com.njht.webyun.management.order.vo.HthtOrderInfo;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataInfoDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lmd
 */
@Repository
public interface OrderDao {


    /**
     * 查询订单
     * @param userId
     * @param state
     * @param orderState
     * @param beginTime
     * @param endTime
     * @return
     */
     List<HthtOrderInfo> queryByCondition(@Param("userId") String userId, @Param("state") String state, @Param("orderState") String orderState, @Param("beginTime") String beginTime, @Param("endTime") String endTime);


    /**
     * 订单list表插入数据
     *
     * @param dataId
     * @param identifier
     */
    void insertOrderListData(@Param("orderId") String orderId, @Param("dataId") String dataId, @Param("identifier") String identifier);


    /**
     * 订单Info表插入数据
     * @param orderId
     * @param orderName
     * @param dataUse
     * @param userId
     * @param realName
     * @param fileNum
     * @param fileSize
     */
    void insertOrderInfoData(@Param("orderId") String orderId,@Param("orderName") String orderName, @Param("dataUse") String dataUse, @Param("userId") String userId, @Param("realName") String realName, @Param("fileNum") int fileNum, @Param("fileSize") String fileSize);

    /**
     * 查询数据表名和表字段
     * @param menuId
     * @return
     */
    Map<String, String> queryTableNameColumn(@Param("menuId") String menuId);


    /**
     * 查询表数据
     * @param tableName
     * @param tableColumn
     * @param dataId
     * @return
     */
    List<Map<String, Object>> queryDataList(@Param("tableName") String tableName, @Param("tableColumn") String tableColumn, @Param("dataId") String dataId);


    /**
     * 审核订单
     * @param orderId
     * @param orderState
     * @param orderReason
     */
    void checkOrder(@Param("orderId") String orderId, @Param("orderState") int orderState,@Param("orderReason") String orderReason);

    /**
     * 根据订单id查询订单数据所属表名
     * @param orderId
     * @return
     */
    List<Map<String,Object>> queryTableNameByOrderId(@Param("orderId") String orderId);

    /**
     * 根据数据Id和表名查询数据
     * @param dataId
     * @param tableName
     * @return
     */
    List<Map<String, Object>> queryDataByDataTableName(@Param("dataId") String dataId, @Param("tableName") String tableName);


    /**
     * 删除订单list表
     * @param orderIds
     */
    void delOrderList(@Param("orderIds")List<String> orderIds);

    /**
     * 删除订单Info表
     * @param orderIds
     */
    void delOrderInfo(@Param("orderIds")List<String> orderIds);

    List<String> selectUserName();

    int queryDataNum(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    Map<String,String> selectOrder(@Param("orderId") String orderId, @Param("dataId") String dataId, @Param("identifier") String identifier);

    List<String> selectByUserId(String userId);

    Long selectFileSize(@Param("datas") List<Map<String, String>> datas);

    List<Map<String, Object>> queryBasicDataByDataTableName(@Param("dataId") String dataId, @Param("tableName") String tableName);


    void insertOrderDataList(@Param("datas") List<Map<String, String>> datas);

    List<Map<String, Object>> queryTotalDataList(@Param("tableName") String tableName, @Param("tableColumn") String tableColumn, @Param("dataIdList") List<String> dataIdList);

    /**
     * 订单新增文件信息
     * @param fileInfo
     */
    void insertOrderFileInfo(OrderFileInfo fileInfo);

    /**
     * 订单中某个文件修改
     * @param updateTime
     * @param filePath
     */
    void updateOrderFileInfo(@Param("updateTime")String updateTime,@Param("filePath") String filePath);

    /**
     * 按照文件名称删除
     * @param filePath
     */
    void deleteByFileName(@Param("filePath") String filePath);

    /**
     * 通过路径查询有没有重复的数据
     * @param filePath
     * @return
     */
    OrderFileInfo selectOrderFile(@Param("filePath") String filePath);

    List<Map<String, Object>> querySatelliteDataList(@Param("dataIdList") List<String> dataIdList);

    List<String> queryOrgDiataIdList(@Param("dataIdList") List<String> dataIdList);



    List<Map<String, Object>> querySatelliteDataList2(@Param("sourceFileId") String sourceFileId);

    /**
     * 查询业务产品数据详情
     * @param dataId
     * @return
     */
    List<Map<String, Object>> queryBusinessDataByDataTableName(@Param("dataId") String dataId);

    /**
     * 查询专题数据数据信息
     * @param dataIdList
     * @return
     */
    List<Map<String, Object>> selectThematicDataList(@Param("dataIdList") List<String> dataIdList);

    /**
     * 查询专题数据数据详情
     * @param dataId
     * @return
     */
    List<Map<String, Object>> queryThematicDataByDataTableName(@Param("dataId") String dataId);

    /**
     * 将专题数据插入订单list表
     * @param datas
     */
    void insertOrderThematicDataList(@Param("datas") List<Map<String, String>> datas);

    /**
     * 获取卫星数据下的专题数据
     * @param set
     * @return
     */
    List<SatelliteThematicDataInfoDTO> querySatelliteThematicdDataList(@Param(value = "set") Set<String> set);

    /**
     * 获取卫星数据下的专题数据
     * @param dataId
     * @return
     */
    SatelliteThematicDataInfoDTO querySatelliteThematicdDataTableName(String dataId);

    /**
     * 根据用户角色获取查看订单权限的信息
     * @param userId
     * @return
     */
    List<String> getUserAuthorityInfo(@Param("userId") Integer userId);

    /**
     * 获取用户角色信息
     * @param userId
     * @return
     */
    List<String> getUserRoles(String userId);
}
