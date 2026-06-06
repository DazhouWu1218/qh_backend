package com.njht.webyun.publish.feedback.dto;

import com.njht.webyun.publish.feedback.entity.FeedbackManagementEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/2 14:12
 * @Description: 用户反馈DTO
 */
@Data
public class FeedBackDto extends FeedbackManagementEntity {

    private String userName;
    private String userImgUrl;
    private String toUserName;
    private String toUserImgUrl;
    private String type;
    private List<String> imgUrlList;
}
