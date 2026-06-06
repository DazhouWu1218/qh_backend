package com.njht.webyun.publish.feedback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njht.webyun.publish.feedback.dao.FeedbackImgurlDao;
import com.njht.webyun.publish.feedback.dto.FeedBackDto;
import com.njht.webyun.publish.feedback.entity.FeedbackImgurlEntity;
import com.njht.webyun.publish.feedback.entity.FeedbackManagementEntity;
import com.njht.webyun.publish.feedback.service.FeedbackImgurlService;
import com.njht.webyun.publish.feedback.vo.FeedbackReqVo;
import com.njht.webyun.utils.FileSearchUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 *
 * @author
 */
@Service("feedbackImgurlService")
public class FeedbackImgurlServiceImpl extends ServiceImpl<FeedbackImgurlDao, FeedbackImgurlEntity> implements FeedbackImgurlService {


    @Value("${file.prefix}")
    private String filePreFix;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveImgListInfo(Integer id, List<String> fileList) {
        AtomicReference<Integer> i = new AtomicReference<>(0);
        List<FeedbackImgurlEntity> collect = fileList.stream().map(s -> {
            FeedbackImgurlEntity entity = new FeedbackImgurlEntity();
            //保留相对路径
            String replace =
                    FileSearchUtils.getReplaceFilePath(s).replace(FileSearchUtils.getReplaceFilePath(filePreFix),"");
            entity.setFeedbackId(id);
            entity.setImgurl(replace);
            entity.setSort(i.getAndSet(i.get() + 1));
            return entity;
        }).collect(Collectors.toList());
        return this.saveBatch(collect);
    }

    @Override
    public List<FeedbackReqVo> getQueryResult(List<FeedBackDto> list) {
        //根据id 集合查询出对应的图片
        List<Integer> idList = list.stream().map(FeedbackManagementEntity::getFeedbackId).collect(Collectors.toList());
        //图片集合
        QueryWrapper<FeedbackImgurlEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("feedback_id",idList);
        List<FeedbackImgurlEntity> imgEntityList = this.list(queryWrapper);
        Map<Integer, List<FeedbackImgurlEntity>> map = null;
        if(!CollectionUtils.isEmpty(imgEntityList)){
            //图片集合根据sort升序，并根据id分组
            map = imgEntityList.stream().sorted(Comparator.comparing(FeedbackImgurlEntity::getSort)).collect(Collectors.groupingBy(FeedbackImgurlEntity::getFeedbackId));
        }
        //设置返回结果并返回
        Map<Integer, List<FeedbackImgurlEntity>> finalMap = map;
        return list.stream().map(item -> {
            FeedbackReqVo reqVo = new FeedbackReqVo();
            BeanUtils.copyProperties(item, reqVo);
            reqVo.setUserId(item.getCreatedBy());
            if(finalMap != null && !CollectionUtils.isEmpty(finalMap.get(item.getFeedbackId()))){
                List<String> imgList = finalMap.get(item.getFeedbackId()).stream().map(FeedbackImgurlEntity::getImgurl).collect(Collectors.toList());
                reqVo.setImgUrlList(imgList);
            }
            return reqVo;
        }).collect(Collectors.toList());
    }

}