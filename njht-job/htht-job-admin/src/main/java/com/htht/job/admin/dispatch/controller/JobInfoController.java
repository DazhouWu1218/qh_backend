package com.htht.job.admin.dispatch.controller;

import com.htht.job.admin.common.ThreadLocalParams;
import com.htht.job.admin.dispatch.controller.annotation.PermissionProductIds;
import com.htht.job.admin.dispatch.core.thread.JobTriggerPoolHelper;
import com.htht.job.admin.dispatch.core.trigger.TriggerTypeEnum;
import com.htht.job.admin.dispatch.service.DataCategoryService;
import com.htht.job.admin.dispatch.service.XxlJobService;
import com.htht.job.admin.dispatch.vo.JobAlgorithmReqVo;
import com.htht.job.admin.dispatch.vo.JobDetailReqVo;
import com.htht.job.admin.dispatch.vo.JobReqVo;
import com.htht.job.admin.dispatch.vo.JobVo;
import com.htht.job.admin.template.service.TemplateParametersService;
import com.htht.job.admin.template.vo.TemplateParamReqVo;
import com.htht.job.core.biz.model.ReturnT;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.enums.NumberEnum;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.StringUtils;
import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * index controller
 * @author piesat 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
@Api(tags = "任务调度")
public class JobInfoController {

	@Resource
	private XxlJobService xxlJobService;
	@Autowired
	private DataCategoryService dataCategoryService;

	@Autowired
	private TemplateParametersService templateParametersService;


	@ApiOperation(value = "根据插件id加载插件模板", notes = "首次加载模板时用，不能用于查询")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="id", dataType="String", value="插件id"),
	})
	@GetMapping("/getPluginParamList")
	@ResponseBody
	public ReturnT<List<TemplateParamReqVo>> getPluginParamList(@RequestParam("id") String id) {
		List<TemplateParamReqVo> paramInfoVoList = templateParametersService.getParamList(id, NumberEnum.NUMBER_0.getNum());
		return ReturnT.success(paramInfoVoList);
	}

	@ApiOperation(value = "根据算法id加载算法模板", notes = "首次加载模板时用，不能用于查询")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="id", dataType="String", value="插件id"),
	})
	@GetMapping("/getAlgorithmParamList")
	@ResponseBody
	public ReturnT<List<TemplateParamReqVo>> getAlgorithmParamList(@RequestParam(value = "id") String id) {
		List<TemplateParamReqVo> paramInfoVoList = templateParametersService.getParamList(id, NumberEnum.NUMBER_1.getNum());
		return ReturnT.success(paramInfoVoList);
	}

	@ApiOperation(value = "任务详情基础信息集合", notes = "任务详情基础信息集合")
	@GetMapping("/jobDetailInfo")
	@ResponseBody
	public ReturnT<JobDetailReqVo> jobDetailInfo() {
		JobDetailReqVo reqVo = xxlJobService.getJobDetailInfo();
		return ReturnT.success(reqVo);
	}

	@ApiOperation(value = "任务详情算法信息集合", notes = "任务详情基础信息集合")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="groupId", dataType="String", value="执行器id"),
			@ApiImplicitParam(paramType="query", name="handlerId", dataType="String", value="插件id")
	})
	@PostMapping("/JobAlgorithmInfo")
	@ResponseBody
	public ReturnT<JobAlgorithmReqVo> jobAlgorithmInfo(@RequestParam(value = "groupId",required = false)String groupId,
													   @RequestParam(value = "handlerId",required = false)String handlerId) {
		JobAlgorithmReqVo reqVo = xxlJobService.getJobAlgorithmInfo(groupId,handlerId);
		return ReturnT.success(reqVo);
	}
	/**
	 * 任务分页查询
	 * @param jobVo
	 * @return
	 */
	@ApiOperation(value = "执行任务分页查询", notes = "执行任务分页查询")
	@PermissionProductIds(value = "#jobVo.productId")
	@PostMapping("/page")
	@ResponseBody
	public ReturnT<PageUtils> pageList(@Validated @RequestBody JobVo jobVo) {
		List<String> productIds = ThreadLocalParams.productIds.get();
		if (!productIds.isEmpty()) {
			String productId = StringUtils.joinWith(",",productIds.toArray());
			jobVo.setProductId(productId);
		}
		PageUtils page = xxlJobService.queryPage(jobVo);
		return ReturnT.success(page);
	}

	/**
	 * 查询任务详情
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据任务id查询任务详情", notes = "根据任务id查询任务详情")
	@PostMapping("/info")
	@ResponseBody
	public ReturnT<JobReqVo> jobDetailInfo(@RequestParam("id") Integer id) {
		JobReqVo reqVo = xxlJobService.jobDetailInfo(id);
		return ReturnT.success(reqVo);
	}

	/**
	 * 生成算法目录树（根据用户角色过滤）
	 */
	@ApiOperation(value = "任务目录树", notes = "产品目录树")
	@GetMapping("/dataCategory/tree")
	@ResponseBody
	public ReturnT<List<Tree>> dataCategoryTree(){
		List<Tree> treeList = dataCategoryService.queryTreeListByRoleId();
		return ReturnT.success(treeList);
	}

	@ApiOperation(value = "新增/复制一条任务", notes = "新增/复制一条任务")
	@PostMapping("/add")
	@ResponseBody
	public ReturnT<String> add(@Validated(SaveGroup.class) @RequestBody JobReqVo infoDto) {
		return xxlJobService.add(infoDto);
	}

	@ApiOperation(value = "修改一条任务", notes = "修改一条任务")
	@PostMapping("/update")
	@ResponseBody
	public ReturnT<String> update(@Validated(UpdateGroup.class) @RequestBody JobReqVo infoDto) {
		return xxlJobService.update(infoDto);
	}

	@ApiOperation(value = "删除一条任务", notes = "删除一条任务")
	@ApiImplicitParam(paramType="query", name="id", dataType="Integer", value="任务id")
	@PostMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id) {
		return xxlJobService.remove(id);
	}

	@ApiOperation(value = "停止一条任务", notes = "停止一条任务")
	@ApiImplicitParam(paramType="query", name="id", dataType="Integer", value="任务id")
	@PostMapping("/stop")
	@ResponseBody
	public ReturnT<String> pause(int id) {
		return xxlJobService.stop(id);
	}

	@ApiOperation(value = "启动一条任务", notes = "启动一条任务")
	@ApiImplicitParam(paramType="query", name="id", dataType="Integer", value="任务id")
	@PostMapping("/start")
	@ResponseBody
	public ReturnT<String> start(int id) {
		return xxlJobService.start(id);
	}

	@ApiOperation(value = "任务执行一次", notes = "任务执行一次")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="id", dataType="Integer", value="任务id"),
			@ApiImplicitParam(paramType="query", name="addressList", dataType="String", value="任务执行节点,逗号分隔开")
	})
	@PostMapping("/trigger")
	@ResponseBody
	public ReturnT<String> triggerJob(int id, String addressList) {
		JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, "", addressList);
		return ReturnT.success();
	}

	@ApiOperation(value = "根据任务id查询执行节点", notes = "根据任务id查询执行节点")
	@ApiImplicitParam(paramType="query", name="id", dataType="Integer", value="任务id")
	@PostMapping("/addressList")
	@ResponseBody
	public ReturnT<List<String>> queryAddressList(@RequestParam("id") int id) {
		List<String> list = xxlJobService.queryAddressList(id);
		return ReturnT.success(list);
	}

	@ApiOperation(value = "根据目录id查询对应任务", notes = "根据目录id查询对应任务")
	@ApiImplicitParam(paramType="query", name="productId", dataType="String", value="目录id")
	@PermissionProductIds(value = "#productId")
	@PostMapping("/jobDescList")
	@ResponseBody
	public ReturnT<List<CommonEntity>> queryJobDescList(@RequestParam("productId") String productId) {
		List<String> productIds = ThreadLocalParams.productIds.get();
		List<CommonEntity> list = xxlJobService.queryJobDescList(productIds);
		return ReturnT.success(list);
	}

}
