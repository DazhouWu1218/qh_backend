package com.njht.webyun.management.order.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateShoppingCartInfo {
    private String userId;
    private String type;
    private List<Map<String,String>> datas;

    private String dataType;


}
