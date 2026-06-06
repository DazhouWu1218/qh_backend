package com.htht.job.admin.dispatch.controller;

import com.htht.job.admin.common.ThreadLocalParams;
import com.htht.job.admin.dispatch.controller.annotation.PermissionProductIds;
import com.htht.job.admin.dispatch.service.JobLogService;
import com.htht.job.admin.dispatch.vo.JobLogVo;
import com.htht.job.admin.dispatch.vo.LogTypeEntity;
import com.htht.job.core.biz.model.LogResult;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.constant.DateConstant;
import com.njht.entity.xxljob.CountInfo;
import com.njht.webyun.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * index controller
 * @author piesat 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/joblog")
@Api(tags = "任务日志")
public class JobLogController {
	private static Logger logger = LoggerFactory.getLogger(JobLogController.class);

	@Autowired
	private JobLogService jobLogService;

	/**
	 * 任务日志（多条件）分页查询
	 * @param jobLogVo
	 * @return
	 */
	@ApiOperation(value = "任务日志（多条件）分页查询", notes = "任务日志（多条件）分页查询")
	@PermissionProductIds(value = "#jobLogVo.treeId")
	@PostMapping("/page")
	@ResponseBody
	public ReturnT<PageUtils> pageList(@RequestBody JobLogVo jobLogVo) {
		List<String> productIds = ThreadLocalParams.productIds.get();
		PageUtils page = jobLogService.queryPage(jobLogVo, productIds);
		return ReturnT.success(page);
	}

	@ApiOperation(value = "日志详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="executorAddress", dataType="String", value="执行地址"),
			@ApiImplicitParam(paramType="query", name="triggerTime", dataType="Date", value="调度时间"),
			@ApiImplicitParam(paramType="query", name="logId", dataType="long", value="日志id")
	})
	@PostMapping("/logDetailCat")
	@ResponseBody
	public ReturnT<LogResult> logDetailCat(@RequestParam("executorAddress") String executorAddress,
										   @RequestParam("triggerTime")
										   @DateTimeFormat(pattern = DateConstant.YYYY_MM_DD_HH_MM_SS) Date triggerTime,
										   @RequestParam("logId") long logId){
		int fromLineNum = 1;
		long time = triggerTime.getTime();
		// 日志查询
		return jobLogService.queryLogDetail(executorAddress,time,logId,fromLineNum);
	}

	@ApiOperation(value = "终止正在运行的任务",notes = "日志运行过程中,通过该接口终止当前正在运行的任务")
	@ApiImplicitParam(paramType="query", name="id", dataType="int", value="日志id")
	@PostMapping("/logKill")
	@ResponseBody
	public ReturnT<String> logKill(int id){
		// base check
		return jobLogService.logKill(id);
	}

	/**
	 * 任务日志清理
	 * @param treeId 产品id
	 * @param jobGroup  执行器id
	 * @param jobId  任务id
	 * @param type  清理时间类型
	 * @return
	 */
	@ApiOperation(value = "任务日志清理", notes = "任务日志清理")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="treeId", dataType="String", value="执行地址"),
			@ApiImplicitParam(paramType="query", name="jobGroup", dataType="int", value="执行器"),
			@ApiImplicitParam(paramType="query", name="jobId", dataType="int", value="任务id"),
			@ApiImplicitParam(paramType="query", name="type", dataType="int", value="删除类型")
	})
	@PostMapping("/clearLog")
	@PermissionProductIds(value = "#treeId")
	@ResponseBody
	public ReturnT<String> clearLog(String treeId,@RequestParam(defaultValue = "0") int jobGroup, @RequestParam(defaultValue = "0")int jobId,@RequestParam(defaultValue = "1") int type){
		jobLogService.clearLog(treeId,jobGroup,jobId,type);
		return ReturnT.success();
	}

	@ApiOperation(value = "根据日志id删除一条日志")
	@PostMapping("/deleteById")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="logId", dataType="Long", value="日志id"),
	})
	@ResponseBody
	public ReturnT<String> deleteById(@RequestParam(defaultValue = "0")Long logId){
		jobLogService.deleteById(logId);
		return ReturnT.success();
	}


	@ApiOperation(value = "删除类型对照关系")
	@PostMapping("/typeList")
	@ResponseBody
	public ReturnT<List<LogTypeEntity>> typeList() {

		List<LogTypeEntity> logEnumList = jobLogService.typeList();

		return ReturnT.success(logEnumList);
	}

	/**
	 * 获取任务，日志以及执行器数量
	 * @return
	 */
	@GetMapping("/count")
	@ResponseBody
	public ReturnT<CountInfo> count() {
		CountInfo countInfo = jobLogService.countInfo();
		return ReturnT.success(countInfo);
	}
}
