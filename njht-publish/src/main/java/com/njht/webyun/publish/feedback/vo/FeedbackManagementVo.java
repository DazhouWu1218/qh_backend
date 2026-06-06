package com.njht.webyun.publish.feedback.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/12 17:10
 * @Description: 用户反馈信息
 */
@Data
@ApiModel(value = "用户反馈信息")
public class FeedbackManagementVo {

    /**
     * 问题分类
     */
    @ApiModelProperty(value = "问题分类")
    private Integer feedbackType;
    /**
     * 问题内容
     */
    @ApiModelProperty(value = "问题内容")
    private String feedbackContent;
    /**
     * 是否是原始问题1-是，0-不是  1原始问题， 0 回复
     */
    @ApiModelProperty(value = "是否是原始问题1-是，0-不是  1原始问题， 0 回复")
    private Integer question;
    /**
     * 父节点id
     */
    @ApiModelProperty(value = "提问0 回复为回复问题的id")
    private Integer parentsId;
    /**
     * 是否公开，1-公开，0-不公开 （不公开只有管理员可见）
     */
    @ApiModelProperty(value = "是否公开，1-公开，0-不公开 （不公开只有管理员可见）")
    private Integer publicStatus;

}
