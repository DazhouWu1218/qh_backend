package com.njht.webyun.publish.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/28 10:38
 * @Description: 周期相关信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCycleReqVo {

    private String label;
    private String value;
    private Integer sort;
}
