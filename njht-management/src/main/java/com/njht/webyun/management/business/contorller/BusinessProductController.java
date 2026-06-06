package com.njht.webyun.management.business.contorller;

import com.alibaba.fastjson.JSON;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.management.business.entity.ProductParam;
import com.njht.webyun.management.business.entity.ProductSelectForm;
import com.njht.webyun.management.business.service.BusinessProductService;
import com.njht.webyun.management.region.service.RegionInfoService;
import com.njht.webyun.utils.ReturnT;
import com.njht.webyun.utils.TreeBuilderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author dgj
 */
@RestController
@RequestMapping("ywProduct")
@Api(tags = "数据检索-业务产品")
public class BusinessProductController {

    @Autowired
    private BusinessProductService businessProductService;

    @Autowired
    private RegionInfoService regionInfoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 查询四级业务产品信息
     * @param userId
     * @return
     */
    @ApiOperation(value = "业务产品数结构", notes = "业务产品数结构")
    @ApiImplicitParam(paramType="String", name="userId", dataType="String", required=true, value="用户id")
    @GetMapping("getProductTree")
    @ResponseBody
    public ReturnT<List<Tree>> getProductTree(@RequestParam("userId") String userId){
        List<Tree> productTreeList = businessProductService.findProductTreeByUid(userId);
        List<Tree> productTree = TreeBuilderUtil.buildTreeList(productTreeList);
        return ReturnT.success(productTree);
    }

    /**
     * 查询某一类型产品的周期、数据源信息
     * @param id
     * @return
     */
    @ApiOperation(value = "查询产品的周期、数据源信息", notes = "查询产品的周期、数据源信息")
    @ApiImplicitParam(paramType="String", name="id", dataType="String", required=true, value="产品id")
    @GetMapping("getCycleAndDataSource")
    @ResponseBody
    public ReturnT<Object> getCycleAndDataSource(@RequestParam("id") String id){
        ProductSelectForm form = businessProductService.getCycleAndDataSourceById(id);
        return ReturnT.success(form);
    }

    /**
     * 查询具体产品不同期次不同产品的信息
     * @param productParam
     * @return
     */
    @ApiOperation(value = "查询产品结果集", notes = "查询产品结果集")
    @PostMapping("getListInfo")
    @ResponseBody
    public ReturnT<Object> getProductDetails(@Validated @RequestBody ProductParam productParam){
        ReturnT returnT=new ReturnT<String>();
        if(productParam.getPageNum()==null){
            productParam.setPageNum(1);
        }
        if(productParam.getPageSize() == null || productParam.getPageSize() == 0 ){
            productParam.setPageSize(5);
        }
        //regionId
        String regionId = productParam.getRegionId();
        List<String> regions = new ArrayList<>();
        if(regionId!= null && !("".equals(regionId)) && productParam.getGeo() == null){
            regions.add(regionId);
        }else if(productParam.getGeo() !=null && productParam.getGeo().size() !=0  ){
            //对geo图进行处理
            regions = regionInfoService.getRegionByGeo(productParam.getGeo());
            if(regions.isEmpty() ){
                returnT.setMsg("没有数据");
                Map<String,Object> map = new HashMap<>(3);
                map.put("list",new ArrayList<>());
                map.put("total",0);
                map.put("size",0);
                returnT.setCode(ReturnT.SUCCESS_CODE);
                returnT.setData(map);
                return returnT;
            }
        }else{
            regions.add("1000000");
        }
        return businessProductService.getProjectInfos(productParam,regions);
    }



    /**
     * 查询父级id
     * @param id
     * @return
     */
    @ApiOperation(value = "查询父级id")
    @GetMapping("getParentIdList")
    @ResponseBody
    public ReturnT<Object> getParentIdList(@RequestParam("id") String id){
        return businessProductService.getParentListInfo(id);
    }
    /**
     * 模糊查询
     * @param id  用户id
      * @return
     */
    @ApiOperation(value = "业务产品模糊查询", notes = "业务产品模糊查询")
    @ApiImplicitParam(paramType="String", name="id", dataType="String", required=true, value="用户id")
    @GetMapping("getProductList")
    @ResponseBody
    public ReturnT<Object> getProductList(@RequestParam("id") String id){
        ReturnT<Object> returnT  = new ReturnT<>();
        String productStr = stringRedisTemplate.opsForValue().get("ywProduct::"+id);
        if(productStr == null){
            List<Object> productList = businessProductService.getProductList(id);
            productStr = JSON.toJSONString(productList);
            stringRedisTemplate.opsForValue().set("ywProduct::"+id,productStr,1, TimeUnit.HOURS);
        }
        returnT.setData(JSON.parseObject(productStr, List.class));
        return returnT;
    }

    /**
     * 气温快试图
     * @param id
     * @return
     */
    @GetMapping("getPngInfo")
    @ResponseBody
    public ReturnT<Object> getPngInfo(@RequestParam("id") String id){
        return businessProductService.getPngInfo(id);
    }
}
