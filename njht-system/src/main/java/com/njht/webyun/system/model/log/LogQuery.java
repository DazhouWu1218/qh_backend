package com.njht.webyun.system.model.log;


import com.njht.webyun.system.model.base.BasePageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author : David
 * @date : 10:03 2019/12/4
 */
@ApiModel(value = "LogQuery", description = "分页查询登录日志")
public class LogQuery extends BasePageModel {
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    @ApiModelProperty(value = "机构ID", required = true)
    private int orgId;
    @ApiModelProperty(value = "是否包含子机构", required = true)
    private boolean containSub;
    @ApiModelProperty(value = "数据是否改变", required = true)
    private boolean dataChanged;
    @ApiModelProperty(value = "登录起始日期", required = true)
    private String beginDate;
    @ApiModelProperty(value = "登录中止日期", required = true)
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDate;
    @ApiModelProperty(value = "排序字段", required = false)
    private String sort;
    @ApiModelProperty(value = "排序方式", required = false)
    private String order;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public boolean isContainSub() {
        return containSub;
    }

    public void setContainSub(boolean containSub) {
        this.containSub = containSub;
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
}
