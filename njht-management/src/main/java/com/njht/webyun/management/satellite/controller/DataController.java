package com.njht.webyun.management.satellite.controller;

import com.njht.webyun.management.satellite.entity.HthtDmsSateDataInfoVO;
import com.njht.webyun.management.satellite.service.HthtDmsSateDataFileInfoService;
import com.njht.webyun.management.satellite.service.HthtDmsSateDataInfoService;
import com.njht.webyun.management.satellite.vo.PageInfo;
import com.njht.webyun.management.satellite.vo.SelectParam;
import com.njht.webyun.management.satellite.vo.SelectThematicParam;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lmd
 * @describe 数据检索接口
 */
@Api(tags = "数据检索-卫星数据")
@RestController
public class DataController {

    @Autowired
    private HthtDmsSateDataInfoService hthtDmsSateDataInfoService;

    @Autowired
    private HthtDmsSateDataFileInfoService hthtDmsSateDataFileInfoService;

    /**
     * 查询卫星名称树结构
     * @return
     */
    @GetMapping(value = "/data/selectSateField")
    @ResponseBody
    public ReturnT<Object> selectSateField() {
        ReturnT<Object> returnT = new ReturnT<Object>();
        try {
            List<Map<String,Object>> mapList = hthtDmsSateDataInfoService.selectSateField();
            returnT.setCode(ReturnT.SUCCESS_CODE);
            returnT.setData(mapList);
            returnT.setMsg("数据查询成功");
        } catch (Exception e) {
            returnT.setCode(ReturnT.FAIL_CODE);
            returnT.setData(null);
            returnT.setMsg("数据查询发生异常！！");
        }
        return returnT;
    }

    /**
     * 数据信息查询
     *
     * @param selectParam
     * @return
     */
    @ApiOperation(value = "检索数据")
    @RequestMapping(value = "/data/selectPaged", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<Object> selectPaged(@RequestBody SelectParam selectParam) {
        ReturnT<Object> returnT = new ReturnT<Object>();
        if (StringUtils.isEmpty(selectParam.getSort()) || selectParam.getCloud() == null
                || selectParam.getBeginTime() == null || selectParam.getEndTime() == null
                || selectParam.getSateSensorId() == null || selectParam.getSateSensorId().size() <= 0) {
            returnT.setCode(ReturnT.FAIL_CODE);
            return returnT;
        }
        PageInfo<HthtDmsSateDataInfoVO> pageInfo = hthtDmsSateDataInfoService.selectPagedOrderF(selectParam.getPageNum(), selectParam.getPageSize(), selectParam.getResolution(), selectParam.getBeginTime(), selectParam.getEndTime(), selectParam.getSort(), selectParam.getSateSensorId(), selectParam.getCloud(), selectParam.getDataType(), selectParam.getGeo(), selectParam.getRegionId());
        returnT.setData(pageInfo);
        returnT.setCode(ReturnT.SUCCESS_CODE);
        return returnT;
    }
    /**
     * 卫星数据下的专题数据信息查询
     * @param selectThematicParam
     * @return
     */
    @RequestMapping(value = "/data/selectThematicPaged", method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<Object> selectThematicPaged(@RequestBody SelectThematicParam selectThematicParam) {
        ReturnT<Object> returnT = new ReturnT<Object>();
        PageInfo<Map<String,Object>> pageInfo = hthtDmsSateDataInfoService.selectThematicPaged(selectThematicParam);
        returnT.setData(pageInfo);
        returnT.setCode(ReturnT.SUCCESS_CODE);
        return returnT;
    }

    @RequestMapping("/data/getGeoArea")
    public ReturnT<Map<String, Object>> getGeoArea(@RequestBody SelectParam selectParam) {
        ReturnT<Map<String, Object>> returnT = new ReturnT<>();
        if (selectParam.getCloud() == null
                || selectParam.getBeginTime() == null || selectParam.getEndTime() == null
                || selectParam.getSateSensorId() == null || selectParam.getSateSensorId().isEmpty()) {
            returnT.setCode(ReturnT.FAIL_CODE);
            return returnT;
        }
        Map<String, Object> map = hthtDmsSateDataInfoService.areageo(selectParam.getResolution(), selectParam.getBeginTime(), selectParam.getEndTime(), selectParam.getSateSensorId(), selectParam.getCloud(), selectParam.getDataType(), selectParam.getGeo(), selectParam.getRegionId());
        returnT.setCode(ReturnT.SUCCESS_CODE);
        returnT.setData(map);
        return returnT;
    }


}
