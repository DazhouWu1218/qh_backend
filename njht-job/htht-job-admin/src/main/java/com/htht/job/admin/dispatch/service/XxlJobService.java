package com.htht.job.admin.dispatch.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.job.admin.dispatch.core.model.XxlJobInfo;
import com.htht.job.admin.dispatch.vo.JobAlgorithmReqVo;
import com.htht.job.admin.dispatch.vo.JobDetailReqVo;
import com.htht.job.admin.dispatch.vo.JobReqVo;
import com.htht.job.admin.dispatch.vo.JobVo;
import com.htht.job.core.biz.model.ReturnT;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.utils.PageUtils;

import java.util.List;

/**
 * core job action for xxl-job
 * 
 * @author piesat 2016-5-28 15:30:33
 */
public interface XxlJobService extends IService<XxlJobInfo> {

	/**
	 * 分页查询任务
	 * @param jobVo
	 * @return
	 */
	PageUtils queryPage(JobVo jobVo);

	/**
	 * add job
	 *
	 * @param infoDto
	 * @return
	 */
	ReturnT<String> add(JobReqVo infoDto);

	/**
	 * update job
	 *
	 * @param infoDto
	 * @return
	 */
	ReturnT<String> update(JobReqVo infoDto);

	/**
	 * remove job
	 * 	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> remove(int id);

	/**
	 * start job
	 *
	 * @param id
	 * @return
	 */
	public ReturnT<String> start(int id);

	/**
	 * stop job
	 *
	 * @param id
	 * @return
	 */
	ReturnT<String> stop(int id);

	/**
	 * 查询任务详情页下拉框集合信息 2022.06.02
	 * @return
	 */
    JobDetailReqVo getJobDetailInfo();

	/**
	 * 查询算法相关信息
	 * @return
	 * @param groupId
	 * @param handlerId
	 */
	JobAlgorithmReqVo getJobAlgorithmInfo(String groupId, String handlerId);

	/**
	 * 查询任务对应的执行节点
	 * @param id
	 * @return
	 */
	List<String> queryAddressList(int id);

	/**
	 * 根据树结构id 查询任务名称
	 * @param productIds
	 * @return
	 */
	List<CommonEntity> queryJobDescList(List<String> productIds);

	JobReqVo jobDetailInfo(Integer id);

	/**
	 * 通过插件id 查询对应的任务
	 * @param ids
	 * @return
	 */
	List<String> queryJobIdListByHandlerIds(List<String> ids);

	/**
	 * 通过算法id 查询对应任务
	 * @param ids
	 * @return
	 */
	List<String> queryJobIdListByAlgorithmIds(List<String> ids);
}
