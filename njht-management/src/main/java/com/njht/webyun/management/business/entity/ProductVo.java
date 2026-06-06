package com.njht.webyun.management.business.entity;

import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/5/26 13:59
 * @Description: java类作用描述
 */
@Data
public class ProductVo {

    private String value;
    private String label;


    public ProductVo(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public ProductVo() {
    }
}
