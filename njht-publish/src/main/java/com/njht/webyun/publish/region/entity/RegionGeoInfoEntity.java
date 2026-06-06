package com.njht.webyun.publish.region.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/24 17:58
 * @Description: 行政区域边界信息
 */
@Data
@TableName("htht_region_geo_info")
public class RegionGeoInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String regionId;
    private String theGemo;
}
