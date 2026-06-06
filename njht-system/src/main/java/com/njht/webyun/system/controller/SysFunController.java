package com.njht.webyun.system.controller;


import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.model.sysFun.FunMoveModel;
import com.njht.webyun.system.model.sysFun.SysFun;
import com.njht.webyun.system.model.sysFunUrl.SysFunUrl;
import com.njht.webyun.system.model.sysUrl.SysUrlQuery;
import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.njht.webyun.system.service.inf.SysFunService;
import com.njht.webyun.system.service.inf.SysUrlService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ：David
 * @date ：Created in 2020/4/23 10:06
 * @description：系统功能管理
 * 1、新增、修改、删除菜单时，复制操作数据到功能表中
 * 2、功能模块中，只能对功能进行操作，禁止菜单的操作
 * 3、取消url设置功能
 * 4、新增的功能是从字典中配置项获取的，这样减少手动配置功能模块与前端页面定义的功能值不一致情况发生
 * @modified By：
 * @version: $
 */
@Api(tags = "功能管理",value= "funmanger")
@RestController
@RequestMapping(value = "/system/fun")
public class SysFunController {
    private static final Logger logger = LoggerFactory.getLogger(SysFunController.class);
    @Autowired
    SysFunService sysFunService;
    @Autowired
    SysUrlService sysUrlService;
    
    /**
     * 获取功能树
     * @return
     */
    @PostMapping(value = "/tree")
    @ApiOperation("获取功能树")
    public List<Map<String, Object>> getFunTreeJson() throws Exception
    {
        return  sysFunService.getFunsByParentId(UserUtil.getCurrentUser().getUserId(), 0);
    }

    /**
     * 新增功能
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增功能")
    public void newFun(@RequestBody SysFun fun) throws Exception
    {
        sysFunService.createFun(fun);
    }
    
    /**
     * 编辑功能
     * @param fun
     * @return
     */
    @PostMapping(value = "/edit")
    @ApiOperation("编辑功能")
    public void editFun(@RequestBody SysFun fun) throws Exception
    {
        sysFunService.editSysFun(fun);
    }

    /**
     * @return
     */
    @PostMapping(value = "/delete")
    @ApiOperation("删除功能")
    public void removeFun(@RequestBody SysFun fun) throws Exception{
        sysFunService.removeSysFun(fun.getFunId());
    }

    /**
     * @param funMoveModel
     * @return
     */
    @PostMapping(value = "/move")
    @ApiOperation("拖动修改功能树")
    public void moveFun(@RequestBody FunMoveModel funMoveModel) throws Exception
    {
        sysFunService.moveSysFun(funMoveModel);
    }

    @PostMapping(value = "/select")
    @ApiOperation("查询所有功能")
    public List<Map<String, Object>> selectAllFuns()
    {
        List<Map<String, Object>> functions = sysFunService.getFunctionsForTree( UserUtil.getCurrentUser().getUserId(), 1);
        if(functions == null || functions.size()==0){
//                return RespBean.error("未获取到功能信息");
            throw new CommMsgException(MapUtil.get(CommonKey.NOT_GET_INFORMATION));
        }
        return functions;
    }

    @PostMapping("/existsUrl")
    @ApiOperation("查询功能已选URL")
    public List<SysUrl> getUsersByRoleId(@RequestBody SysFunUrl sysFunUrl) throws Exception{
        return sysUrlService.getUrlsByFunId(sysFunUrl.getFunId());
    }

    @PostMapping(value = "/excepted")
    @ApiOperation("查询除某些URL外的所有URL")
    public Page<SysUrl> getUrlsExcepted(@RequestBody SysUrlQuery query) throws Exception
    {
        return sysUrlService.getUrlsExceptByPage(query);
    }

    @PostMapping(value = "/bindUrl")
    @ApiOperation("功能绑定URL 参数：{\"funId\":1,\"urls\":[{\"urlId\":1}]}")
    public void bindUrl(@RequestBody SysFun sysfun) throws Exception
    {
        sysUrlService.bindUrl(sysfun);
    }

}
