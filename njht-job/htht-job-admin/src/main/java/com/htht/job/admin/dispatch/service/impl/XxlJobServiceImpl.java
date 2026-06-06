package com.htht.job.admin.dispatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.pagehelper.PageInfo;
import com.htht.job.admin.algorithm.service.AlgorithmService;
import com.htht.job.admin.dispatch.core.cron.CronExpression;
import com.htht.job.admin.dispatch.core.model.JobInfoDto;
import com.htht.job.admin.dispatch.core.model.XxlJobGroup;
import com.htht.job.admin.dispatch.core.model.XxlJobInfo;
import com.htht.job.admin.dispatch.core.route.ExecutorRouteStrategyEnum;
import com.htht.job.admin.dispatch.core.scheduler.MisfireStrategyEnum;
import com.htht.job.admin.dispatch.core.scheduler.ScheduleTypeEnum;
import com.htht.job.admin.dispatch.core.thread.JobScheduleHelper;
import com.htht.job.admin.dispatch.core.util.I18nUtil;
import com.htht.job.admin.dispatch.dao.XxlJobGroupDao;
import com.htht.job.admin.dispatch.dao.XxlJobInfoDao;
import com.htht.job.admin.dispatch.dao.XxlJobLogDao;
import com.htht.job.admin.dispatch.dao.XxlJobLogGlueDao;
import com.htht.job.admin.dispatch.service.XxlJobService;
import com.htht.job.admin.dispatch.util.EnumInfoUtil;
import com.htht.job.admin.dispatch.vo.*;
import com.htht.job.admin.param.service.TaskParamService;
import com.htht.job.admin.plugin.entity.HandlerEntity;
import com.htht.job.admin.plugin.service.HandlerService;
import com.htht.job.admin.plugin.vo.PluginTaskReqVo;
import com.htht.job.admin.template.vo.TemplateParamReqVo;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.enums.ExecutorBlockStrategyEnum;
import com.htht.job.core.glue.GlueTypeEnum;
import com.htht.job.core.util.DateUtil;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.utils.PageUtil;
import com.njht.webyun.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * core job action for xxl-job
 * @author piesat 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl extends ServiceImpl<XxlJobInfoDao,XxlJobInfo> implements XxlJobService {
	private static Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobInfoDao xxlJobInfoDao;
	@Resource
	public XxlJobLogDao xxlJobLogDao;
	@Resource
	private XxlJobLogGlueDao xxlJobLogGlueDao;

	@Autowired
	private AlgorithmService algorithmService;

	@Autowired
	private HandlerService handlerService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TaskParamService taskParamService;

	/**
	 * 分页查询调度任务
	 * @param jobVo
	 * @return
	 */
	@Override
	public PageUtils queryPage(JobVo jobVo) {
		List<String> productIds = new ArrayList<>();
		// 分页对象
		if (StringUtils.isNotEmpty(jobVo.getProductId())) {
			productIds = Arrays.asList(jobVo.getProductId().split(","));
		}
		// 任务列表
		PageUtil.setPageAndSize(jobVo.getPage(),jobVo.getSize(),1,10);

		List<XxlJobInfo> jobInfoList = xxlJobInfoDao.pageList(productIds, jobVo.getJobGroup(), jobVo.getTriggerStatus(), jobVo.getJobDesc(),jobVo.getAuthor(),jobVo.getJobId());
		PageUtils pageUtils = new PageUtils(new PageInfo<>(jobInfoList));
		// 设置返回结果
		List<JobInfoReqVo> collect = Optional.ofNullable(jobInfoList).orElse(new ArrayList<>())
				.stream()
				.map(item -> {
					JobInfoReqVo reqVo = new JobInfoReqVo();
					BeanUtils.copyProperties(item, reqVo);
					reqVo.setNextTriggerTime(this.getStrNextTime(item.getTriggerNextTime()));
					return reqVo;
				}).collect(Collectors.toList());
		pageUtils.setList(collect);
		return pageUtils;
	}

	/**
	 * 获取下次执行时间
	 * @param triggerNextTime
	 * @return
	 */
	private String getStrNextTime(long triggerNextTime) {
		String s = null;
		if (triggerNextTime != 0L) {
			s = DateUtil.formatDateTime(new Date(triggerNextTime), DateConstant.YYYY_MM_DD_HH_MM_SS);
		} 
		return s;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnT<String> add(JobReqVo infoDto) {
		XxlJobInfo jobInfo = new XxlJobInfo();
		BeanUtils.copyProperties(infoDto,jobInfo);
		// valid job
		if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_gluetype")+I18nUtil.getString("system_unvalid")) );
		}
		if (GlueTypeEnum.BEAN==GlueTypeEnum.match(jobInfo.getGlueType()) && (jobInfo.getExecutorHandler()==null || jobInfo.getExecutorHandler().trim().length()==0) ) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("system_please_input")+"JobHandler") );
		}
		// 》fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL==GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource()!=null) {
			jobInfo.setGlueSource(jobInfo.getGlueSource().replace("\r", ""));
		}

		ReturnT<String> returnTrigger = this.validParam(jobInfo);
		if (Objects.equals(returnTrigger.getCode(),ReturnT.FAIL_CODE)) {
			return returnTrigger;
		}
		HandlerEntity handlerEntity = handlerService.getHandlerById(infoDto.getHandlerId());
		if (handlerEntity == null) {
			throw new CommonException("handler is null,handlerId is doesn't exist");
		}
		// add in db
		jobInfo.setExecutorHandler(handlerEntity.getModelIdentify());
		jobInfo.setAddTime(new Date());
		jobInfo.setUpdateTime(new Date());
		jobInfo.setGlueUpdatetime(new Date());
		this.save(jobInfo);
		if (jobInfo.getId() < 1) {
			throw new CommonException(I18nUtil.getString("jobinfo_field_add")+I18nUtil.getString("system_fail"));
		}
		// 新增参数信息(动态参数和固定参数)
		infoDto.setId(jobInfo.getId());
		taskParamService.saveParams(infoDto);
		return ReturnT.success(String.valueOf(jobInfo.getId()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnT<String> update(JobReqVo infoDto) {
		XxlJobInfo jobInfo = new XxlJobInfo();
		BeanUtils.copyProperties(infoDto,jobInfo);
		// stage job info
		XxlJobInfo existsJobInfo = xxlJobInfoDao.loadById(jobInfo.getId());
		if (existsJobInfo == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_id")+I18nUtil.getString("system_not_found")) );
		}
		// next trigger time (5s后生效，避开预读周期)
		long nextTriggerTime = existsJobInfo.getTriggerNextTime();
		boolean scheduleDataNotChanged = jobInfo.getScheduleType().equals(existsJobInfo.getScheduleType()) && jobInfo.getScheduleConf().equals(existsJobInfo.getScheduleConf());
		if (existsJobInfo.getTriggerStatus() == 1 && !scheduleDataNotChanged) {
			nextTriggerTime = getNextTriggerTime(jobInfo);
			if (nextTriggerTime == 0) {
				return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
			}
		}
		BeanUtils.copyProperties(jobInfo,existsJobInfo);
		existsJobInfo.setUpdateTime(new Date());
		// handlerId 修改之后 对应的identify也要修改(前端传参要修改handlerId 且handlerId 与数据库不一致)
		if (jobInfo.getHandlerId() != null && !jobInfo.getHandlerId().equals(existsJobInfo.getHandlerId())) {
			HandlerEntity handlerEntity = handlerService.getHandlerById(infoDto.getHandlerId());
			if (handlerEntity == null) {
				throw new CommonException("handler is null,handlerId is doesn't exist");
			}
			existsJobInfo.setExecutorHandler(handlerEntity.getModelIdentify());
		}
        this.updateById(existsJobInfo);
        // 修改 参数
		taskParamService.updateParams(infoDto);
		return ReturnT.success();
	}

	@Override
    public JobDetailReqVo getJobDetailInfo() {
		JobDetailReqVo reqVo = new JobDetailReqVo();
		// 路由策略
		reqVo.setExecutorRouteStrategyList(EnumInfoUtil.executorRouteStrategyList());
		// 阻塞策略
		reqVo.setExecutorBlockStrategyList(EnumInfoUtil.executorBlockList());
		// 运行模式
		reqVo.setGlueTypeList(EnumInfoUtil.glueTypeList());
		// 调度类型
		reqVo.setScheduleTypeList(EnumInfoUtil.scheduleTypeList());
		// 调度过期策略
		reqVo.setMisfireStrategyList(EnumInfoUtil.misfireStrategyList());
		return reqVo;
    }

	@Override
	public JobAlgorithmReqVo getJobAlgorithmInfo(String groupId, String handlerId) {
		JobAlgorithmReqVo reqVo = new JobAlgorithmReqVo();
		int iGroupId = StringUtils.isEmpty(groupId) ? 0 : Integer.parseInt(groupId);
		// 执行器列表 不传查全部
		List<XxlJobGroup> xxlJobGroups = xxlJobGroupDao.selectGroupList(null,null,iGroupId);
		List<CommonEntity> groupList = Optional.ofNullable(xxlJobGroups).orElse(new ArrayList<>())
				.stream()
				.map(item -> new CommonEntity(String.valueOf(item.getId()), item.getTitle()))
				.collect(Collectors.toList());
		reqVo.setJobGroupList(groupList);
		// 插件列表
		List<PluginTaskReqVo> pluginList = handlerService.queryPluginListByGroupId(groupId,handlerId);
		reqVo.setHandlerList(pluginList);
		
		// 算法树结构列表
		List<Tree> algTreeList = algorithmService.queryAlgorithmList(groupId,handlerId);
		reqVo.setAlgorithmList(algTreeList);
		return reqVo;
	}

	@Override
	public List<String> queryAddressList(int id) {
		// 该任务与算法绑定 获取算法绑定的执行器节点
		String addressList = algorithmService.queryAddressListByJobId(id);
		if (StringUtils.isNotEmpty(addressList)) {
			return Arrays.asList(addressList.split(","));
		}
		// 任务没有与算法绑定,获取执行器节点
		String s = xxlJobGroupDao.queryRegistryListByJobId(id);
		if (StringUtils.isNotEmpty(s)) {
			return Arrays.asList(s.split(","));
		}
		return new ArrayList<>();
	}

	@Override
	public List<CommonEntity> queryJobDescList(List<String> productIds) {
		List<CommonEntity> list = new ArrayList<>();
		if (!CollectionUtils.isEmpty(productIds)) {
			list.add(new CommonEntity("-1","全部"));
			List<CommonEntity> commonEntities = xxlJobInfoDao.selectJobDescList(productIds);
			list.addAll(commonEntities);
		}
		return list;
	}

	@Override
	public JobReqVo jobDetailInfo(Integer id) {
		JobInfoDto dto = xxlJobInfoDao.selectJobDetailInfo(id);
		JobReqVo reqVo = new JobReqVo();
		BeanUtils.copyProperties(dto,reqVo);
		// 查询 算法id 所有父级id 用于前端渲染树结构
		List<String> algorithmIdList =  algorithmService.queryAlgorithmIdList(reqVo.getAlgorithmId());
		reqVo.setAlgorithmIdList(Optional.ofNullable(algorithmIdList).orElse(new ArrayList<>()));
		// 参数返回值设置
		reqVo.setDynamicList(this.formatJsonToList(dto.getDynamicParameter()));
		reqVo.setModelParameterList(this.formatJsonToList(dto.getModelParameters()));
		reqVo.setFixedList(this.formatJsonToList(dto.getFixedParameter()));
		return reqVo;
	}

    @Override
    public List<String> queryJobIdListByHandlerIds(List<String> ids) {
        return xxlJobInfoDao.selectJobIdListByHandlerIds(ids);
    }

	@Override
	public List<String> queryJobIdListByAlgorithmIds(List<String> ids) {
		return xxlJobInfoDao.selectJobIdListByAlgorithmIds(ids);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnT<String> remove(int id) {
		XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
		if (xxlJobInfo == null) {
			return ReturnT.SUCCESS;
		}
		xxlJobInfoDao.delete(id);
		taskParamService.deleteByJobId(id);
		xxlJobLogDao.delete(id);
		xxlJobLogGlueDao.deleteByJobId(id);
		return ReturnT.success();
	}

	@Override
	public ReturnT<String> start(int id) {
		XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);

		// valid
		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(xxlJobInfo.getScheduleType(), ScheduleTypeEnum.NONE);
		if (ScheduleTypeEnum.NONE == scheduleTypeEnum) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type_none_limit_start")) );
		}
		// next trigger time (5s后生效，避开预读周期)
		long nextTriggerTime = this.getNextTriggerTime(xxlJobInfo);
		if (nextTriggerTime == 0L) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
		}
		xxlJobInfo.setTriggerStatus(1);
		xxlJobInfo.setTriggerLastTime(0);
		xxlJobInfo.setTriggerNextTime(nextTriggerTime);

		xxlJobInfo.setUpdateTime(new Date());
		this.updateById(xxlJobInfo);
		return ReturnT.success();
	}

	@Override
	public ReturnT<String> stop(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);

		xxlJobInfo.setTriggerStatus(0);
		xxlJobInfo.setTriggerLastTime(0);
		xxlJobInfo.setTriggerNextTime(0);

		xxlJobInfo.setUpdateTime(new Date());
		this.updateById(xxlJobInfo);
		return ReturnT.success();
	}

	/**
	 * 对新增修改的一些特殊参数进行校验
	 * @param jobInfo
	 * @return
	 */
	private ReturnT<String> validParam(XxlJobInfo jobInfo) {
		// group valid
		XxlJobGroup jobGroup = xxlJobGroupDao.load(jobInfo.getJobGroup());
		if (jobGroup == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_jobgroup")+I18nUtil.getString("system_unvalid")) );
		}
		// valid trigger
		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
		if (scheduleTypeEnum == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
		}
		if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
			if (jobInfo.getScheduleConf()==null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
				return new ReturnT<>(ReturnT.FAIL_CODE, "Cron"+I18nUtil.getString("system_unvalid"));
			}
		} else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE/* || scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY*/) {
			if (jobInfo.getScheduleConf() == null) {
				return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")) );
			}
			try {
				int fixSecond = Integer.parseInt(jobInfo.getScheduleConf());
				if (fixSecond < 1) {
					return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
				}
			} catch (Exception e) {
				return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
			}
		}
		// valid advanced
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (MisfireStrategyEnum.match(jobInfo.getMisfireStrategy(), null) == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("misfire_strategy")+I18nUtil.getString("system_unvalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<>(ReturnT.FAIL_CODE, (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_unvalid")) );
		}

		// 》ChildJobId valid
		if (jobInfo.getChildJobId()!=null && jobInfo.getChildJobId().trim().length()>0) {
			String[] childJobIds = jobInfo.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (childJobIdItem!=null && childJobIdItem.trim().length()>0 && isNumeric(childJobIdItem)) {
					XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.parseInt(childJobIdItem));
					if (childJobInfo==null) {
						return new ReturnT<>(ReturnT.FAIL_CODE,
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
				} else {
					return new ReturnT<>(ReturnT.FAIL_CODE,
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_unvalid")), childJobIdItem));
				}
			}
			// join , avoid "xxx,,"
			String temp = "";
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);
			jobInfo.setChildJobId(temp);
		}
		return new ReturnT<>(String.valueOf(jobInfo.getId()));
	}

	private boolean isNumeric(String str){
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 将JSON 转为list
	 * @param jsonStr
	 * @return
	 */
	private List<TemplateParamReqVo>  formatJsonToList(String jsonStr) {
		if (StringUtils.isEmpty(jsonStr)) {
			return new ArrayList<>();
		}
		List<TemplateParamReqVo> commonParameterList;
		try {
			CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, TemplateParamReqVo.class);
			commonParameterList = objectMapper.readValue(jsonStr,listType);
		} catch (IOException e) {
			throw new CommonException("json 格式转换异常");
		}
		return commonParameterList;

	}

	/**
	 * 获取下次执行时间
	 * @param jobInfo
	 * @return
	 */
	private long getNextTriggerTime(XxlJobInfo jobInfo) {
		long nextTriggerTime = 0L;
		try {
			Date nextValidTime = JobScheduleHelper.generateNextValidTime(jobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
			if (nextValidTime != null) {
				nextTriggerTime = nextValidTime.getTime();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return nextTriggerTime;
	}
}
