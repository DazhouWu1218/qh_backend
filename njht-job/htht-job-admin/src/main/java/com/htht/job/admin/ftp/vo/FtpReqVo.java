package com.htht.job.admin.ftp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 代国军
 * @description: ftp 返回结果
 * @date 2022/6/27 17:34
 */
@Data
@ApiModel
public class FtpReqVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "端口号")
    private Integer port;

    @ApiModelProperty(value = "用户名")
    private String userName;
}
