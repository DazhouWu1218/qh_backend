package com.htht.job.admin.dispatch.vo;

import com.htht.job.admin.template.vo.TemplateParamReqVo;
import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * @author 代国军
 * @description: 任务返回参数
 * @date 2022/6/21 16:01
 */
@Data
@ApiModel
public class JobReqVo extends JobInfoReqVo{

    @ApiModelProperty(value = "算法id")
    private String algorithmId;

    @ApiModelProperty(value = "算法每一级id集合,按照 一级 二级 三级排序")
    private List<String> algorithmIdList;

    @ApiModelProperty(value = "插件id")
    @NotEmpty(message = "插件id不能为空",groups = {SaveGroup.class})
    private String handlerId;

    @ApiModelProperty(value = "报警邮件")
    private String alarmEmail;

    @ApiModelProperty(value = "调度配置，值含义取决于调度类型")
    private String scheduleConf;
    @ApiModelProperty(value = "调度过期策略")
    @NotEmpty(message = "调度过期策略不能为空",groups = {SaveGroup.class})
    private String misfireStrategy;
    @ApiModelProperty(value = "执行器路由策略")
    @NotEmpty(message = "执行器路由策略不能为空",groups = {SaveGroup.class})
    private String executorRouteStrategy;
    @ApiModelProperty(value = "执行器，任务Handler名称")
    private String executorHandler;

    @ApiModelProperty(value = "阻塞处理策略")
    @NotEmpty(message = "阻塞处理策略不能为空",groups = {SaveGroup.class})
    private String executorBlockStrategy;
    @ApiModelProperty(value = "任务执行超时时间，单位秒")
    private int executorTimeout;
    @ApiModelProperty(value = "失败重试次数")
    private int executorFailRetryCount;
    @ApiModelProperty(value = "子任务id,多个用逗号分隔开")
    private String childJobId;

    @ApiModelProperty(value = "运行模式")
    @NotEmpty(message = "新增运行模式不能为空",groups = {SaveGroup.class})
    @Null(message = "运行模式不能被修改",groups = {UpdateGroup.class})
    private String glueType;

    @ApiModelProperty(value = "动态输入参数")
    private List<TemplateParamReqVo> dynamicList;

    @ApiModelProperty(value = "算法模板参数")
    private List<TemplateParamReqVo> fixedList;

    @ApiModelProperty(value = "调度模板参数")
    private List<TemplateParamReqVo> modelParameterList;



}
