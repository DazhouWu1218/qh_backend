package com.htht.executor.task.service.base.impl;

import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.executor.task.util.ShardUtil;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.shard.SpringContextUtil;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 16:35
 * @Description: 基础插件，输入文件个数为7个
 */
@Service(value="gribPreHandlerService")
@Slf4j
public class GribPreHandlerService extends BaseProductHandlerService {


	@Override
	public ReturnT<List<String>> executeShard(TaskParam taskParam) {
		baseBusinessHandlerService = (BaseProductHandlerService) SpringContextUtil.getBeanByType(getClass());
		ReturnT<List<String>> result = new ReturnT<>();
		// 获取 要处理的期次
		List<String> issueList = ShardUtil.getIssueList(taskParam.getModelParameters(), taskParam.getFixedMap(), taskParam.getDynamicMap());
		// 将期次拆分成 08 20 两个时次 （grb2 预处理 每天只处理 08 20 时间）
		List<String> returnIssue = Optional.of(issueList).orElse(new ArrayList<>())
				.stream()
				.flatMap(item -> getDayIssueList(item).stream())
				.collect(Collectors.toList());

		result.setData(returnIssue);
		result.setCode(ReturnT.SUCCESS_CODE);
		log.info("=============总共需要处理：[{}]期数据=================",returnIssue.size());
		return result;
	}

	/**
	 * 获取每天的期次 （08 20）
	 * @return
	 */
	private static List<String> getDayIssueList(String issue) {
		List<String> list = new ArrayList<>();
		// 天
		issue = issue.substring(0,8);
		String issue08 = issue + "0800";
		list.add(issue08);
		String issue20 = issue + "2000";
		list.add(issue20);
		return list;
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
