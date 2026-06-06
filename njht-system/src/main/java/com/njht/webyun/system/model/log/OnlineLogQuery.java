package com.njht.webyun.system.model.log;


import com.njht.webyun.system.model.base.BasePageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实时在线用户查询条件
 * @author : David
 * @date : 17:01 2019-12-9
 */
@ApiModel(value="OnlineLogQuery",description="分页查询实时在线用户")
public class OnlineLogQuery extends BasePageModel {
    @ApiModelProperty(value="排序字段",required=false)
    private String sort;
    @ApiModelProperty(value="排序方式",required=false)
    private String order;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
