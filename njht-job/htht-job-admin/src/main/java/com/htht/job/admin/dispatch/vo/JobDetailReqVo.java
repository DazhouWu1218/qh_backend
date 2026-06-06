package com.htht.job.admin.dispatch.vo;

import com.njht.webyun.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 代国军
 * @description: 任务详情页 路由策略等集合信息
 * @date 2022/6/1 17:32
 */
@Data
@ApiModel(value = "任务详情页，下拉框集合")
public class JobDetailReqVo {

    /**
     * 路由策略
     */
    @ApiModelProperty(value = "路由策略")
    private List<CommonEntity> executorRouteStrategyList;

    @ApiModelProperty(value = "运行模式")
    private List<CommonEntity> glueTypeList;

    @ApiModelProperty(value = "调度类型,固定速度 单位秒.corn需要集成corn表达式")
    private List<CommonEntity> scheduleTypeList;

    @ApiModelProperty(value = "阻塞处理策略")
    private List<CommonEntity> executorBlockStrategyList;

    @ApiModelProperty(value = "调度过期策略")
    private List<CommonEntity> misfireStrategyList;
}
