package com.njht.webyun.publish.behavior.vo;

import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.utils.DateFormatUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/15 11:18
 * @Description: 行为日志信息类
 */
@Data
@ApiModel(value = "行为日志信息查询类")
public class StatisticSearchVo extends PageEntity {

    @ApiModelProperty(value = "权限id")
    private Integer roleId;

    @ApiModelProperty(value = "组织机构id")
    @NotNull(message = "组织机构id不能为空")
    private Integer orgId;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "行为 0-访问，1-下载，2-更改用户信息，3-全部")
    private Integer action;

    @ApiModelProperty(value = "请输入查询信息")
    private String search;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = DateFormatUtils.setEndTime(endTime);
    }
}
