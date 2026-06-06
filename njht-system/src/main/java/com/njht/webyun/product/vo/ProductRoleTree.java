package com.njht.webyun.product.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.njht.webyun.entity.Tree;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: 代国军
 * @CreateDate: 2021/11/16 13:08
 * @Description: 产品树
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRoleTree extends Tree {

    @ApiModelProperty(value = "产品勾选状态，1勾选，0不勾选")
    private Integer status;

    public ProductRoleTree(String value, String label, String parentId, Integer status) {
        super(value, label, parentId);
        this.status = status;
    }



    public ProductRoleTree(String value, String label, String parentId) {
        super(value, label, parentId);
    }

}
