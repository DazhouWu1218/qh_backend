package com.njht.webyun.system.model.sysUrl;

import com.njht.webyun.system.model.base.BasePageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="SysUrlQuery",description="分页查询URL")
public class SysUrlQuery extends BasePageModel {

    @ApiModelProperty(value = "URL名称", required = true)
    private String urlName;
    @ApiModelProperty(value = "排序字段", required = false)
    private String sort;
    @ApiModelProperty(value = "排序方式", required = false)
    private String order;
    @ApiModelProperty(value = "排除urlId", required = true)
    private String excepted;

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

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

    public String getExcepted() {
        return excepted;
    }

    public void setExcepted(String excepted) {
        this.excepted = excepted;
    }
}
