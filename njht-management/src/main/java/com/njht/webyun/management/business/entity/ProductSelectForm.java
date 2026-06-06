package com.njht.webyun.management.business.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/5/27 10:10
 * @Description: java类作用描述
 */
@Data
public class ProductSelectForm {

    private List<ProductVo> cycleList;
    private List<ProductVo> dataSourceList;
    private List<ProductVo> productList;
    private UnitInfo unit;
    private Boolean isAno;

}
