package com.htht.executor.task.issue;


import com.htht.job.core.entity.paramtemplate.ProductParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 批次日期分片接口
 * @author zedong
 */
public interface IssueTimeShard {

    /**
     * 根据时间范围，获取产品批次
     * @param productParam
     * @return
     */
    default List<String> getIssueListByTimeRange(ProductParam productParam){
        return new ArrayList<>();
    };

}
