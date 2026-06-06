package com.njht.webyun.system.controller;


import com.njht.webyun.common.RespBean;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.constant.OrgKeys;
import com.njht.webyun.system.model.sysOrg.OrgExportModel;
import com.njht.webyun.system.model.sysOrg.SysOrg;
import com.njht.webyun.system.service.inf.SysOrgService;
import com.njht.webyun.utils.ExcelUtil;
import com.njht.webyun.utils.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "机构管理",value= "orgmanger")
@RestController
public class SysOrgController {
	
	private final static Logger logger = LoggerFactory.getLogger(SysOrgController.class);

	
	@Autowired
	SysOrgService orgService;
	
    @PostMapping(value = "/system/orgs")
    @ApiOperation("获取机构树")
    public List<Map<String, Object>> getOrgsInTreeJson()
    {
		return orgService.getOrgs();
    }

	@PostMapping(value = "/system/departments")
	@ApiOperation("获取组织机构树，数管系统使用")
	public List<Map<String, Object>> getDepartmentsInTreeJson()
	{
		return orgService.getDepartments();
	}


	@PostMapping(value = "/system/orgs/add")
	@ApiOperation("新增机构")
	public void newOrg(@RequestBody SysOrg org) throws Exception
	{
		orgService.addOrg(org);
	}



	@PostMapping(value = "/system/orgs/edit")
	@ApiOperation("编辑机构")
	public void editOrg(@RequestBody SysOrg org) throws Exception
	{
		orgService.editOrg(org);
	}

	@PostMapping(value = "/system/orgs/delete")
	@ApiOperation("删除机构")
	public void removeOrg(@RequestBody SysOrg org) throws Exception{
		orgService.removeOrg(org.getOrgId());
	}

/*	@PostMapping(value = "/system/orgs/move")
	@ApiOperation("拖动修改机构树")
	public RespBean moveOrg(@RequestBody OrgMoveModel orgMoveModel)
	{
		try {
			if( !checkOrgMoveModel(orgMoveModel) ){
				logger.error("移动机构树失败：参数格式异常");
				return RespBean.error("移动机构树失败，参数格式异常");
			}
			int retNum = orgService.moveOrg(orgMoveModel);
			if(Num.ZERO == retNum){
				logger.error("移动机构树失败：修改0条数据");
				return RespBean.error("修改0条数据");
			}
		} catch (Exception e) {
			logger.error("移动机构树失败：" + e.getMessage());
			return RespBean.error("移动机构树失败");
		}
		return RespBean.ok("OK");
	}*/


	@PostMapping(value = "/system/orgs/{orgId}/parent/{parentId}/{parentLevelNum}")
	public void moveOrg(@PathVariable("orgId") int orgId, @PathVariable("parentId") int parentId,
						@PathVariable("parentLevelNum") int parentLevelNum,@RequestBody Map<String,Object> pMap)
	{
		orgService.moveOrg(orgId, parentId,parentLevelNum,pMap);
	}

/*	boolean checkOrgMoveModel(OrgMoveModel model){
		if(model.getOrg() == null || model.getInheritedId() == null ||  model.getInheritedName() == null ||
				model.getOrgId() == null || model.getParentId() == null || model.getParentLevelNum() == null){
			return false;
		}
		return true;
	}*/



    /**
     * 导出列表
     */
    @GetMapping(value = "/system/orgs/export")
    @ApiOperation("导出机构列表")
    public void exportOrg(@Param("orgId") int orgId, HttpServletResponse response){
		logger.info(">>>>>>开始导出机构列表");
    	 long start = System.currentTimeMillis();
         try {
			List<OrgExportModel> list = orgService.exportOrg(orgId);
			logger.info("导出机构列表excel所花时间：" + (System.currentTimeMillis() - start)+"ms");
			ExcelUtil.exportExcel(list, "机构信息表", "机构信息", OrgExportModel.class, "机构信息", response);
		} catch (Exception e) {
			logger.error(MapUtil.get(OrgKeys.ORG_EXPORT_FAIL)+"：" ,e);
		}
 		logger.info(">>>>>>导出机构列表完成");
    }


    /**
     * 导入机构列表模板信息
     * @param file
     */
    @PostMapping(value = "/system/orgs/import")
    @ApiOperation("导入机构列表")
    public void importExcel(@RequestParam("file") MultipartFile file) {
		logger.info(">>>>>>开始导入机构列表");
		long start = 0;
		try {
			System.currentTimeMillis();
			orgService.importOrg(file);

		} catch (Exception e) {
			logger.error("导入机构表excel失败：{}" ,e);
//			return RespBean.error("导入机构表excel失败："+ e.getMessage());
			throw new CommMsgException(MapUtil.get(OrgKeys.ORG_IMPORT)+MapUtil.get(CommonKey.FAIL)+":"+ e.getMessage());
		}
		logger.info(">>>>>>导入机构列表完成，所花时间：" + (System.currentTimeMillis() - start)+"ms");
    }

	@GetMapping(value = "/system/orgs/Template")
	@ApiOperation("导出机构列表模板")
	public void getOrgTemplate(HttpServletResponse response){
		logger.info(">>>>>>开始导出机构列表模板");
		try {
			long start = System.currentTimeMillis();
			List<OrgExportModel> list = new ArrayList<OrgExportModel>();
			ExcelUtil.exportExcel(list, "机构信息表", "机构信息", OrgExportModel.class, "机构信息(模板)", response);
			logger.info("导出机构excel模板所花时间：" + (System.currentTimeMillis() - start)+"ms");
		} catch (Exception e) {
			logger.error("导出机构列表模板失败：" ,e);
		}
		logger.info(">>>>>>导出机构列表模板完成");
	}


//	@PostMapping(value = "/system/orgs/getAllHallOrgs")
//	@ApiOperation("获取所有网点机构")
//	public RespBean getAllHallOrgs()
//	{
//		List<SysOrg> list = null;
//		try {
//			list = orgService.getAllHallOrgs();
//			if(list == null || list.size()==0){
//                logger.error("未获取到网点机构");
//                return RespBean.error("获取网点机构失败");
//            }
//		} catch (Exception e) {
//			logger.error("获取网点机构失败："+e.getMessage());
//			return RespBean.error("获取网点机构失败,数据库处理异常");
//		}
//		return RespBean.ok("OK", list);
//	}



	@PostMapping(value = "/system/orgsbyorgid", headers = "accept=application/json")
	@ApiOperation("根据机构号获取机构树")
	public RespBean getOrgsByOrgidInTreeJson(@RequestBody SysOrg org)
	{
		/*logger.info(">>>>>>开始根据机构号获取机构树，查询条件："+ new Gson().toJson(org));
		List<Map<String, Object>> list = orgService.getOrgs(org.getOrgId());
		if(list == null || list.size()==0){
			logger.error("未获取到机构树");
			return RespBean.error("获取机构树失败");
		}
		logger.info(">>>>>>根据机构号获取机构树完成");
		return RespBean.ok("OK", list);*/
		return null;
	}

	@PostMapping(value = "/system/orgs/sense")
	@ApiOperation("获取机构树(增加VIP人数)")
	public List<Map<String, Object>> getOrgsAndVipInTree()
	{
		List<Map<String, Object>> list = orgService.getOrgsAndVip();
		if(list == null || list.size()==0){
			logger.error("未获取到机构树(含VIP)");
//			return RespBean.error("获取机构树失败(含VIP)");
			throw new CommMsgException(MapUtil.get(OrgKeys.ORG_TREE_QUERY_FAIL));
		}
		return list;
	}

//	@PreAuthorize("hasRole('ROLE_生态管理员')")
//	@PreAuthorize("hasPermission('/system/orgs/getOrgById','sys:org:info')")
	@GetMapping("/system/orgs/getOrgById")
	@ApiOperation("通过机构Id获取机构信息")
	public SysOrg getOrgById(@Param("orgId") int orgId){
		return orgService.getOrgById(orgId);
	}


	@GetMapping("/system/orgs/queryOrgsByParentId")
	@ApiOperation("通过父机构Id获取所有子机构信息")
	public List<Map<String, Object>> queryOrgsByParentId(@Param("parentOrgId") int parentOrgId){
		return orgService.queryOrgsByParentId(parentOrgId);
	}

	@GetMapping("/system/orgs/queryOrgsForTree")
	@ApiOperation("通过机构Id获取机构信息")
	public Map<String, Object> queryOrgsForTree(@Param("parentOrgId") int parentOrgId){
		return orgService.selectOrgsForTree(parentOrgId);
	}

	@GetMapping("/system/orgs/getOrgByCode")
	@ApiOperation("通过机构Code获取机构信息")
	public Map<String, Object> getOrgByCode(@Param("orgCode") String orgCode){
		return orgService.getOrgByCode(orgCode);
	}

	@GetMapping("/system/orgs/queryChildOrgByOrgId")
	@ApiOperation("通过机构id获取子机构信息")
	public List<SysOrg> queryChildOrgByOrgId(@Param("orgId") int orgId){
		return orgService.queryChildOrgByOrgId(orgId);
	}

	@GetMapping("/system/orgs/selectOrgList")
	@ApiOperation("获取机构列表（名称与ID）")
	public List<SysOrg> selectOrgList(){
		return orgService.selectOrgIdAndName();
	}

}
