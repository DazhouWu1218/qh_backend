package com.njht.webyun.product.dto;

import com.njht.webyun.product.entity.DataCategoryEntity;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/19 14:06
 * @Description: 扩展类
 */
@Data
public class DataCategoryDto extends DataCategoryEntity {

    //p.mark,p.product_path,p.cycle
    private String mark;
    private String productPath;
    private String cycle;
    private Integer isRelease;
}
