package com.njht.webyun.publish.notice.controller;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.publish.notice.service.NoticeInfoService;
import com.njht.webyun.publish.notice.service.NoticeUserService;
import com.njht.webyun.publish.notice.vo.NoticeInfoSearchVo;
import com.njht.webyun.utils.R;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-26 12:24:54
 */
@RestController
@RequestMapping("notice/info")
@Api(tags = "通知公告")
public class NoticeInfoController {

    @Autowired
    private NoticeInfoService noticeInfoService;

    @Autowired
    private NoticeUserService noticeUserService;

    /**
     * 通知公告列表
     */
    @ApiOperation(value = "通知公告列表", notes = "通知公告列表")
    @PostMapping("/list")
    @ResponseBody
    public ReturnT<Object> list(@RequestBody NoticeInfoSearchVo searchVo){
        Map<String,Object> map = noticeInfoService.queryPage(searchVo);
        if (map.isEmpty()){
            return ReturnT.failed();
        }
        return ReturnT.success(map);
    }


    @ApiOperation(value = "消息置顶", notes = "消息置顶")
    @PostMapping("/toTop")
    @ResponseBody
    public ReturnT<Object> toTop(@RequestParam("id") String id){
        boolean flag = noticeInfoService.toTop(id);
        return ReturnT.success(flag);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "发布通知公告", notes = "发布通知公告")
    @PostMapping("/save")
    @ResponseBody
    public ReturnT save(@RequestParam("title") String title,
                  @RequestParam("content") String content,
                        @RequestParam("author") String author,
                  @RequestParam("isTop") Integer isTop,
                  @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("time") Date time,
                  @RequestParam(value = "file",required = false) MultipartFile file){
        return noticeInfoService.saveOrUpdateNoticeInfo(null,title, content, isTop, time, file,author);
    }


    /**
     * 编辑
     */
    @ApiOperation(value = "编辑通知公告信息", notes = "编辑通知公告信息")
    @PostMapping("/update")
    @ResponseBody
    public ReturnT update(
                        @RequestParam("id") String id,
                        @RequestParam(value = "title",required = false) String title,
                        @RequestParam(value = "content",required = false) String content,
                        @RequestParam(value = "author",required = false) String author,
                        @RequestParam(value = "isTop",required = false) Integer isTop,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "time",required = false) Date time,
                        @RequestParam(value = "file",required = false) MultipartFile file){
        return noticeInfoService.saveOrUpdateNoticeInfo(id,title, content, isTop, time, file,author);
    }

    /**
     * 消息已读，删除对应中间表的用户信息
     */
    @ApiOperation(value = "消息已读", notes = "消息已读")
    @PostMapping("/read")
    @ResponseBody
    public ReturnT info(@RequestParam("userId") Integer id,@RequestParam("noticeids") List<String> noticeIds){
        noticeUserService.removeByUserIdAndNoticeId(id,noticeIds);
        return ReturnT.success();
    }

    /**
     * 消息通知
     */
    @ApiOperation(value = "消息通知", notes = "消息通知")
    @PostMapping("/send")
    @ResponseBody
    public ReturnT<List<String>> send(@RequestParam("userId") Integer userId){
        // 用户id从token中获取
        userId = UserUtil.getCurrentUser().getUserId();
        List<String> noticeIdList = noticeUserService.getUnReadNoticeInfoByUserId(userId);
        return ReturnT.success(noticeIdList);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除通知公告信息", notes = "删除通知公告信息")
    @PostMapping("/delete")
    @ResponseBody
    public R delete(@RequestParam String[] ids){
		noticeInfoService.removeNoticeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
