package com.njht.entity.dataPush;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 代国军
 * @description: 数据统计实体
 * @date 2022/9/13 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleTaskExeEntity  {
    private static final long serialVersionUID = 1L;
    @JsonProperty("task_type")
    private String taskType;

    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("task_name")
    private String taskName;

    @JsonProperty("task_result")
    private String taskResult;

    @JsonProperty("failure_reason")
    private String failureReason;

    @JsonProperty("record_time")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date recordTime;


    public SingleTaskExeEntity(String taskType, String taskId, String taskName, String taskResult, String failureReason) {
        this.taskType = taskType;
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskResult = taskResult;
        this.failureReason = failureReason;
        this.recordTime = new Date();
    }
}
