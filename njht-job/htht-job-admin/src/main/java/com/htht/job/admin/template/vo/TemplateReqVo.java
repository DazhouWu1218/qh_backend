package com.htht.job.admin.template.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author 代国军
 * @description: 模板返回结果
 * @date 2022/6/29 10:19
 */
@Data
public class TemplateReqVo {

    private String id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private List<TemplateParamReqVo> paramList;
}
