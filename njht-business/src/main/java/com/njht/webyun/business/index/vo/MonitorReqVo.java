package com.njht.webyun.business.index.vo;

import com.njht.webyun.business.index.dto.TaskParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/1/7 10:42
 * @Description: 节点监控信息
 */
@ApiModel(value = "监控信息返回类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorReqVo {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "key")
    private String value;

    @ApiModelProperty(value = "总数")
    private Long sumNum;

    @ApiModelProperty(value = "分类信息")
    private List<TaskParam> taskList;
}
