package com.njht.webyun.management.satellite.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * 专题数据的查询条件
 * @author zhushizhen
 */
@Data
public class SelectThematicParam implements Serializable {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    /**
     * 当前页
     */
    private Integer pageNum=1;
    /**
     * 每页显示多少条
     */
    private Integer pageSize=5;
    /**
     * 数据类型
     */
    private List<Integer> dataType;
    /**
     * 树结构的id
     */
    private List<String> treeIds;
}
