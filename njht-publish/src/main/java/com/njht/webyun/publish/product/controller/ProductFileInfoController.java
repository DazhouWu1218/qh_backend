package com.njht.webyun.publish.product.controller;

import com.njht.webyun.publish.common.util.FileDownLoadUtils;
import com.njht.webyun.publish.product.service.ProductFileInfoService;
import com.njht.webyun.publish.product.vo.ImgInfoReqVo;
import com.njht.webyun.publish.product.vo.ProductFileBaseReqVo;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author daiguojun
 * @date 2021-11-11 10:22:52
 */
@RestController
@RequestMapping("product/fileInfo")
@Api(tags = "产品文件详情")
@Slf4j
public class ProductFileInfoController {

    @Autowired
    private ProductFileInfoService productFileInfoService;

    @ApiOperation(value = "获取产品文件相关信息",consumes = "multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="id", dataType="String", required=true, value="该期产品对应id"),
            @ApiImplicitParam(paramType="String", name="mark", dataType="String", required=true, value="该期产品对应标识"),
            @ApiImplicitParam(paramType="String", name="fileTypes", dataType="List<String>", required=false, value="文件类型,全选用ALL标识，或者传全部类型")
    })
    @PostMapping(value = {"/list"},consumes = "multipart/form-data")
    @ResponseBody
    public ReturnT<List<ProductFileBaseReqVo>> getProductSearchInfo(@RequestParam("id")String id,@RequestParam("mark")String mark,
                                                                    @RequestParam(value ="fileTypes",required = false)List<String> fileTypeList){
        List<ProductFileBaseReqVo> list = productFileInfoService.getFileInfoList(id,mark,fileTypeList);
        return ReturnT.success(list);
    }


    @ApiOperation(value = "播放专题图以及栅格数据数据",consumes = "multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="ids", dataType="List", required=true, value="产品id")
    })
    @PostMapping(value = {"/getImgUrlList"},consumes = "multipart/form-data")
    @ResponseBody
    public ReturnT<List<ImgInfoReqVo>> getProductSearchInfo(@RequestParam("ids")List<String> ids){
        List<ImgInfoReqVo> list = productFileInfoService.getTifAndJpgByIds(ids);
        return ReturnT.success(list);
    }

    @GetMapping(value = "/downLoad")
    @ApiOperation(value = "下载文件，以及下载文件记录",consumes = MediaType.ALL_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="ids", dataType="String", required=true, value="id 拿逗号分隔开"),
            @ApiImplicitParam(paramType="String", name="types", dataType="String", value="文件类型,逗号分隔开")
    })
    public void downloadNotOpen(@Validated @Length(min = 1, message = "id不能为空") @RequestParam(name = "ids") String ids,
                                                                 @RequestParam(value = "types",required = false) String types, HttpServletResponse response) {
        String path = productFileInfoService.getDownLoadPathByIdList(ids,types);
        log.info("下载文件路径：[{}]",path);
        if(StringUtils.isNotBlank(path)){
            FileDownLoadUtils.downloadFile(path,response);
        }

    }

    @GetMapping(value = "/downLoadGif")
    @ApiOperation(value = "下载动图")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="String", name="ids", dataType="String", required=true, value="id 拿逗号分隔开"),
    })
    public void downLoadGif(@Validated @Length(min = 1, message = "id不能为空") @RequestParam(name = "ids") String ids,HttpServletResponse response) {
        String path = productFileInfoService.getDownLoadGifPathByIdList(ids);
        if(StringUtils.isNotBlank(path)){
            FileDownLoadUtils.downloadFile(path,response);
        }
    }



}
