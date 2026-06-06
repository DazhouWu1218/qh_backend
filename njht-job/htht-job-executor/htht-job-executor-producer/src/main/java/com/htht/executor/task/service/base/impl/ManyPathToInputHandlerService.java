package com.htht.executor.task.service.base.impl;

import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 16:35
 * @Description: 多个输入路径，主要用于处理周期为 年和季 的产品
 */
@Service(value="manyPathToInputHandlerService")
@Slf4j
public class ManyPathToInputHandlerService extends BaseProductHandlerService {

	private final String cycleCoaq = "COAQ";
	private final String cycleCoay = "COAY";

	@Override
	public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
		String issue = triggerParam.getExecutorParams();
		String cycle = productParam.getCycle();
		log.info("---BaseBusinessHandlerService,获取输入文件期次：{},周期：{}",issue,cycle);
		//获取到一个list 循环 去取数据
		List<String> issueList = this.getIssueListByCycle(issue,cycle);
		String inputPath = productParam.getInputFilePath();
		String fileNamePattern = productParam.getFileNamePattern();
		List<String> pathList = new ArrayList<>();
		for (String s:issueList){
			//没有正则输入文件只有路径,有正则输入文件为匹配到的具体文件
			String inputInfo = super.getInputInfo(s, fileNamePattern, inputPath);
			if(inputInfo != null ){
				pathList.add(inputInfo);
			}
		}
		if(pathList.isEmpty()){
			return BaseProductServiceConstant.No_Input_Msg + ":" + inputPath;
		}

		// 返回所有匹配到的日的tif信息
		inputXmlParam.setInputFile(String.join(",", pathList));
        LinkedHashMap<String,Object> dynamicMap = triggerParam.getTaskParam().getDynamicMap();
		String otherFilePath = (String)dynamicMap.get("otherFilePath");
		String otherFileRegex = (String)dynamicMap.get("otherFileRegex");
		String otherFileName= (String)dynamicMap.get("otherFileName");

		if(StringUtils.isNotEmpty(otherFilePath)) {
			List<String> otherPathList = new ArrayList<>();
			for (String s:issueList){
				//没有正则输入文件只有路径,有正则输入文件为匹配到的具体文件
				String otherFilePaths = super.getInputInfo(s,otherFileRegex,otherFilePath);
				if(otherFilePaths != null ){
					otherPathList.add(otherFilePaths);
				}
			}
			if(otherPathList.isEmpty()){
				return BaseProductServiceConstant.No_Input_Msg + ":" + otherFilePath;
			}
            // 返回所有匹配到的日的tif信息
            Map otherMap = new HashMap();
            otherMap.put(otherFileName, String.join(",", otherPathList));
            inputXmlParam.setOtherMap(otherMap);
		}



		return null;
	}

	/**
	 * 根据周期获取期次信息（季匹配3个月。年匹配12个月）
	 * @param issue
	 * @param cycle
	 * @return
	 */
	private List<String> getIssueListByCycle(String issue, String cycle) {
		//返回的期次list
		List<String> issueList = new ArrayList<>();
		String year = issue.substring(0,4);
		if(cycleCoay.equals(cycle)){
			for (int i = 1;i <= 12;i++){
				String rIssue = year;
				if (i < 10){
					rIssue = rIssue +"0"+i+"010000";
				} else {
					rIssue = rIssue + i + "010000";
				}
				issueList.add(rIssue);
			}
		} else if (cycleCoaq.equals(cycle)) {
			String month = issue.substring(4,6);
			this.getCoaqIssueList(issueList,year,month);
		} else {
			issueList.add(issue);
		}
		return issueList;
	}

	/**
	 * 获取季对应要输入的期次
	 * @param issueList
	 * @param year
	 * @param month
	 */
	private void getCoaqIssueList(List<String> issueList, String year, String month) {
		int m = Integer.parseInt(month);
		for (int i=0;i<3;i++) {
			String rIssue = year;
			if(m >= 10 ){
				rIssue = rIssue + m + "010000";
			} else {
				rIssue = rIssue + "0" + m + "010000";
			}
			m--;
			issueList.add(rIssue);
		}
	}

}
