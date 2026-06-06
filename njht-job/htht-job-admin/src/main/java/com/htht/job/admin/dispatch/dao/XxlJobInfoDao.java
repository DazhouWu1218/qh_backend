package com.htht.job.admin.dispatch.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htht.job.admin.dispatch.core.model.JobInfoDto;
import com.htht.job.admin.dispatch.core.model.XxlJobInfo;
import com.njht.webyun.entity.CommonEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * job info
 * @author piesat 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoDao extends BaseMapper<XxlJobInfo> {

	List<XxlJobInfo> pageList(@Param("productIds") List<String> productIds,
									@Param("jobGroup") Integer jobGroup,
                                     @Param("triggerStatus") Integer triggerStatus,
                                     @Param("jobDesc") String jobDesc,
                                     @Param("author") String author,
									 @Param("jobId") Integer jobId);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("triggerStatus") int triggerStatus,
							 @Param("jobDesc") String jobDesc,
							 @Param("executorHandler") String executorHandler,
							 @Param("author") String author);
	

	public XxlJobInfo loadById(@Param("id") int id);
	
	public int delete(@Param("id") long id);

	public List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	public int findAllCount();

	public List<XxlJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize );

	public int scheduleUpdate(XxlJobInfo xxlJobInfo);

	/**
	 * 查询任务名称
	 * @param productIds
	 * @return
	 */
	List<CommonEntity> selectJobDescList(@Param("productIds")List<String> productIds);

	/**
	 * 查询任务详情
	 * @param id
	 * @return
	 */
	JobInfoDto selectJobDetailInfo(Integer id);

	/**
	 * 根据任务id 集合 查询任务信息
	 * @param jobIdList
	 * @return
	 */
    List<XxlJobInfo> getJobInfoByJobIds(@Param("jobIdList") Set<Integer> jobIdList);

	/**
	 * 通过插件id 查询对应任务
	 * @param ids
	 * @return
	 */
	List<String> selectJobIdListByHandlerIds(@Param("ids")List<String> ids);

	/**
	 * 通过算法id 查询对应任务
	 * @param ids
	 * @return
	 */
	List<String> selectJobIdListByAlgorithmIds(@Param("ids")List<String> ids);
}
