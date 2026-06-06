package com.njht.webyun.management.order.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


public class  OrderParam {


    private Integer id;
    @Length(min = 1,message = "userId不能为空")
    @NotNull(message = "userId不能为null")
    private String userId;
    @Length(min = 1,message = "orderBz不能为空")
    @NotNull(message = "dataUse不能为null")
    private String dataUse;
    @Length(min = 1,message = "orderName不能为空")
    @NotNull(message = "orderName不能为null")
    private String orderName;
    private List<Map<String,String>> datas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataUse() {
        return dataUse;
    }

    public void setDataUse(String dataUse) {
        this.dataUse = dataUse;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public List<Map<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "OrderParam{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", dataUse='" + dataUse + '\'' +
                ", orderName='" + orderName + '\'' +
                ", datas=" + datas +
                '}';
    }
}
