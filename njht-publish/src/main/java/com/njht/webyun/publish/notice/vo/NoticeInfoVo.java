package com.njht.webyun.publish.notice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/12 9:12
 * @Description: 通知公告返回类
 */
@Data
@ApiModel(value = "通知公告保存结果")
public class NoticeInfoVo {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "详情")
    private String content;

    @ApiModelProperty(value = "图片相对路径")
    private String imgUrl;

    @ApiModelProperty(value = "是否星标")
    private Integer isStar;

    @ApiModelProperty(value = "作者")
    private String userName;
}
