package com.njht.webyun.business.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/11 9:39
 * @Description: 服务器状态监控返回信息
 */
@Data
@ApiModel(value = "服务器列表")
@AllArgsConstructor
@NoArgsConstructor
public class ServerReqVo {

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "是否运行 0未运行 1运行")
    private Long isRun;

    /**
     * 物理内存
     */
    @ApiModelProperty(value = "总内存")
    private String totalPhysicalMemorySize;
    /**
     * 已用
     */
    @ApiModelProperty(value = "已用内存")
    private String usePhysicalMemorySize;

    @ApiModelProperty(value = "内存使用率")
    private String memoryLoad;
    /**
     * 当前系统cpu 使用率
     */
    @ApiModelProperty(value = "cpu 使用率")
    private String systemCpuLoad;

}
