package com.njht.webyun.system.model.sysDic;

import com.njht.webyun.system.model.base.BasePageModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：David
 * @date ：Created in 2019/12/9 10:05
 * @description：字典管理查询
 * @modified By：
 * @version: $
 */
public class SysDicValueQuery extends BasePageModel {

    @ApiModelProperty(value="字典名称")
    private String dicName;
    @ApiModelProperty(value="字典类型")
    private String dicType;
    @ApiModelProperty(value="字典键")
    private String dicKey;
    @ApiModelProperty(value="字典值")
    private String dicValue;
    @ApiModelProperty(value="排序字段")
    private String sort;
    @ApiModelProperty(value="排序方式")
    private String order;

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
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
