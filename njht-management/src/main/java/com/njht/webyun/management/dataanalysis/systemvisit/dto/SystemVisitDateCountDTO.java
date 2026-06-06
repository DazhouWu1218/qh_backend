package com.njht.webyun.management.dataanalysis.systemvisit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemVisitDateCountDTO implements Serializable {

    private String date;

    private String count = "0";

}
