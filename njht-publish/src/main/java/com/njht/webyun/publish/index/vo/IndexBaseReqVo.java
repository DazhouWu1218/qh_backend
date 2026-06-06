package com.njht.webyun.publish.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/11 13:42
 * @Description: 首页标题返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TitleReqVo", description = "首页标题相关信息")
public class IndexBaseReqVo {

    @ApiModelProperty(value = "标题",example = "青海省生态与农牧业气象监测评估预警一体化发布平台")
    private String title;

    @ApiModelProperty(value = "轮播图第一张大标题",example = "青海省生态与农牧业气象监测评估预警一体化发布平台2.0全面上线")
    private String chartTitle;

    @ApiModelProperty(value = "未登录跳转新闻连接")
    private String newsLink;

    @ApiModelProperty(value = "轮播图第一张底部内容",example = "更高效的服务发布,更便捷的产品检索")
    private List<String> chartTextList;

}
