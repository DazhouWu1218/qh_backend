package com.njht.webyun.management.satellite.vo;


import lombok.Data;

import java.io.Serializable;
import java.sql.Time;

/**
 * @author miaoyu
 * @date 2020-05-25 03:19:51
 */
@Data
public class HthtDmsSateDataInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String id;
    private String satelliteStatus;
    private String dataType;
    /**
     *
     */
    private String satelliteId;
    /**
     *
     */
    private String sensorId;
    /**
     *
     */
    private java.sql.Date date;
    /**
     *
     */
    private Time time;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String resolution;
    /**
     *
     */
    private String type;
    /**
     *
     */
    private Integer bands;
    /**
     *
     */
    private Double cloud;
    /**
     *
     */
    private Double topleftlatitude;
    /**
     *
     */
    private Double topleftlongitude;
    /**
     *
     */
    private Double toprightlatitude;
    /**
     *
     */
    private Double toprightlongitude;
    /**
     *
     */
    private Double bottomleftlatitude;
    /**
     *
     */
    private Double bottomleftlongitude;
    /**
     *
     */
    private Double bottomrightlatitude;
    /**
     *
     */
    private Double bottomrightlongitude;
    /**
     *
     */
    private Double centerlongitude;
    /**
     *
     */
    private Double centerlatitude;
    /**
     *
     */

    private Double pngtopleftlatitude;
    /**
     *
     */
    private Double pngtopleftlongitude;
    /**
     *
     */
    private Double pngtoprightlatitude;
    /**
     *
     */
    private Double pngtoprightlongitude;
    /**
     *
     */
    private Double pngbottomleftlatitude;
    /**
     *
     */
    private Double pngbottomleftlongitude;
    /**
     *
     */
    private Double pngbottomrightlatitude;
    /**
     *
     */
    private Double pngbottomrightlongitude;

    private String level;

    private String pngId;

    private String smallPngId;

    private String pngurl;
    private String smallPngurl;

    private String the_geom;

    private String productId;


}
