package com.njht.webyun.management.business.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * @Author: 代国军
 * @CreateDate: 2021/7/1 8:54
 * @Description: java类作用描述
 */
@Data
public class BusinessParam {

    @NotNull(message = "开始时间不能为空")
    private String beginTime;
    @NotNull(message = "结束时间不能为空")
    private String  endTime;
    @NotNull
    @Size(min = 1,message = "产品标识不能为空")
    private List<String> mark;

    /**产品周期 */
    private List<String> cycle;

    /**数据源 */
    private List<String> dataSource;

    /**所属区域*/
    private String regionId;

    /**地形图*/
    private Map geo;

    private Integer pageNum;

    private Integer pageSize;
}
