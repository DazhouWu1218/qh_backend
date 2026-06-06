package com.njht.webyun.management.business.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * @author dgj
 */
@Component
@Data
@ApiModel("产品查询参数")
public class ProductParam {

    private String beginTime;

    private String  endTime;

    /**产品分类 */
    @Size(min = 1,message = "分类id不能为空")
    @NotNull(message = "id不能为空")
    private List<String> feiLei;

    private List<String> name;

    /**产品周期 */
    private List<String> cycle;

    /**数据源 */
    private List<String> dataSource;

    /**产品类型 */
    private List<String> type;

    /**所属区域*/
    private String regionId;

    /**地形图*/
    private Map geo;

    private List<String> unit;


    private Integer pageNum;

    private Integer pageSize;


}
