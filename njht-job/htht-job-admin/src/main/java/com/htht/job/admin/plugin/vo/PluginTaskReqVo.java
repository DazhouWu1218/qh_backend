package com.htht.job.admin.plugin.vo;

import com.njht.webyun.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 代国军
 * @description: 任务面板相关插件参数
 * @date 2022/6/16 15:05
 */
@Data
@NoArgsConstructor
public class PluginTaskReqVo extends CommonEntity {

    @ApiModelProperty(value = "插件标识,任务调度时传参")
    private String modelIdentify;

    public PluginTaskReqVo(String value, String label, String modelIdentify) {
        super(value, label);
        this.modelIdentify = modelIdentify;
    }

    public PluginTaskReqVo(String modelIdentify) {
        this.modelIdentify = modelIdentify;
    }
}
