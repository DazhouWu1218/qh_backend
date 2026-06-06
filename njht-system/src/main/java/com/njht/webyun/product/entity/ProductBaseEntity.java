package com.njht.webyun.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/25 11:08
 * @Description: 产品基础类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String id;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;
    /**
     *
     */
    private Integer version;
    private String menuId;
}
