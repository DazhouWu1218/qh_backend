package com.njht.webyun.publish.feedback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.publish.feedback.entity.FeedbackManagementEntity;
import com.njht.webyun.publish.feedback.vo.FeedbackReqVo;
import com.njht.webyun.publish.feedback.vo.FeedbackSearchVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:50:22
 */
public interface FeedbackManagementService extends IService<FeedbackManagementEntity> {

    /**
     * 查询用户反馈信息列表
     * @param params
     * @return
     */
    PageUtils queryPage(FeedbackSearchVo params);

    /**
     * 保存用户反馈信息
     * @param feedbackType
     * @param feedbackContent
     * @param parentsId
     * @param files
     * @param userId
     * @param toUserId
     * @return
     */
    ReturnT saveFeedBackInfo(String feedbackType, String feedbackContent,Integer parentsId, List<MultipartFile> files,
                             Integer userId,Integer toUserId);

    /**
     * 根据问题id查询问题回复内容
     * @param id
     * @return
     */
    List<FeedbackReqVo> queryReplyList(Integer id);

    /**
     * 根据问题id，下载问题以及对应回复信息
     * @param list
     * @return
     */
    String getDownLoadPath(List<String> list);
}

