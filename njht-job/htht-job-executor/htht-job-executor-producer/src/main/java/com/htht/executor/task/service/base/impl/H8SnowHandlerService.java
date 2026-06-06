package com.htht.executor.task.service.base.impl;

import com.alibaba.fastjson.JSON;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.shard.SpringContextUtil;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 16:35
 * @Description: 基础插件，输入文件个数为7个
 */
@Service(value="h8SnowHandlerService")
@Slf4j
public class H8SnowHandlerService extends BaseProductHandlerService {

	@Override
	public ReturnT<List<String>> executeShard(TaskParam taskParam) {
		baseBusinessHandlerService = (BaseProductHandlerService) SpringContextUtil.getBeanByType(getClass());
		ReturnT<List<String>> result = new ReturnT<>();
		ProductParam productParam = JSON.parseObject(taskParam.getModelParameters(), ProductParam.class);
		List<String> issues = new ArrayList<>();

		Date beginTime = null;
		Date endTime = null;

		String dateType = productParam.getDateType();
		if("now".equals(dateType)){
			Date now = new Date();
			SimpleDateFormat sdf  = new SimpleDateFormat("HH");
			String hour = sdf.format(now);
			int day = 0;

			String productRangeDay = productParam.getProductRangeDay();
			int i = Integer.parseInt(StringUtils.isEmpty(productRangeDay)?"1":productRangeDay);

			Integer hour1 = Integer.valueOf(hour);
			if(hour1<12){
				day = -1;
				hour1 = 14 ;
			}
			if(hour1 > 14) hour1 = 14;

			for(int j= 0;j<i;j++){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, hour1);
				calendar.set(Calendar.MINUTE, 0);
				calendar.add(Calendar.DAY_OF_YEAR, day);
				String issue = DateUtil.formatDateTime(calendar.getTime(), DateConstant.YYYYMMDDHHMM);
				issues.add(issue);
				if(hour1 ==14 || hour1 == 13) hour1--;
				else{
					hour1 = 14;
					day--;
				}

			}
		}
		if("history".equals(dateType)){
			String[] temp = productParam.getProductRangeDate().split(",");
			beginTime = com.htht.job.core.util.DateUtil.strToDate(temp[0], DateConstant.YYYY_MM_DD_HH_MM);
			endTime = com.htht.job.core.util.DateUtil.strToDate(temp[1], DateConstant.YYYY_MM_DD_HH_MM);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginTime);
			calendar.set(Calendar.MINUTE, 0);
			SimpleDateFormat sdf = new SimpleDateFormat("HH");
			String begin = sdf.format(calendar.getTime());
			Integer hour1 = Integer.valueOf(begin);


			if(hour1<12) hour1 = 12;
			if(hour1 > 14){
				calendar.add(Calendar.DAY_OF_YEAR,1);
				hour1 = 12;
				beginTime = calendar.getTime();
			}

			while(endTime.getTime() > beginTime.getTime()){
				Calendar c1 = Calendar.getInstance();
				c1.setTime(calendar.getTime());
				if(hour1<=14){
					c1.set(Calendar.HOUR_OF_DAY, hour1++);
					String issue = DateUtil.formatDateTime(c1.getTime(), "yyyyMMddHHmm");
					issues.add(issue);
				}else{
					c1.add(Calendar.DAY_OF_YEAR,1);
					hour1 = 12;
					c1.set(Calendar.HOUR_OF_DAY,hour1++);
					beginTime = c1.getTime();
					if(endTime.getTime() < beginTime.getTime()) break;
					String issue = DateUtil.formatDateTime(c1.getTime(), "yyyyMMddHHmm");
					issues.add(issue);
				}
				beginTime = c1.getTime();
			}
		}
		result.setData(issues);
		return result;
	}


	@Override
	public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
		String issue = triggerParam.getExecutorParams();
		log.info("---BaseBusinessHandlerService,获取输入文件信息====={}",issue);
		String inputPath = productParam.getInputFilePath();
		inputXmlParam.setInputFile(FileUtil.getFilePath(inputPath,issue));
		return null;
	}
}
