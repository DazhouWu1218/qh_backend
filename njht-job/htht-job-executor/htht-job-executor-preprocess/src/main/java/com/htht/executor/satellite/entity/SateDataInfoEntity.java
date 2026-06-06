package com.htht.executor.satellite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-19 11:40:19
 */
@Data
@TableName("htht_dms_sate_data_info")
public class SateDataInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date updateTime;
	/**
	 * 乐观锁
	 */
	private String version = "0";
	/**
	 * 数据分类id
	 */
	private String catalogId;
	/**
	 * 产品id
	 */
	private String productId;
	/**
	 * 卫星
	 */
	private String satelliteId;
	/**
	 * 1:陆域卫星,2:海洋卫星,3:高光谱卫星,4:商业卫星
	 */
	private Integer satelliteType;
	/**
	 * 传感器
	 */
	private String sensorId;
	/**
	 * 等级
	 */
	private String level;
	/**
	 * 数据类型
	 */
	private String type;
	/**
	 * 波段数
	 */
	private Integer bands;
	/**
	 * 分辨率
	 */
	private Double resolution;
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 时间
	 */
	private String time;
	/**
	 * 云量
	 */
	private Double cloud;
	/**
	 * 多边形范围
	 */
	private String theGeom;
	/**
	 * 范围面积
	 */
	private Double area;
	/**
	 * 镶嵌数据集文件名
	 */
	private String mosaicFile;
	/**
	 * tif 左上角纬度
	 */
	private Double topleftlatitude;
	/**
	 * tif 左上角经度
	 */
	private Double topleftlongitude;
	/**
	 * tif 右上角纬度
	 */
	private Double toprightlatitude;
	/**
	 * tif 右上角经度
	 */
	private Double toprightlongitude;
	/**
	 * tif 左下角纬度
	 */
	private Double bottomleftlatitude;
	/**
	 * tif 左下角经度
	 */
	private Double bottomleftlongitude;
	/**
	 * tif 右下角纬度
	 */
	private Double bottomrightlatitude;
	/**
	 * tif 右下角经度
	 */
	private Double bottomrightlongitude;
	/**
	 * tif 中心纬度
	 */
	private Double centerlatitude;
	/**
	 * tif 中心经度
	 */
	private Double centerlongitude;
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
	/**
	 * 所查询的数据类型： 0：预处理数据  1：原始数据 
	 */
	private Integer dataType;

}
