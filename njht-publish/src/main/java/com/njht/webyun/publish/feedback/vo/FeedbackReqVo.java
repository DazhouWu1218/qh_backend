package com.njht.webyun.publish.feedback.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/15 9:43
 * @Description: 用户反馈返回类
 */
@Data
@ApiModel(value = "用户反馈类型")
public class FeedbackReqVo {

    /**
     * 问题反馈的id，主键
     */
    @ApiModelProperty(value = "问题反馈id")
    private Integer feedbackId;
    /**
     * 问题分类
     */
    @ApiModelProperty(value = "问题分类")
    private String feedbackType;

    /**
     * 问题内容
     */
    @ApiModelProperty(value = "问题内容")
    private String feedbackContent;

    @ApiModelProperty(value = "图片集合")
    private List<String> imgUrlList;

    /**
     * 父节点id
     */
    @ApiModelProperty(value = "父节点id")
    private Integer parentsId;

    @ApiModelProperty(value = "回复状态。1-已回复，0-未回复。只针对问题，回复不需要该状态")
    private Integer replyStatus;

    @ApiModelProperty(value = "问题恢复条数，恢复消息该字段为空")
    private Integer replyNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    @ApiModelProperty(value = "创建者")
    private String userName;

    private Integer userId;
    private String userImgUrl;


    @ApiModelProperty(value = "回复给谁")
    private String toUserName;

    private Integer toUserId;
    private String toUserImgUrl;
}
