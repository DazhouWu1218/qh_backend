package com.njht.webyun.management.order.vo;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/7/12 20:09
 * @Description: 订单用户
 */
@Data
public class OrderUserDTO {

    private String key;
    private String label;
    private String value;

    public OrderUserDTO() {
    }

    public OrderUserDTO(String key, String label, String value) {
        this.key = key;
        this.label = label;
        this.value = value;
    }
}
