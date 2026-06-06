package com.htht.job.admin.dispatch.controller;

import com.htht.job.admin.dispatch.core.model.XxlJobGroup;
import com.htht.job.admin.dispatch.service.JobGroupService;
import com.htht.job.admin.dispatch.vo.JobGroupVo;
import com.htht.job.core.biz.model.ReturnT;
import com.njht.webyun.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * job group controller
 * @author piesat 2016-10-02 20:52:56
 */
@Controller
@RequestMapping("/jobgroup")
@Api(tags = "执行器管理")
public class JobGroupController {


	@Autowired
	private JobGroupService jobGroupService;

	@ApiOperation(value = "执行器列表")
	@PostMapping("/list")
	@ResponseBody
	public ReturnT<PageUtils> list(@RequestBody JobGroupVo jobGroupVo){
		PageUtils pageList = jobGroupService.queryPageList(jobGroupVo);
		return ReturnT.success(pageList);
	}

	/**
	 * 执行器节点自动新增，不提供手动新增
	 * @param xxlJobGroup
	 * @return
	 */
	@ApiOperation(value = "执行器新增")
	@PostMapping("/save")
	@ResponseBody
	public ReturnT<String> save(@RequestBody XxlJobGroup xxlJobGroup){
		return jobGroupService.insert(xxlJobGroup);
	}

	@ApiOperation(value = "执行器编辑")
	@PostMapping("/update")
	@ResponseBody
	public ReturnT<String> update(@RequestBody XxlJobGroup xxlJobGroup){
		return jobGroupService.edit(xxlJobGroup);
	}

	@ApiOperation(value = "执行器删除")
	@PostMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id){
		return jobGroupService.delete(id);

	}

	@GetMapping("/loadById")
	@ResponseBody
	public ReturnT<XxlJobGroup> loadById(Integer id){
		XxlJobGroup jobGroup = jobGroupService.loadById(id);
		return jobGroup!=null?new ReturnT<>(jobGroup):new ReturnT<>(ReturnT.FAIL_CODE, null);
	}

}
