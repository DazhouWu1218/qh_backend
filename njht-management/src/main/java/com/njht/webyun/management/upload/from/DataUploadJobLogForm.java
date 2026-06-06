package com.njht.webyun.management.upload.from;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 获取手动日志的请求参数
 * @author zhushizhen
 */
@Data
public class DataUploadJobLogForm {
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;

    /**
     * 数据分类
     */
    private List<String> dataTypeList;
    /**
     * 用户姓名
     */
    private List<String> userIdList;
    /**
     * 任务状态
     */
    private List<String> statusList;

}
