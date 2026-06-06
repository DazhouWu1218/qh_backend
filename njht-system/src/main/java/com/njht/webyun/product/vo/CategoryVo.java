package com.njht.webyun.product.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 15:10
 * @Description: 产品树结构查询参数
 */
@Data
public class CategoryVo {

    @NotNull(message = "id不能为空")
    private Integer roleId;
    private List<CategoryStatusVo> getCategoryStatusList;
}
