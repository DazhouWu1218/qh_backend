package com.njht.webyun.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 树结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Tree",description = "树结构")
public class Tree extends CommonEntity{

    /** 父id*/
    @ApiModelProperty(value = "父Id")
    private String parentId;

    @ApiModelProperty(value = "节点类型")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String type;

    /** 子产品目录*/
    @ApiModelProperty(value = "子集")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<? extends Tree> children;

    public Tree(String value, String label, String parentId) {
        super(value, label);
        this.parentId = parentId;
    }
}
