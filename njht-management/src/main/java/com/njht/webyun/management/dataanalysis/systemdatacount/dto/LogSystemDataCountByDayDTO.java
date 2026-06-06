package com.njht.webyun.management.dataanalysis.systemdatacount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统各类数据按天统计
 *
 * @author zhouhouliang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogSystemDataCountByDayDTO implements Serializable {

    private String date = "0";

    private String count = "0";

}
