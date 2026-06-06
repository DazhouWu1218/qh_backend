package com.htht.executor.test;


import com.alibaba.fastjson.JSON;
import com.htht.executor.satellite.service.SateDataTaskFileService;
import com.htht.executor.sys.service.DicService;
import com.htht.job.core.entity.paramtemplate.PreDataParam;
import com.htht.job.core.exception.CommonException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class XxlJobExecutorExampleBootApplicationTests {

	@Autowired
	private SateDataTaskFileService sateDataTaskFileService;


	@Test
	public void test() {
		String s = "{\"preDataTaskName\":\"FY3D_MERSI_1000\",\"projectionExeLocaiton\":\"C:\\\\htht\\\\algorithm\\\\SmartPreprocessV5.01\\\\Release64\\\\PIE.Meteo.ProjectTool.exe\",\"outputDataFilePath\":\"Y:\\\\htht\\\\projection\",\"validEnvelopes\":\"name:中国及周边,minx:60,maxx:150,miny:10,maxy:70\",\"formate\":\"TIFF\",\"envelopes\":\"\",\"bands\":\"1,2,3,4,5,6,7\",\"projectionIdentify\":\"GLL\",\"resolutionX\":\"0.01\",\"resolutionY\":\"0.01\",\"dateType\":\"2\",\"projectionInputArgXml\":\"Y:\\\\EAMIS\\\\xml\\\\satellite\",\"projectioDate\":\"2019-01-01 - 2020-12-31\",\"rangeDay\":\"12\",\"extArgs\":\"SolarZenith,SolarAzimuth,SensorZenith,SensorAzimuth\",\"inputDataFilePath\":\"Y:\\\\FY3D\\\\mersi\\\\{yyyy}\\\\{MM}\",\"fileNamePattern\":\"^FY3D_X_yyyy_MM_.*_A_G_MERSI_1000M_L1B.HDF$\",\"fileDatePattern\":\"(\\\\d{4})_(\\\\d{2})_(\\\\d{2})_(\\\\d{2})_(\\\\d{2})\",\"scandisk\":\"606cb1f3c2a749fb950fb78fc59c8ffa\"}";
		PreDataParam preDataParam = new PreDataParam();
		try {
			//获取到固定参数
			preDataParam = JSON.parseObject(s, PreDataParam.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException("预处理参数获取失败,"+e.getMessage());
		}
		List<String> strings = sateDataTaskFileService.selectFileNameBySatellite(preDataParam);
		System.out.println(11);
	}

	@Autowired
	private DicService dicService;

	@Test
	public void test1() {
		String sateUrl = dicService.findSateUrl();
		System.out.println(sateUrl);
	}

}