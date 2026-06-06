package com.njht.webyun.management.order.entity;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/21 10:34
 * @Description: 订单消息提醒
 */
@Data
public class OrderMessageDTO {

    private String id;
    private String orderTime;
    private String orderId;
    private String orderState;
    private String orderLog;
    private String messageStatus;
}
