package com.njht.webyun.publish.feedback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.webyun.publish.feedback.dto.FeedBackDto;
import com.njht.webyun.publish.feedback.entity.FeedbackImgurlEntity;
import com.njht.webyun.publish.feedback.vo.FeedbackReqVo;

import java.util.List;

/**
 * 
 *
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:50:22
 */
public interface FeedbackImgurlService extends IService<FeedbackImgurlEntity> {

    /**
     * 保存图片相对路径
     * @param id
     * @param fileList
     * @return
     */
    Boolean saveImgListInfo(Integer id, List<String> fileList);

    /**
     * 用户反馈信息关联图片表
     * @param list
     * @return
     */
    List<FeedbackReqVo> getQueryResult(List<FeedBackDto> list);
}

