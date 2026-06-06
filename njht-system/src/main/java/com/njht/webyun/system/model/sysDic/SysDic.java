package com.njht.webyun.system.model.sysDic;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value="SysDic",description="字典")
public class SysDic {
    @ApiModelProperty(value="表ID")
    private Integer dicId;
    @ApiModelProperty(value="字典名称")
    @NotNull(message = "dicName不能为空")
    private String dicName;
    @NotNull(message = "dicType不能为空")
    @ApiModelProperty(value="字典类型")
    private String dicType;
    @ApiModelProperty(value="修改前的字典类型")
    private String lastDicType;
    @ApiModelProperty(value="字典键")
    private String dicKey;
    @ApiModelProperty(value="字典值")
    private String dicValue;
    @ApiModelProperty(value="象征")
    private String dicSymbol;
    @ApiModelProperty(value="创建人ID")
    private Integer createdBy;
    @ApiModelProperty(value="创建人姓名")
    private String createdName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="创建日期")
    private Date createdDate;
    @ApiModelProperty(value="更改人id")
    private Integer lastUpdatedBy;
    @ApiModelProperty(value="更改人姓名")
    private String lastUpdateName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="更改日期")
    private Date lastUpdatedDate;
    @ApiModelProperty(value="编辑字典值类型（inserted：新增 updated：修改 deleted：删除）")
    private String statu;

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public Integer getDicId() {
        return dicId;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }

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

    public String getLastDicType() {
        return lastDicType;
    }

    public void setLastDicType(String lastDicType) {
        this.lastDicType = lastDicType;
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

    public String getDicSymbol() {
        return dicSymbol;
    }

    public void setDicSymbol(String dicSymbol) {
        this.dicSymbol = dicSymbol;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdateName() {
        return lastUpdateName;
    }

    public void setLastUpdateName(String lastUpdateName) {
        this.lastUpdateName = lastUpdateName;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}