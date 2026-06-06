package com.njht.webyun.publish.product.vo;

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
public class ProductTree extends Tree {

    @ApiModelProperty(value = "图片路径")
    private String ImgUrl;

    public ProductTree(String value, String label, String parentId,String imgUrl) {
        super(value, label, parentId);
        this.ImgUrl = imgUrl;
    }

    public ProductTree(String value, String label, String parentId) {
        super(value, label, parentId);
    }

}
