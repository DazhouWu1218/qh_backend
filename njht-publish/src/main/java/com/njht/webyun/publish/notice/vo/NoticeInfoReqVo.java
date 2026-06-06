package com.njht.webyun.publish.notice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/12 9:12
 * @Description: 通知公告返回类
 */
@Data
@ApiModel(value = "通知公告返回结果")
public class NoticeInfoReqVo {


    @ApiModelProperty(value = "id")
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "详情")
    private String content;

    @ApiModelProperty(value = "图片相对路径")
    private String imgUrl;

    @ApiModelProperty(value = "是否星标")
    private Integer isStar;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "是否已读 0已读，1未读")
    private Integer isRead;

    @ApiModelProperty(value = "是否置顶")
    private Integer isTop;
}
