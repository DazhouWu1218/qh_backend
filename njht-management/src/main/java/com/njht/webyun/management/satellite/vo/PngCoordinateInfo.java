package com.njht.webyun.management.satellite.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 地图贴图四角坐标信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PngCoordinateInfo {

    private Double pngbottomleftlongitude;
    private Double pngbottomleftlatitude;
    private Double pngtoprightlongitude;
    private Double pngtoprightlatitude;
}
