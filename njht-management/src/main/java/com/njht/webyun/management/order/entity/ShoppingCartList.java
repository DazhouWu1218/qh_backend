package com.njht.webyun.management.order.entity;


import lombok.Data;

import java.util.Date;

/**
 * @author zhushizhen
 */
@Data
public class ShoppingCartList {
    private Integer id;
    private String cartId;
    private Date createTime;
    private String dataId;
    private String identifier;
    private String queryTime;

    private String dataType;


}
