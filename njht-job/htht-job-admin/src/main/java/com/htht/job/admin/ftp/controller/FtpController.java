package com.htht.job.admin.ftp.controller;

import com.htht.job.admin.ftp.service.FtpService;
import com.htht.job.admin.ftp.vo.FtpVo;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.valid.SaveGroup;
import com.njht.webyun.valid.UpdateGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 调度平台-FTP配置信息表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-06-27 17:05:24
 */
@RestController
@RequestMapping("/ftp")
@Api(tags = "ftp管理")
public class FtpController {
    @Autowired
    private FtpService ftpService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="name", value="名称"),
            @ApiImplicitParam(paramType="Integer", name="page",value="第几页"),
            @ApiImplicitParam(paramType="Integer", name="size",value="每页大小")
    })
    @PostMapping("/list")
    public ReturnT<PageUtils> list(@RequestParam(value = "name",required = false) String name,
                                   @RequestParam("page") Integer page,@RequestParam("size") Integer size){
        PageUtils pageUtils = ftpService.queryPage(name,page,size);
        return ReturnT.success(pageUtils);
    }
    /**
     * 列表
     */
    @ApiOperation(value = "FTP键值对")
    @GetMapping("/ftpList")
    public ReturnT<List<CommonEntity>> ftpList(){
        List<CommonEntity> list = ftpService.queryFtpList();
        return ReturnT.success(list);
    }

    @ApiOperation(value = "测试 连接")
    @ApiImplicitParam(paramType="String", name="id", value="id")
    @PostMapping("/isConnect")
    public ReturnT<String> isConnect(@RequestParam("id") String id){
        return ftpService.testConnect(id);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public ReturnT<String> save(@Validated(SaveGroup.class) @RequestBody FtpVo ftp){
        ftpService.insertInfo(ftp);
        return ReturnT.success();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public ReturnT<String> update(@Validated(UpdateGroup.class) @RequestBody FtpVo ftp){
        ftpService.edit(ftp);
        return ReturnT.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParam(paramType="String", name="id", value="id")
    @PostMapping("/delete")
    public ReturnT<String> delete(@RequestParam("id") String id){
		ftpService.removeById(id);
        return ReturnT.success();
    }

}
