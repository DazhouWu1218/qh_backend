package com.njht.webyun.management.dataanalysis.systemdatacount.vo;

import com.njht.webyun.management.dataanalysis.systemdatacount.dto.LogSystemDataCountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogSystemDataByDataTypeVO implements Serializable {

    private String dataType;

    private List<LogSystemDataCountDTO> logSystemDataCountDTOS;

}
