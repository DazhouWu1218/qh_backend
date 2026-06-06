package com.htht.job.core.entity.paramtemplate;

import com.htht.job.core.util.GsonTool;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

public class ProductParam {
	// 可执行文件路径
	private String exePath;
	// 算法源码
	private String logPath;
	// xml存放路径
	private String xmlPath;
	// 产品算法名称
	private String areaID;
	// 产品周期
	private String cycle;
	// 产品临时目录
	private String tempPath;
	// now 实时数据 history历史数据
	private String dateType;
	// 历史数据时间范围
	private String productRangeDate;
	// 实时数据，天数
	private String productRangeDay;
	// file 扫描文件， sql 查询数据库
	private String inputType = "file";
	// 扫描目录
	private String inputFilePath;
	// 文件正则
	private String fileNamePattern;
	// 查询数据库，sql
	private String inputSql;
	// 任务备注
	private String bz;

	// 卫星
	private String satellite;
	// 传感器
	private String sensor;
	// 分辨率
	private String resolution;

	public String getSatellite() {
		return satellite;
	}

	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getProductRangeDate() {
		return productRangeDate;
	}

	public void setProductRangeDate(String productRangeDate) {
		if (StringUtils.isEmpty(productRangeDate)) {
			this.productRangeDate = productRangeDate;
		} else {
			List<String> list = GsonTool.fromJsonList(productRangeDate, String.class);
			this.productRangeDate = String.join(",", list);
		}
	}

	public String getExePath() {
		return exePath;
	}

	public void setExePath(String exePath) {
		this.exePath = exePath;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public String getAreaID() {
		return areaID;
	}

	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getProductRangeDay() {
		return productRangeDay;
	}

	public void setProductRangeDay(String productRangeDay) {
		this.productRangeDay = productRangeDay;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public String getInputSql() {
		return inputSql;
	}

	public void setInputSql(String inputSql) {
		this.inputSql = inputSql;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
