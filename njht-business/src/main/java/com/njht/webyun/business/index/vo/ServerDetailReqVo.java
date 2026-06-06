package com.njht.webyun.business.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/11 9:39
 * @Description: 服务器状态监控返回信息
 */
@Data
@ApiModel(value = "服务器详情列表")
@AllArgsConstructor
@NoArgsConstructor
public class ServerDetailReqVo extends ServerReqVo {


    @ApiModelProperty("当前节点算法详情")
    private List<String> algorithmList;

}
