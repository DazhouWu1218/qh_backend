package com.njht.webyun.system.model.sysOrg;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author David
 * @Time 2019年11月14日 下午5:26:49
 */
public class OrgImportModel implements Serializable {
	private static final long serialVersionUID = -7850071820927526330L;
    
	@ApiModelProperty(value="序号")
    @Excel(name = "序号", orderNum = "0", width = 5)
    private Integer rowNum;
	
	@ApiModelProperty(value="机构编码")
	@Excel(name = "机构编码", orderNum = "1", width = 15)
	private String orgCode;
    
	@ApiModelProperty(value="机构名称")
    @Excel(name = "机构名称", orderNum = "2", width = 15)
    private String orgName;

	@ApiModelProperty(value="父机构编码")
    @Excel(name = "父机构编码", orderNum = "3", width = 15)
    private String parentOrgCode;
    
	@ApiModelProperty(value="地址")
    @Excel(name = "地址", orderNum = "4", width = 25)
    private String address;
    
	@ApiModelProperty(value="联系电话")
    @Excel(name = "联系电话", orderNum = "5", width = 15)
    private String tel;
    
	@ApiModelProperty(value="机构类型（0-网点 1-非网点）")
    @Excel(name = "机构类型（0-网点 1-非网点）", orderNum = "6", width = 15)
    private Integer isNotHall;

    @ApiModelProperty(value="备注")
    @Excel(name = "备注", orderNum = "7", width = 15)
    private String remark;


    /**
	 * 需要一个无参的构造，否则会报错“对象创建错误
	 */
	public OrgImportModel() {
		super();
	}
	public Integer getRowNum() {
		return rowNum;
	}
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getOrgCode() {
        return orgCode;
    }
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    public String getParentOrgCode() {
        return parentOrgCode;
    }
    public void setParentOrgCode(String parentOrgCode) {
        this.parentOrgCode = parentOrgCode;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getIsNotHall() {
        return isNotHall;
    }
    public void setIsNotHall(Integer isNotHall) {
        this.isNotHall = isNotHall;
    }
}
