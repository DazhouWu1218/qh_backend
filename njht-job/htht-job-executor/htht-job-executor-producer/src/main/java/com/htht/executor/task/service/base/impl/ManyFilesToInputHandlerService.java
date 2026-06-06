package com.htht.executor.task.service.base.impl;

import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 16:35
 * @Description: 周期为旬，且输入文件为多个,需要在多个期次去拿多个输入文件
 */
@Service(value="manyFilesToInputHandlerService")
@Slf4j
public class ManyFilesToInputHandlerService extends BaseProductHandlerService {

	private final String cycleCotm = "COTD";

	@Override
	public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
		String issue = triggerParam.getExecutorParams();
		String cycle = productParam.getCycle();
		log.info("---BaseBusinessHandlerService,获取输入文件期次：{},周期：{}",issue,cycle);
		String inputPath = productParam.getInputFilePath();
		String fileNamePattern = productParam.getFileNamePattern();
		if (cycleCotm.equals(cycle)) {
			//周期为旬，修改正则中的dd
			String s = issue.substring(6, 8);
			if (s.equals("01")) {
				fileNamePattern = fileNamePattern.replace("dd","(0[1-9]|10)");
			} else if (s.equals("11")) {
				fileNamePattern = fileNamePattern.replace("dd","(1[1-9]|20)");
			} else {
				fileNamePattern = fileNamePattern.replace("dd","(2[1-9]|3[0,1])");
			}
		}
		//没有正则输入文件只有路径,有正则输入文件为匹配到的具体文件
		String inputInfo = super.getInputInfo(issue, fileNamePattern, inputPath);
		if(inputInfo == null){
			return BaseProductServiceConstant.No_Input_Msg + ":" + inputPath;
		}
		// 月直接返回，旬的过滤出10天的
		inputXmlParam.setInputFile(inputInfo);
		return null;
	}

}
