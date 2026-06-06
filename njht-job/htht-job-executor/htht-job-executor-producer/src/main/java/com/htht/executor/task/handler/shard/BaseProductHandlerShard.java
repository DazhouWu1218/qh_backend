package com.htht.executor.task.handler.shard;


import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.executor.task.util.ProductServiceUtil;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.shard.SharingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基本产品模板分片调度逻辑
 * @author piesat
 */
@Service("baseProductHandlerShard")
public class BaseProductHandlerShard implements SharingHandler {


	@Autowired
	private ProductServiceUtil productServiceUtil;

	@Override
	public ReturnT<List<String>> executeShard(TaskParam taskParam) {
		BaseProductHandlerService baseProductHandlerService = productServiceUtil.getServiceInfo(taskParam);
		return baseProductHandlerService.executeShard(taskParam);
	}
}
