package com.njht.webyun.management.thematicData.entity;


import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.business.entity.UnitInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/4/19 15:06
 * @Description: java类作用描述
 */
@Data
public class TreeDto extends Tree {

    /** 周期 */
    private List<ValueType>  cycle;

    /** 数据类型 */
    private  List<ValueType>  dataType;

    /** 数据源 */
    private  List<ValueType>  dataSource;

    private UnitInfo unitInfo;

    private Boolean isShp;
}
