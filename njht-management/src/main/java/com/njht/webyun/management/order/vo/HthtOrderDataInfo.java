package com.njht.webyun.management.order.vo;


import java.io.Serializable;
import java.sql.Time;

/**
 * @author lmd
 */
public class HthtOrderDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    /**
     *
     */
    private Integer bands;
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
    private Double centerlatitude;
    /**
     *
     */
    private Double centerlongitude;
    /**
     *
     */
    private Double cloud;
    /**
     *
     */
    private java.sql.Date date;
    private long filessize;
    /**
     *
     */
    private String id;
    private String level;
    /**
     *
     */
    private String name;
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
    private String pngId;
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
    private String pngurl;
    /**
     *
     */
    private Double resolution;
    /**
     *
     */
    private String satelliteId;
    /**
     *
     */
    private String sensorId;
    private String smallPngId;

    private String smallPngurl;

    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     *
     */
    private Time time;

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
    private String type;

	public Integer getBands() {
        return bands;
    }

	public Double getBottomleftlatitude() {
        return bottomleftlatitude;
    }
    
    public Double getBottomleftlongitude() {
        return bottomleftlongitude;
    }

    public Double getBottomrightlatitude() {
        return bottomrightlatitude;
    }

    public Double getBottomrightlongitude() {
        return bottomrightlongitude;
    }

    public Double getCenterlatitude() {
        return centerlatitude;
    }

    public Double getCenterlongitude() {
        return centerlongitude;
    }

    public Double getCloud() {
        return cloud;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public long getFilessize() {
        return filessize;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public Double getPngbottomleftlatitude() {
        return pngbottomleftlatitude;
    }

    public Double getPngbottomleftlongitude() {
        return pngbottomleftlongitude;
    }

    public Double getPngbottomrightlatitude() {
        return pngbottomrightlatitude;
    }

    public Double getPngbottomrightlongitude() {
        return pngbottomrightlongitude;
    }

    public String getPngId() {
        return pngId;
    }

    public Double getPngtopleftlatitude() {
        return pngtopleftlatitude;
    }

    public Double getPngtopleftlongitude() {
        return pngtopleftlongitude;
    }

    public Double getPngtoprightlatitude() {
        return pngtoprightlatitude;
    }

    public Double getPngtoprightlongitude() {
        return pngtoprightlongitude;
    }

    public String getPngurl() {
		return pngurl;
	}

    public Double getResolution() {
        return resolution;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getSmallPngId() {
        return smallPngId;
    }

    public String getSmallPngurl() {
		return smallPngurl;
	}

    public Time getTime() {
        return time;
    }

    public Double getTopleftlatitude() {
        return topleftlatitude;
    }

    public Double getTopleftlongitude() {
        return topleftlongitude;
    }

    public Double getToprightlatitude() {
        return toprightlatitude;
    }

    public Double getToprightlongitude() {
        return toprightlongitude;
    }

    public String getType() {
        return type;
    }

    public void setBands(Integer bands) {
        this.bands = bands;
    }

    public void setBottomleftlatitude(Double bottomleftlatitude) {
        this.bottomleftlatitude = bottomleftlatitude;
    }

    public void setBottomleftlongitude(Double bottomleftlongitude) {
        this.bottomleftlongitude = bottomleftlongitude;
    }

    public void setBottomrightlatitude(Double bottomrightlatitude) {
        this.bottomrightlatitude = bottomrightlatitude;
    }

    public void setBottomrightlongitude(Double bottomrightlongitude) {
        this.bottomrightlongitude = bottomrightlongitude;
    }

    public void setCenterlatitude(Double centerlatitude) {
        this.centerlatitude = centerlatitude;
    }

    public void setCenterlongitude(Double centerlongitude) {
        this.centerlongitude = centerlongitude;
    }

    public void setCloud(Double cloud) {
        this.cloud = cloud;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public void setFilessize(long filessize) {
        this.filessize = filessize;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPngbottomleftlatitude(Double pngbottomleftlatitude) {
        this.pngbottomleftlatitude = pngbottomleftlatitude;
    }

    public void setPngbottomleftlongitude(Double pngbottomleftlongitude) {
        this.pngbottomleftlongitude = pngbottomleftlongitude;
    }

    public void setPngbottomrightlatitude(Double pngbottomrightlatitude) {
        this.pngbottomrightlatitude = pngbottomrightlatitude;
    }

    public void setPngbottomrightlongitude(Double pngbottomrightlongitude) {
        this.pngbottomrightlongitude = pngbottomrightlongitude;
    }

    public void setPngId(String pngId) {
        this.pngId = pngId;
    }

    public void setPngtopleftlatitude(Double pngtopleftlatitude) {
        this.pngtopleftlatitude = pngtopleftlatitude;
    }

    public void setPngtopleftlongitude(Double pngtopleftlongitude) {
        this.pngtopleftlongitude = pngtopleftlongitude;
    }

    public void setPngtoprightlatitude(Double pngtoprightlatitude) {
        this.pngtoprightlatitude = pngtoprightlatitude;
    }

    public void setPngtoprightlongitude(Double pngtoprightlongitude) {
        this.pngtoprightlongitude = pngtoprightlongitude;
    }

    public void setPngurl(String pngurl) {
		this.pngurl = pngurl;
	}

    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }

    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public void setSmallPngId(String smallPngId) {
        this.smallPngId = smallPngId;
    }

    public void setSmallPngurl(String smallPngurl) {
		this.smallPngurl = smallPngurl;
	}

    public void setTime(Time time) {
        this.time = time;
    }

    public void setTopleftlatitude(Double topleftlatitude) {
        this.topleftlatitude = topleftlatitude;
    }

    public void setTopleftlongitude(Double topleftlongitude) {
        this.topleftlongitude = topleftlongitude;
    }

    public void setToprightlatitude(Double toprightlatitude) {
        this.toprightlatitude = toprightlatitude;
    }

    public void setToprightlongitude(Double toprightlongitude) {
        this.toprightlongitude = toprightlongitude;
    }

    public void setType(String type) {
        this.type = type;
    }
}
