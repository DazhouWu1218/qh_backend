package com.njht.webyun.management.order.vo;


import com.njht.webyun.management.order.entity.HthtDmsOrderList;
import lombok.Data;

import java.util.List;

/**
 * @author lmd
 */
@Data
public class HthtOrderInfo {
  private String id;
  private String orderId;
  private String orderName;
  private String dataUse;

  private String createTime;
  private String updateTime;
  private String identifier;
  private String userId;
  private String realName;
  private Integer fileNum;
  private String fileSize;
  private Integer orderState;
  private Integer state;
  private String orderReason;
  private Integer operation;
  private List<HthtDmsOrderList> dataList;

  private String downloadPath;
}
