package com.njht.webyun.system.controller;

import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.system.model.sysDic.SysDic;
import com.njht.webyun.system.model.sysDic.SysDicQuery;
import com.njht.webyun.system.model.sysDic.SysDicValueQuery;
import com.njht.webyun.system.service.inf.SysDicService;
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

/**
 * @author ：David
 * @date ：Created in 2019/12/9 10:28
 * @description：系统字典管理
 * @modified By：
 * @version: $
 */
@RestController
@RequestMapping("/system/dic")
@Api(tags = "字典管理",value= "SysDicController")
public class SysDicController {

    private final static Logger logger = LoggerFactory.getLogger(SysDicController.class);
    
    @Autowired
    SysDicService sysDicService;

    @PostMapping("/show")
    @ApiOperation("分页查询字典")
    public Page<SysDic> getSysDics(@RequestBody SysDicQuery query){
        Page<SysDic> list = null;
        try {
            list = sysDicService.showSysDics(query);
            if(list == null){
                logger.error("查询字典失败");
                throw new CommMsgException("查询字典失败");
            }
        } catch (Exception e) {
            logger.error("查询字典失败："+e.getMessage());
            throw new CommMsgException("查询字典失败，数据库处理异常");
        }
        logger.info(">>>>>>分页查询字典完成");
        return list;
    }

    @PostMapping("/add")
    @ApiOperation("新增字典")
    public void addSysDic(@RequestBody List<SysDic> sysDicList ){
        try {
            sysDicService.addSysDic(sysDicList);
        }catch (CommMsgException c){
            logger.error("新增字典失败："+c.getMessage());
            throw new CommMsgException("新增字典失败"+c.getMessage());
        }catch(Exception e) {
            logger.error("新增字典失败："+e.getMessage());
            throw new CommMsgException("新增字典失败"+e.getMessage());
        }
        logger.info(">>>>>>新增字典完成");
    }

    @PostMapping("/edit")
    @ApiOperation("编辑字典")
    public void editSysDic(@RequestBody List<SysDic> sysDicList){
        try {
            sysDicService.editSysDic(sysDicList);

        }catch (CommMsgException c){
            logger.error("编辑字典失败："+c.getMessage());
            throw new CommMsgException("编辑字典失败"+c.getMessage());
        }catch (Exception e) {
            logger.error("编辑字典失败："+e.getMessage());
            throw new CommMsgException("编辑字典失败");
        }
        logger.info(">>>>>>编辑字典完成");
    }


    @PostMapping("/delete")
    @ApiOperation("批量删除字典")
    public void deleteSysDic(@RequestBody List<SysDic> list ){
        try {
            sysDicService.deleteSysDics(list);
        } catch (Exception e) {
            logger.error("删除字典失败："+e.getMessage());
            throw new CommMsgException("删除字典失败");
        }
        logger.info(">>>>>>删除字典完成");
    }

    @PostMapping("/showValues")
    @ApiOperation("分页查询字典<值>")
    public List<SysDic> getDicValues(@RequestBody SysDicValueQuery dicValueQuery){
        Page<SysDic> list = null;
        try {
            list = sysDicService.showSysDicValues(dicValueQuery);
            if(list == null){
                logger.error("查询字典<值>失败");
                throw new CommMsgException("查询字典<值>失败");
            }
        } catch (Exception e) {
            logger.error("查询字典<值>失败："+e.getMessage());
            throw new CommMsgException("查询字典<值>失败，数据库处理异常");
        }
        logger.info(">>>>>>分页查询字典<值>完成");
        return list;
    }

    @PostMapping("/getValuesByType")
    @ApiOperation("根据type查询字典<值>")
    public List<SysDic> getValuesByType(@RequestBody SysDic sysDic){
        List<SysDic> list = null;
        try {
            list = sysDicService.getValuesByType(sysDic);
            if(list == null){
                logger.error("查询字典<值>失败");
                throw new CommMsgException("查询字典<值>失败");
            }
        } catch (Exception e) {
            logger.error("查询字典<值>失败："+e.getMessage());
            throw new CommMsgException("查询字典<值>失败，数据库处理异常");
        }
        logger.info(">>>>>>根据type查询字典<值>信息完成");
        return list;
    }


}
