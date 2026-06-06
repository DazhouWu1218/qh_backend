package com.njht.webyun.management.order.service;

import java.util.List;
import java.util.Map;

/**
 * @author dgj
 */
public interface OrderDownLoadService {

    /**
     * 查询订单的状态，如果审核通过下载订单信息
     */
    void getOrderStatus();

    /**
     * 用户订单消息消息提醒
     * @param userName
     * @return
     */
    Map<String,Object> getOrderIMessage(String userName);

    /**
     * 订单消息提醒设置为已读
     * @param idList
     * @return
     */
    String updateMessageInfo(List<String> idList,Integer i);

}
