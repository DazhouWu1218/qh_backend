package com.njht.webyun.publish.feedback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.publish.common.util.FileUploadUtils;
import com.njht.webyun.publish.feedback.constant.FeedBackConstant;
import com.njht.webyun.publish.feedback.dao.FeedbackManagementDao;
import com.njht.webyun.publish.feedback.dto.FeedBackDto;
import com.njht.webyun.publish.feedback.entity.FeedbackManagementEntity;
import com.njht.webyun.publish.feedback.service.FeedbackImgurlService;
import com.njht.webyun.publish.feedback.service.FeedbackManagementService;
import com.njht.webyun.publish.feedback.vo.FeedbackReqVo;
import com.njht.webyun.publish.feedback.vo.FeedbackSearchVo;
import com.njht.webyun.publish.sys.service.RoleService;
import com.njht.webyun.utils.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Administrator
 */
@Service("feedbackManagementService")
@Slf4j
public class FeedbackManagementServiceImpl extends ServiceImpl<FeedbackManagementDao, FeedbackManagementEntity> implements FeedbackManagementService {

    @Autowired
    private FeedbackImgurlService feedbackImgurlService;

    @Autowired
    private RoleService roleService;

    @Value("${upload.path}")
    private String upLoadPath;


    @Value("${file.prefix}")
    private String prefix;


    @Override
    public PageUtils queryPage(FeedbackSearchVo params) {
        // 当前用户
        Integer userId = null;
        // 3 查询我的问题
        if(Objects.equals(FeedBackConstant.FEED_THREE,params.getReplyStatus()) && params.getUserId() != null){
            userId = params.getUserId();
            log.info("==============用户反馈,当前用户id [{}]==============",userId);
        }
        // 2 查全部
        if(Objects.equals(FeedBackConstant.FEED_TWO,params.getReplyStatus())){
            params.setReplyStatus(null);
        }
        PageUtil.setPageAndSize(params.getPage(),params.getSize(),1,10);
        List<FeedBackDto>  feedBackDtoList = baseMapper.selectFeedBackListByQueryInfo(params.getReplyStatus(),userId,0,
                                                                            params.getStartTime(),params.getEndTime(),params.getQueryInfo());
        PageUtils pageUtils = new PageUtils(new PageInfo<>(feedBackDtoList));
        //问题反馈集合
        if(CollectionUtils.isEmpty(feedBackDtoList)){
            return pageUtils;
        }
        // 将问题以及对应图片集合
        List<FeedbackReqVo> reqVoList = feedbackImgurlService.getQueryResult(feedBackDtoList);
        // 返回问题回复条数
        reqVoList = this.getReplyNumInfo(reqVoList);
        pageUtils.setList(reqVoList);
        return pageUtils;
    }

    /**
     * 获取问题的回复条数
     * @param reqVoList
     * @return
     */
    private List<FeedbackReqVo> getReplyNumInfo(List<FeedbackReqVo> reqVoList) {
        List<Integer> idList = reqVoList.stream().map(FeedbackReqVo::getFeedbackId).collect(Collectors.toList());
        QueryWrapper<FeedbackManagementEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("parents_id","feedback_id");
        queryWrapper.in("parents_id",idList);
        List<FeedbackManagementEntity> list = this.list(queryWrapper);
        reqVoList.stream().forEach(item -> {
            //父id 对应的回复信息
            List<FeedbackManagementEntity> collect = list.stream().filter(entity -> entity.getParentsId().equals(item.getFeedbackId())).collect(Collectors.toList());
            int size = Optional.ofNullable(collect).orElse(new ArrayList<>()).size();
            item.setReplyNum(size);
        });
        return reqVoList.stream().
                sorted(Comparator.comparing(FeedbackReqVo::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public ReturnT saveFeedBackInfo(String feedbackType, String feedbackContent, Integer parentsId,
                                    List<MultipartFile> files, Integer userId, Integer toUserId) {
        // 已上传的文件路径集合
        List<String> filePathList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(files)){
            log.info("=========上传文件，并判断文件格式是否正确==============");
            ReturnT<List<String>> r = FileUploadUtils.uploadFiles(files,upLoadPath+File.separator+FeedBackConstant.FEED_IMG_PREFIX);
            if(r.getCode() != ReturnT.SUCCESS_CODE){
                return r;
            }
            filePathList = r.getData();
            log.info("=========文件格式正确，开始入库==============");
        }else {
            log.info("=========没有相关图片信息==============");
        }

        // 用户基本信息入库,并返回用户入库用户信息
        FeedbackManagementEntity feedbackManagementEntity = new FeedbackManagementEntity();
        feedbackManagementEntity.setFeedbackType(StringUtils.isBlank(feedbackType)?null:feedbackType);
        feedbackManagementEntity.setFeedbackContent(StringUtils.isBlank(feedbackContent)?null:feedbackContent);
        feedbackManagementEntity.setParentsId(parentsId);
        //回复
        if(FeedBackConstant.FEED_ZERO.equals(parentsId)){
            //问题第一次新增，未回复
            feedbackManagementEntity.setReplyStatus(0);
        }else {
            //用户 回复 修改问题状态为回复ror
            FeedbackManagementEntity updateEntity = new FeedbackManagementEntity();
            // toUserId 如果是管理员，修改成已回复（1 已回复）
            // 获取当前角色 （如果是管理员，修改问题回复状态为已回复）
            String funCode = roleService.getFunCodeByUserId(userId);
            if(StringUtils.isEmpty(funCode)){
                // 不是管理员修改状态为 未回复
                updateEntity.setReplyStatus(0);
            } else{
                //已回复
                updateEntity.setReplyStatus(1);
            }
            // 对谁说 (问题没有对谁说)
            feedbackManagementEntity.setToUserId(toUserId == null?0:toUserId);
            //设置问题父id信息
            updateEntity.setFeedbackId(parentsId);
            this.updateById(updateEntity);
        }
        //逻辑删除 默认 0 不删除
        feedbackManagementEntity.setDeleted(0);
        //创建人前端传递
        if(userId == null){
            userId = UserUtil.getCurrentUser().getUserId();
        }
        feedbackManagementEntity.setCreatedBy(userId);
        feedbackManagementEntity.setLastUpdatedBy(userId);
        if(FeedBackConstant.FEED_ZERO.equals(parentsId)){
            //问题父id 默认为0
            feedbackManagementEntity.setQuestion(0);
        }else {
            // 回复 为 1
            feedbackManagementEntity.setQuestion(1);
        }
        // 基本信息入库
        this.save(feedbackManagementEntity);
        if(!CollectionUtils.isEmpty(filePathList)){
            // 图片信息入库
            feedbackImgurlService.saveImgListInfo(feedbackManagementEntity.getFeedbackId(),filePathList);
        }
        log.info("=====================入库成功=======================");
        return ReturnT.success();
    }

    @Override
    public List<FeedbackReqVo> queryReplyList(Integer id) {
        List<FeedBackDto> list = baseMapper.selectFeedBackListByQueryInfo(null,null,id,
                null,null,null);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        // 将问题以及对应图片集合 以及用户信息放到返回结果中
        List<FeedbackReqVo> reqVoList = feedbackImgurlService.getQueryResult(list);
        return reqVoList.stream().
                sorted(Comparator.comparing(FeedbackReqVo::getCreatedDate))
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public String getDownLoadPath(List<String> list) {
        String path =upLoadPath + File.separator+ FeedBackConstant.FEED_IMG_PREFIX+File.separator+"userFeedBack"+System.currentTimeMillis()+".docx";
        try (
             FileOutputStream out = new FileOutputStream(path);
             XWPFDocument doc = new XWPFDocument()
        ) {
            // 根据id查询数据库对应信息
            List<FeedBackDto> feedBackList = baseMapper.selectFeedBackList(list);
            //过滤出对应的问题
            List<FeedBackDto> questionList = feedBackList.stream()
                    .filter(item -> item.getParentsId().equals(FeedBackConstant.FEED_ZERO)).collect(Collectors.toList());
            Integer i =0;
            for(FeedBackDto feedBackDto:questionList){
                i++;
                List<FeedBackDto> replyList = feedBackList.stream()
                        .filter(item -> feedBackDto.getFeedbackId().equals(item.getParentsId()))
                        .sorted(Comparator.comparing(FeedbackManagementEntity::getCreatedDate))
                        .collect(Collectors.toList());
                writeTxtToDoc(i,feedBackDto,replyList,doc);
            }
            doc.write(out);
        }
        return FileSearchUtils.getReplaceFilePath(path).replace(FileSearchUtils.getReplaceFilePath(prefix),"");
    }

    /**
     * 往 word 文档里写数据
     * @param feedBack
     * @param replyList
     * @throws Exception
     */
    @SneakyThrows
    private void writeTxtToDoc(Integer i,FeedBackDto feedBack,List<FeedBackDto> replyList,XWPFDocument doc) {

        XWPFParagraph p = doc.createParagraph();
        XWPFRun xwpfRun = p.createRun();
        xwpfRun.setText(i+"、"+feedBack.getType());
        xwpfRun.addBreak();
        xwpfRun.setBold(false);
        xwpfRun.setText("提问人："+feedBack.getUserName()+"  时间:"+DateFormatUtils.dateToStr(feedBack.getCreatedDate(),DateFormatUtils.formatYY_MM_dd_HH_mm));
        xwpfRun.addBreak();
        xwpfRun.setColor("333333");
        xwpfRun.setText("问题："+feedBack.getFeedbackContent());
        xwpfRun.addBreak();
        //写图片
        this.writeImgToDoc(feedBack.getImgUrlList(),xwpfRun);

        // 回复
        xwpfRun.setText(" ");
        xwpfRun.addBreak();
        if(!CollectionUtils.isEmpty(replyList)){
            xwpfRun.setText(" ");
            xwpfRun.addBreak();
            for (FeedBackDto feedBackReply:replyList ){
                if(StringUtils.isEmpty(feedBackReply.getToUserName())){
                    xwpfRun.setText(feedBackReply.getUserName());
                }else {
                    xwpfRun.setText(feedBackReply.getUserName() + "回复" +feedBackReply.getToUserName());
                }
                xwpfRun.addBreak();
                xwpfRun.setText("回复时间：");
                xwpfRun.setText(DateFormatUtils.dateToStr(feedBackReply.getCreatedDate(),DateFormatUtils.formatYY_MM_dd_HH_mm));
                xwpfRun.addBreak();
                xwpfRun.setText("回复内容：");
                xwpfRun.addBreak();
                xwpfRun.setText("  "+feedBackReply.getFeedbackContent());
                this.writeImgToDoc(feedBackReply.getImgUrlList(),xwpfRun);
                xwpfRun.addBreak();
                xwpfRun.setText("  ");
                xwpfRun.addBreak();
            }
            xwpfRun.addBreak();
            xwpfRun.setText(" ");
            xwpfRun.addBreak();
        }
    }


    private static final int FORMAT = Document.PICTURE_TYPE_JPEG;
    @SneakyThrows
    private void writeImgToDoc(List<String> imgUrlList, XWPFRun xwpfRun) {
        for(String imgFile:imgUrlList){
            imgFile = prefix + imgFile;
            xwpfRun.addPicture (new FileInputStream(imgFile),FORMAT,imgFile, Units.toEMU(100), Units.toEMU(100));
        }
    }


}