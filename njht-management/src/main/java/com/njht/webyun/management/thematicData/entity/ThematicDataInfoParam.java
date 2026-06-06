package com.njht.webyun.management.thematicData.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/4/19 16:25
 * @Description: java类作用描述
 */
public class ThematicDataInfoParam {

    @NotNull(message = "id不能为空")
    @Length(min = 1,message = "id不能为空")
    private String id;

    @Size(min = 1,message = "至少选一个周期")
    @NotNull
    private List<String> cycleList;

    @Size(min = 1,message = "至少选一个数据源")
    @NotNull
    private List<String> dataSourceList;

    @Size(min = 1,message = "至少选一个数据类型")
    @NotNull
    private List<String> dataTypeList;

    public ThematicDataInfoParam() {
    }

    public String getId() {
        return id;
    }

    public ThematicDataInfoParam setId(String id) {
        this.id = id;
        return this;
    }

    public List<String> getCycleList() {
        return cycleList;
    }

    public ThematicDataInfoParam setCycleList(List<String> cycleList) {
        this.cycleList = cycleList;
        return this;
    }

    public List<String> getDataSourceList() {
        return dataSourceList;
    }

    public ThematicDataInfoParam setDataSourceList(List<String> dataSourceList) {
        this.dataSourceList = dataSourceList;
        return this;
    }

    public List<String> getDataTypeList() {
        return dataTypeList;
    }

    public ThematicDataInfoParam setDataTypeList(List<String> dataTypeList) {
        this.dataTypeList = dataTypeList;
        return this;
    }
}
