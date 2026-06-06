package com.htht.executor.task.param;

import com.htht.job.core.util.GsonTool;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author daiguojun
 * @date 2022-08-25 17:55
 * 1
 */
@Data
public class H8PreParam {

    /**
     * 标识
     */
    private String identify;

    // 可执行文件路径
    private String exePath;
    // 结果存储位置
    private String outFolder;
    // 输入文件路径
    private String inputPath;
    // 文件正则
    private String fileNamePattern;
    // xml存放路径
    private String xmlPath;
    // 产品周期
    private String cycle;
    // now 实时数据 history历史数据
    private String dateType;
    // 历史数据时间范围
    private String timeRange;
    // 实时数据，天数
    private String rangeDay;


    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        if (StringUtils.isEmpty(timeRange)) {
            this.timeRange = timeRange;
        } else {
            List<String> list = GsonTool.fromJsonList(timeRange, String.class);
            this.timeRange = String.join(",", list);
        }
    }
}
