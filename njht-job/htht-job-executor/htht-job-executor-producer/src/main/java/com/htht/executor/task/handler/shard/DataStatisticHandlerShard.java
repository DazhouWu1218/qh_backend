package com.htht.executor.task.handler.shard;


import com.htht.executor.fileStatistic.service.DataBaseInfoService;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.shard.SharingHandler;
import com.njht.entity.dataReport.DataBaseInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据汇集 预处理统计分片
 * @author Administrator
 */
@Service("dataStatisticHandlerShard")
public class DataStatisticHandlerShard implements SharingHandler {

	@Autowired
	private DataBaseInfoService dataBaseInfoService;

	@Override
	public ReturnT<List<String>> executeShard(TaskParam taskParam) {
		ReturnT<List<String>> returnT = new ReturnT<>();

		// 获取要处理的数据集 并分片
		List<DataBaseInfoEntity> list = dataBaseInfoService.list();
		returnT.setCode(ReturnT.SUCCESS_CODE);
		return returnT;
	}
}
