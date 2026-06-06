package com.njht.webyun.publish.feedback.controller;

import com.njht.webyun.publish.feedback.service.FeedbackManagementService;
import com.njht.webyun.publish.feedback.vo.FeedbackReqVo;
import com.njht.webyun.publish.feedback.vo.FeedbackSearchVo;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.R;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**

 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-12 14:50:22
 */
@RestController
@RequestMapping("feedback/management")
@Api(tags = "用户反馈")
@Slf4j
public class FeedbackManagementController {
    @Autowired
    private FeedbackManagementService feedbackManagementService;

    /**
     * 查询产品列表feedback/management/list
     */
    @ApiOperation(value = "查询用户反馈问题列表", notes = "查询用户反馈问题列表")
    @PostMapping({"/list"})
    @ResponseBody
    public ReturnT<PageUtils> list(@RequestBody FeedbackSearchVo params){
        PageUtils page = feedbackManagementService.queryPage(params);
        if(page == null){
            return ReturnT.failed();
        }
        return ReturnT.success(page);
    }

    @ApiOperation(value = "查询用户回复列表", notes = "查询用户回复列表")
    @PostMapping({"/replyList"})
    @ResponseBody
    public ReturnT<List<FeedbackReqVo>> replyList(@RequestParam("id")Integer id){
        List<FeedbackReqVo> reqVos = feedbackManagementService.queryReplyList(id);
        return ReturnT.success(reqVos);
    }

    @GetMapping("/downLoad")
    @ApiOperation(value = "下载文件")
    @ApiImplicitParam(paramType="String", name="ids", dataType="String", required=true, value="id 拿逗号分隔开")
    public ReturnT<String> downloadNotOpen(@Validated @Length(min = 1, message = "id不能为空") @RequestParam(name = "ids",required = false) String ids, HttpServletResponse response) {
        List<String> list = Arrays.asList(ids.split(","));
        String path = feedbackManagementService.getDownLoadPath(list);
        return ReturnT.success(path);
    }



    /**
     * 保存
     */
    @ApiOperation(value = "保存用户反馈信息", notes = "保存用户反馈信息")
    @PostMapping(value = {"/save"},consumes = "multipart/form-data")
    @ResponseBody
    public ReturnT save(@RequestParam("feedbackType") String feedbackType,
                       @RequestParam("feedbackContent") String feedbackContent,
                        @RequestParam("parentsId") Integer parentsId,
                        @RequestParam("userId") Integer userId,
                        @RequestParam("files") List<MultipartFile> files){
        //入库出错返回错误信息
        return feedbackManagementService.saveFeedBackInfo(feedbackType, feedbackContent, parentsId, files,userId,null);

    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存用户反馈回复信息", notes = "保存用户反馈信息")
    @PostMapping(value = {"/saveReplay"},consumes = "multipart/form-data")
    @ResponseBody
    public ReturnT save(@RequestParam("feedbackContent") String feedbackContent,
                        @RequestParam("parentsId") Integer parentsId,
                        @RequestParam("userId") Integer userId,
                        @RequestParam("toUserId") Integer toUserId,
                        @RequestParam("files") List<MultipartFile> files){
        //入库出错返回错误信息
        return feedbackManagementService.saveFeedBackInfo(null, feedbackContent, parentsId, files,userId,toUserId);

    }

    /**
     * 删除
     */
    @ApiOperation(value = "批量删除用户反馈信息", notes = "批量删除用户反馈信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="Integer[]", name="feedbackIds", dataType="Integer[]", required=true, value="问题id")
    })
    @PostMapping(value = "/delete",consumes = "multipart/form-data")
    public R delete(@RequestParam("feedbackIds") Integer[] feedbackIds){
		feedbackManagementService.removeByIds(Arrays.asList(feedbackIds));
        return R.ok();
    }

}
