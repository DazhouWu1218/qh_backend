package com.njht.entity.dataPush;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 代国军
 * @description: 任务执行实体
 * @date 2022/9/13 15:22
 */
@Data
public class TaskExecuteEntity extends PushDataCommonEntity {

    private static final long serialVersionUID = 2905796563151906658L;
    @JsonProperty("task_type")
    private String taskType;

    @JsonProperty("exe_success")
    private String exeSuccess;

    @JsonProperty("exe_success_count")
    private Integer exeSuccessCount;

    @JsonProperty("exe_being")
    private String exeBeing;

    @JsonProperty("exe_being_count")
    private Integer exeBeingCount;

    @JsonProperty("exe_failed")
    private String exeFailed;

    @JsonProperty("exe_failed_count")
    private Integer exeFailedCount;

}
