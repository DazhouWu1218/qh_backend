package com.njht.webyun.management.order.dao;


import com.njht.webyun.management.order.entity.HthtDmsOrderDetail;
import com.njht.webyun.management.order.entity.HthtDmsOrderInfo;
import com.njht.webyun.management.order.vo.HthtOrderDataInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface SateOrderDao {
	
	void insertOrder(@Param("userId") String userId,@Param("orderId") String orderId,@Param("purpose") String purpose,@Param("fileNum") int fileNum,@Param("fileSize") double fileSize);

	void insertOrderData(@Param("orderId")String orderId,@Param("records") List<String> records);

	void delOrderInNums(@Param("orderIds")List<String> orderIds);

	void delOrderListInNums(@Param("orderIds")List<String> orderIds);

	void updateOrder(@Param("orderIds")List<String> orderIds,@Param("state")String state,@Param("userId")String userId,@Param("bz")String bz);

	List<HthtDmsOrderInfo>  queryByCondition(@Param("userId")String userId, @Param("state")String state, @Param("orderState")String orderState, @Param("beginTime") Date beginTime, @Param("endTime")Date endTime);

	List<HthtDmsOrderInfo>  queryByConditions(RequestParam requestParam);

	List<String>  queryOrderdatas(RequestParam requestParam);

	List<HthtOrderDataInfo>  queryOrderList(@Param("orderId")String orderId);

	List<String>  queryOrderData(@Param("list")List<String> list);


	List<HthtDmsOrderDetail> queryOrderByCondition(@Param("userId")String userId, @Param("state")String state, @Param("orderState")String orderState, @Param("beginTime") Date beginTime, @Param("endTime")Date endTime);

    List<HthtDmsOrderInfo> queryOrderInfoId(@Param("orderId") String orderId);

	void updateOrderByOrderId(@Param("orderId") String orderId, @Param("docPath") String docPath);
}
