package com.njht.webyun.management.order.service;


import com.njht.webyun.management.order.entity.OrderInfoParam;

import java.util.List;
import java.util.Map;

/**
 * @author lmd
 */
public interface OrderService {
    /**
     * 提交订单
     * @param userId
     * @param dataUse
     * @param orderName
     * @param datas
     * @return
     */
    String submitOrder(String userId, String dataUse,String orderName,List<Map<String,String>> datas) throws Exception;

    /**
     * 根据用户查询订单
     * @param userId
     * @param state
     * @param orderState
     * @param beginTime
     * @param endTime
     * @return
     * @throws Exception
     */
    Map<String,Object> queryUserOrder(String userId,String systemUserId, String state, String orderState, String beginTime, String endTime) throws Exception;

    /**
     * 订单审核
     * @param orderIds
     * @param state
     * @param orderReason
     */
    void  checkOrder(String orderIds,String state,String orderReason);

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    List<Map<String, Object>> queryOrderDetail(String orderId);

   /* *//**
     * 删除订单
     * @param orderIds
     *//*
    void  delOrderInNums(List<String> orderIds) throws Exception;*/
    /**
     * 删除订单
     * @param orderInfoParam
     */
    void  delOrderInNums(OrderInfoParam orderInfoParam);

    /**
     * 个人中心查询订购人姓名
     * @return
     */
    Map<String, String> selectUserName();


}
