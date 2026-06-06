package com.njht.webyun.management.order.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * @author lmd
 */
public class HthtDmsOrderInfo {

  private String id;
  private String orderId;
  private Date createTime;
  private Date updateTime;
  private String userId;
  private long state;
  private String confirmId;
  private String confirmBz;
  private long orderState;
  private String bz;
  private List<HthtDmsOrderList> dataList;
  private long fileNum;
  private double fileSize;
  private String purpose;
  private String docFile;

  public String getDocFile() {
    return docFile;
  }

  public void setDocFile(String docFile) {
    this.docFile = docFile;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public double getFileSize() {
    return fileSize;
  }

  public void setFileSize(double fileSize) {
    this.fileSize = fileSize;
  }

  public long getFileNum() {
    return fileNum;
  }

  public void setFileNum(long fileNum) {
    this.fileNum = fileNum;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<HthtDmsOrderList> getDataList() {
    return dataList;
  }

  public void setDataList(List<HthtDmsOrderList> dataList) {
    this.dataList = dataList;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public long getState() {
    return state;
  }

  public void setState(long state) {
    this.state = state;
  }


  public String getConfirmId() {
    return confirmId;
  }

  public void setConfirmId(String confirmId) {
    this.confirmId = confirmId;
  }


  public String getConfirmBz() {
    return confirmBz;
  }

  public void setConfirmBz(String confirmBz) {
    this.confirmBz = confirmBz;
  }


  public long getOrderState() {
    return orderState;
  }

  public void setOrderState(long orderState) {
    this.orderState = orderState;
  }


  public String getBz() {
    return bz;
  }

  public void setBz(String bz) {
    this.bz = bz;
  }

}
