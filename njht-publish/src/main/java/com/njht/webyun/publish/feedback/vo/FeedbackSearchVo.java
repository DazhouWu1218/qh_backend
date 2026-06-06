package com.njht.webyun.publish.feedback.vo;

import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.utils.DateFormatUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/15 11:18
 * @Description: 用户反馈查询类
 */
@Data
@ApiModel(value = "用户反馈查询类")
public class FeedbackSearchVo extends PageEntity {

    @ApiModelProperty(value = "输入查询信息")
    private String queryInfo;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "回复状态。1-已回复，0-未回复，2-全部,3-我的问题 只针对问题")
    private Integer replyStatus;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = DateFormatUtils.setEndTime(endTime);
    }
    
}
