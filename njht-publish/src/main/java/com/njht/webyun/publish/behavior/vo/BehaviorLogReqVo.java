package com.njht.webyun.publish.behavior.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/15 14:57
 * @Description: 信息统计返回类
 */
@Data
@ApiModel(value = "信息统计返回类")
public class BehaviorLogReqVo {

    /**
     * 行为日志id
     */
    @ApiModelProperty(value = "id")
    private Integer behaviorId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 区域名称
     */
    @ApiModelProperty(value = "用户来源")
    private String userOrg;

    @ApiModelProperty(value = "联系方式")
    private String tel;

    @ApiModelProperty(value = "权限")
    private String roleName;

    /**
     * 日志时间
     */
    @ApiModelProperty(value = "日志时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String time;

    /**
     * 动作行为
     */
    @ApiModelProperty(value = "行为 0-访问，1-下载，2-更改用户信息")
    private Integer action;

    @ApiModelProperty(value = "用户下载信息")
    private List<BehaviorLogDownLoadReqVo> downLoadReqVoList;

}
