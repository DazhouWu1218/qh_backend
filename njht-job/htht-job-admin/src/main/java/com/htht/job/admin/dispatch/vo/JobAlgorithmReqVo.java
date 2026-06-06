package com.htht.job.admin.dispatch.vo;

import com.htht.job.admin.plugin.vo.PluginTaskReqVo;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 代国军
 * @description: 任务详情 执行器，插件，算法集合
 * @date 2022/6/1 17:40
 */
@Data
@ApiModel(value = "任务详情 执行器，插件，算法集合")
public class JobAlgorithmReqVo {

    @ApiModelProperty(value = "执行器列表")
    private List<CommonEntity> jobGroupList;

    @ApiModelProperty(value = "插件集合")
    private List<PluginTaskReqVo> handlerList;

    @ApiModelProperty(value = "算法集合")
    private List<Tree> algorithmList;
}
