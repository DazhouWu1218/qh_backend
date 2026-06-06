package com.njht.webyun.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.system.config.RedisService;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.constant.OrgKeys;
import com.njht.webyun.system.dao.mapper.SysOrgMapper;
import com.njht.webyun.system.model.base.BeanProperty.Bean;
import com.njht.webyun.system.model.base.BeanProperty.Num;
import com.njht.webyun.system.model.base.BeanProperty.OrgBean;
import com.njht.webyun.system.model.sysOrg.OrgExportModel;
import com.njht.webyun.system.model.sysOrg.OrgImportModel;
import com.njht.webyun.system.model.sysOrg.SysOrg;
import com.njht.webyun.system.service.inf.SysOrgService;
import com.njht.webyun.utils.ExcelUtil;
import com.njht.webyun.utils.MapUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Component
public class SysOrgServiceImpl implements SysOrgService {

	@Autowired
	SysOrgMapper orgMapper;

	@Autowired
	private RedisService redisService;
	
    protected static final long MAXSIZE = 62914560;//上传文件大小限制
    private static final Logger logger = LoggerFactory.getLogger(SysOrgServiceImpl.class);
	@Value("${file.rootPath:#{\"E:\\imb\"}}") private String fileRoot;//存放路径

	private final String KEY = "ORG:";

	/**
	 * 根据orgId获取机构树
	 */
	@Override
	public List<Map<String, Object>> getOrgs() {
		Integer userId = UserUtil.getCurrentUser().getUserId();
		int orgId = UserUtil.getCurrentUser().getOrgId();
		List list = new ArrayList<Map<String, Object>>(1);
		String redisKey = KEY + orgId;
		if (!redisService.exists( redisKey)) {
			Map<String, Object> map = orgMapper.selectOrgsForTree(orgId);
			list.add(map);
			// 添加缓存
			redisService.set(redisKey, JSONObject.toJSONString(list));
		} else {
			// 直接从缓存里面拿
			String jsonString = (String) redisService.get(redisKey);
			list = JSON.parseObject(jsonString, List.class);
		}
        return list;
	}

	/**
	 * 获取组织机构树
	 */
	@Override
	public List<Map<String, Object>> getDepartments() {
		Map<String, Object> map = orgMapper.selectDepartmentsForTree();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(1);
		list.add(map);

		return list;
	}

	/**
	 * 新增机构
	 */
	@Transactional
	@Override
	public void addOrg(SysOrg org) throws Exception{

        if(!"".equals( validateOrg(org))){
        	throw new CommMsgException( validateOrg(org) );
        }
////		是否不为厅点（0-是厅点 1-不是）
//		if(!parentIsNotHall(org.getParentId())){
//        	throw new CommMsgException("网点机构下不能新增机构");
//		}

		//如果上级机构为空，代表增加的为显示最高层级（父ID为1）
		org.setParentId(StringUtils.isEmpty(org.getParentId())?1:org.getParentId());

        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();

        org.setDeleted(0);
        org.setCreatedBy(currentUserId);
        org.setCreatedDate(now);
        org.setLastUpdatedBy(currentUserId);
        org.setLastUpdatedDate(now);

        if(orgMapper.insertOrg(org) == 0){
//			throw new CommMsgException("新增了0条数据");
        	throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
        if(orgMapper.updateInheritedId(org) == 0){
//        	throw new CommMsgException("根据新增的orgid修改inheritedId失败");
			throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
	}


	/**
	 * 编辑机构
	 */
	@Override
	@Transactional
	public void editOrg(SysOrg org) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>(8);
		if(StringUtils.isEmpty(org.getOrgId())){
//        	throw new CommMsgException("orgid不能为空");
			throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
		}
		if(!"".equals( validateOrg(org))){
			throw new CommMsgException( validateOrg(org) );
		}
////		是否不为厅点（0-是厅点 1-不是）
//		if(!parentIsNotHall(org.getParentId())){
//			throw new CommMsgException("网点机构下不能新增机构");
//		}
		org.setInheritedName(StringUtils.trimAllWhitespace(org.getInheritedName()));
		org.setLastUpdatedBy(UserUtil.getCurrentUser().getUserId());
		org.setLastUpdatedDate(new Date());
		if(orgMapper.updateByPrimaryKeySelective(org) == 0){
//			throw new CommMsgException("修改了0条数据");
			throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
		}

	}
	/**
	 * 验证机构编码和机构名称
	 * @return
	 */
	private String validateOrg(SysOrg org) {

//		if(StringUtils.isEmpty(org.getParentId()) || StringUtils.isEmpty(org.getOrgCode())||StringUtils.isEmpty(org.getOrgName())){
		if(StringUtils.isEmpty(org.getOrgCode())||StringUtils.isEmpty(org.getOrgName())){
//			return"parent_id和org_name和org_code不能为空";
			return MapUtil.get(CommonKey.NOTNULL);
		}

		// 验证父机构下的子机构名称唯一
		if (orgMapper.selectCountByOrgNameAndParentId(org) > Num.ZERO) {
//			return "子机构的机构名称已存在";
			return MapUtil.get(OrgKeys.ORGNAME_EXISTS);
		}

		// 增加验证机构编码唯一
		if (orgMapper.selectCountByOrgCode(org) > Num.ZERO) {
//			return "机构编码已存在";
			return MapUtil.get(OrgKeys.ORGCODE_EXISTS);
		}

		return "";
	}

	/**
	 * 删除机构
	 */
    @Override
	@Transactional
    public void removeOrg(int orgId) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>(4);

        map.put(OrgBean.ORG_ID, orgId);
        map.put(Bean.DELETED, true);
        map.put(Bean.LAST_UPDATED_BY, UserUtil.getCurrentUser().getUserId());
        map.put(Bean.LAST_UPDATED_DATE, new Date());

		if (Num.ZERO == orgMapper.deleteOrg(map)) {
			throw new CommMsgException(MapUtil.get(CommonKey.DELETE_FAIL));
		}
   }
	
    /**
     * 移动机构树
     */
    @Override
    @Transactional
    public void moveOrg(int orgId, int parentId, int parentLevelNum, Map<String, Object> pMap)
    {
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();
		// 更新排序号 更新其父节点的所有儿子的排序号 由前端传过来 map <String id,int sortNum>
        int i = 1;
		List<Map<String, Object>> list = (List<Map<String, Object>>) pMap.get("list");
		for (Map<String, Object> modelMap : list) {
			modelMap.put("sortNum",i++);
			orgMapper.updateOrgSortNum(modelMap);
		}
         
        //2.更新本机构的信息
        Map<String, Object> map = new HashMap<String, Object>(Num.FOUR);
        int levelNum = parentLevelNum + 1;
        map.put(OrgBean.PRIMARY_KEY, orgId);
        map.put(OrgBean.ORG_ID, orgId);
        map.put(OrgBean.PARENT_ID, parentId);
        map.put(OrgBean.INHERITED_ID, StringUtils.trimAllWhitespace((String) pMap.get("inheritedId")));
        map.put(OrgBean.INHERITED_NAME, StringUtils.trimAllWhitespace((String) pMap.get("inheritedName")));
        map.put(Bean.LAST_UPDATED_BY, currentUserId);
        map.put(Bean.LAST_UPDATED_DATE, now);
        map.put(OrgBean.LEVEL_NUM, levelNum);
        orgMapper.updateOrgParent(map);


         
        //3.更新本机构原始的子机构信息
        //先查询所有的每层子机构list
		List<Map<String, Object>> listChild = orgMapper.selectOrgsByParentId(orgId);
		updateChildren(listChild, map, currentUserId, now);
    }
	/**
	 * 移动机构树是更改所有子机构信息
	 * @param list
	 * @param parentOrgMap
	 * @param currentUserId
	 * @param now
	 */
	@SuppressWarnings("unchecked")
	private void updateChildren(List<Map<String, Object>> list, Map<String, Object> parentOrgMap, int currentUserId,
								Date now) {
		int levelNum = (Integer)parentOrgMap.get(OrgBean.LEVEL_NUM) + 1;
		int orgId = 0;
		String orgName = "";
		List<Map<String, Object>> children = null;

		for (Map<String, Object> m : list)
		{
			if (null != m.get("children"))
			{
				children = (List<Map<String, Object>>)m.get("children");
			}

			orgId = (Integer)m.get("id");
			orgName = (String)m.get("orgName");

			m.clear();
			m.put(OrgBean.ORG_ID, orgId);
			m.put(OrgBean.LEVEL_NUM, levelNum);
			m.put(OrgBean.INHERITED_ID,  StringUtils.trimAllWhitespace((String)parentOrgMap.get("inheritedId"))+"/"+orgId);
			m.put(OrgBean.INHERITED_NAME,  StringUtils.trimAllWhitespace((String)parentOrgMap.get("inheritedName"))+"/"+orgName);
			m.put(Bean.LAST_UPDATED_BY, currentUserId);
			m.put(Bean.LAST_UPDATED_DATE, now);

			// 更新所有子节点   层次,inheritedId,inheritedName
			orgMapper.updateOrgLevel(m);

			if (null != children && children.size() > 0)
			{
				updateChildren(children, m, currentUserId, now);
			}
		}
	}


	/**
	 * 根据当前用户orgId导出机构列表
	 */
	@Override
	@Transactional
	public List<OrgExportModel> exportOrg(int orgId) throws Exception {
//		int orgId = UserUtil.getCurrentUser().getOrgId();  因下载时不能传token，所以此处不能直接查orgId,需要前端传
		List<OrgExportModel> list = orgMapper.selectOrgsForTree4Export(orgId);
		return list;
	}





	/**
	 * 导入机构列表
	 */
	@Override
    @Transactional
	public void importOrg(MultipartFile file) throws Exception {
		List<OrgImportModel> personVoList = null;
		if (file != null) {
			long fileSize = file.getSize();
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			// 最大60M
			if (fileSize > MAXSIZE) {
//				throw new CommMsgException("文件大小限制60M");
				throw new CommMsgException(MapUtil.get(CommonKey.FILE_SIZE_LIMIT_60M));
			} else if (!".xls".equals(suffix) && !".xlsx".equals(suffix)) {
//				throw new CommMsgException("传入附件类型错误");
				throw new CommMsgException(MapUtil.get(CommonKey.FILE_FORMAT_ERROR));
			} else if (file.isEmpty()) {
//				throw new CommMsgException("传入附件为空");
				throw new CommMsgException(MapUtil.get(CommonKey.INCOMING_FILE_IS_EMPTY));
			}
			try {
				//解析excel收据
				personVoList = ExcelUtil.importExcel(file, 1, 1, OrgImportModel.class);

				logger.info("导入数量：" + personVoList.size());
				if(personVoList.size() == Num.ZERO){
//					throw new CommMsgException("excel文件中无有效数据");
					throw new CommMsgException(MapUtil.get(CommonKey.NO_VALID_DATA_IN_EXCEL));
				}

				int currentUserId = UserUtil.getCurrentUser().getUserId();
				Date now = new Date();
				String date = DateFormatUtils.format(now, "yyyyMMddHHmmss");
				String path = FilenameUtils.concat(fileRoot, "excel");
				path = FilenameUtils.concat(path, "import");
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				path = FilenameUtils.concat(path, "orgs" + date + ".xls");
				//上传excel文件  目录结构如：E:\vimas\excel\import\orgs20191119152327.xls
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path));

				//读取excel 存放到指定的 tmpList中
				String[] headers = { "机构代码", "机构名称", "父机构代码", "地址", "联系电话", "备注" };
				//增加电话校验
				String regex="(0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8})||(^[1][3-9][0-9]{9}$)";

				/*
				 * 取到根节点code
				 */
				String rootOrgCode = "";
				Map<String, Object> rootOrg = orgMapper.selectRootOrg();
				if (rootOrg != null) {
					rootOrgCode = (String) rootOrg.get(OrgBean.ORG_CODE);
				}

				/*
				 * 校验 tmpList 并组装成指定格式的list
				 */
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Set<String> orgCodeSet = new HashSet<String>();
				Set<String> orgNameSet = new HashSet<String>();
				int parentCodeCount = 0;
				int count = 0;
				for (OrgImportModel model : personVoList) {
					count++;
					Map<String, Object> orgMap = new HashMap<String, Object>();

					String orgCode = model.getOrgCode();
					if ("".equals(orgCode)) {
//						throw new CommMsgException("第" + count + "行" + headers[0] + "未填写");
						throw new CommMsgException(MapUtil.get(CommonKey.UNFILLED_INFORMATION_EXISTS));
					}
					if (orgCodeSet.contains(orgCode)) {
//						throw new CommMsgException("第" + count + "行" + headers[0] + "存在重复");
						throw new CommMsgException(MapUtil.get(CommonKey.DUPLICATE_CONTENT_EXISTS));
					}
					orgCodeSet.add(orgCode);
					orgMap.put(OrgBean.ORG_CODE, orgCode);

					// 机构名称
					String orgName = model.getOrgName();
					if ("".equals(orgName)) {
//						throw new CommMsgException("第" + count + "行" + headers[1] + "未填写");
						throw new CommMsgException(MapUtil.get(CommonKey.UNFILLED_INFORMATION_EXISTS));
					}
					if (orgNameSet.contains(orgName)) {
//						throw new CommMsgException("第" + count + "行" + headers[1] + "存在重复");
						throw new CommMsgException(MapUtil.get(CommonKey.DUPLICATE_CONTENT_EXISTS));
					}
					orgNameSet.add(orgName);
					orgMap.put(OrgBean.ORG_NAME, orgName);
					// 父机构代码 只能有一个根节点 即只能有一个没有父节点的
					String parentOrgCode = model.getParentOrgCode();
					if ("".equals(parentOrgCode)) {
						parentCodeCount++;

						if (!"".equals(rootOrgCode) && !rootOrgCode.equals(orgCode)) {
//							throw new CommMsgException("第" + count + "行总机构已存在");
							throw new CommMsgException(MapUtil.get(OrgKeys.PARENT_ORGCODE_EXISTS));
						}
						if (parentCodeCount > 1) {
//							throw new CommMsgException("第" + count+ "行" + headers[2] + "未填写,只能有一个总机构");
							throw new CommMsgException(MapUtil.get(CommonKey.UNFILLED_INFORMATION_EXISTS+MapUtil.get(OrgKeys.ONLY_BE_ONE)));
						}
					}
					orgMap.put(OrgBean.PARENT_CODE, parentOrgCode);
					// 地址
					orgMap.put(OrgBean.ADDRESS, model.getAddress());
					// 联系电话
					orgMap.put(OrgBean.TEL, model.getTel());
					// 备注
					orgMap.put(OrgBean.REMARK, model.getRemark());
					list.add(orgMap);
				}

				/*
				 * 根据list 更新数据库
				 */
				// Boolean flagBoolean = true;
				Map<String, Object> org = null;
				Map<String, Object> pOrg = null;
				if (list != null && list.size() > 0) {
					count = 0;
					for (Map<String, Object> orgMap : list) {
						SysOrg orgBean = new SysOrg();
						count++;
						//增加对联系电话的格式校验 20200515
						if(orgMap.get(OrgBean.TEL) != null && !"".equals((String)orgMap.get(OrgBean.TEL))){
							Pattern pattern= Pattern.compile(regex); //编译正则表达式
							Matcher matcher=pattern.matcher((String)orgMap.get(OrgBean.TEL)); //创建给定输入模式的匹配器
							if (!matcher.matches()){
//								throw new CommMsgException("第" + (count) + "行联系电话格式有误 "); PHONE_FORMAT_ERROR
								throw new CommMsgException(MapUtil.get(CommonKey.PHONE_FORMAT_ERROR));
							}
						}

						// 验证导入的数据中父机构值是否有效
						pOrg = null;
						if (!StringUtils.isEmpty(orgMap.get(OrgBean.PARENT_CODE))) {
							pOrg = orgMapper.selectOrgByOrgCode(
									StringUtils.trimAllWhitespace((String) orgMap.get(OrgBean.PARENT_CODE)));
							if (pOrg == null) {
								// throw new
								// Exception("第"+(count+1)+"行"+headers[2]+"不存在");
//								throw new CommMsgException("第" + (count) + "行" + headers[2] + "不存在 ");
								throw new CommMsgException(headers[2]+ MapUtil.get(CommonKey.INEXISTENCE));
							}
						}

						// 机构，如果机构已存在，则做更新
						org = orgMapper.selectOrgByOrgCode((String) orgMap.get(OrgBean.ORG_CODE));
						if (org == null) {
							// insert
							if (pOrg == null) {
/*								orgBean.setInheritedId("");
								orgBean.setParentInheritedId("");
								orgBean.setInheritedName((String)orgMap.get(OrgBean.ORG_NAME));
								orgBean.setLevelNum(1);
								orgBean.setSortNum(1);
								orgBean.setParentId(0);*/

//								throw new CommMsgException( "orgCode不能为空" );
								throw new CommMsgException(MapUtil.get(OrgKeys.ORGCODE_NOT_BE_EMPTY));
							} else {
								// orgModel.setInheritedId(pOrg.getInheritedId());

								// 增加对网点机构的父机构判断
								// 网点机构下不能再新增网点
								orgBean.setOrgId((Integer) pOrg.get(OrgBean.ORG_ID));
								SysOrg orgCheck = orgMapper.selectByPrimaryKey(orgBean.getOrgId());
//								if(0 == orgCheck.getIsNotHall() ){
//									throw new CommMsgException( "网点机构下不能再新增网点" );
//								}

								// 取父节点的子节点的最大排序号+1
								Integer maxSortNum = orgMapper
										.selectMaxSortNumByParentId((Integer) pOrg.get(OrgBean.ORG_ID));
								maxSortNum = maxSortNum == null ? 0 : maxSortNum;
								
								orgBean.setInheritedId("");
								orgBean.setParentInheritedId((String) pOrg.get(OrgBean.INHERITED_ID));
								orgBean.setInheritedName( pOrg.get(OrgBean.INHERITED_NAME) + "/" + orgMap.get(OrgBean.ORG_NAME) );
								orgBean.setLevelNum((Integer)pOrg.get(OrgBean.LEVEL_NUM) + 1);
								orgBean.setSortNum(maxSortNum + 1);
								orgBean.setParentId((int)pOrg.get(OrgBean.ORG_ID));
							}

							orgBean.setDeleted(0);
							orgBean.setCreatedBy(currentUserId);
							orgBean.setCreatedDate(now);
							orgBean.setLastUpdatedBy(currentUserId);
							orgBean.setLastUpdatedDate(now);
							orgBean.setOrgCode((String)orgMap.get(OrgBean.ORG_CODE));
							orgBean.setOrgName((String)orgMap.get(OrgBean.ORG_NAME));
							orgBean.setAddress((String)orgMap.get(OrgBean.ADDRESS));
							orgBean.setRemark((String)orgMap.get(OrgBean.REMARK));
							orgBean.setTel((String)orgMap.get(OrgBean.TEL));
//							if(orgMap.get(OrgBean.ISNOTHALL)!=null){
//								orgBean.setIsNotHall((Integer)orgMap.get(OrgBean.ISNOTHALL));
//							}

							orgMapper.insertOrg(orgBean);
							orgMapper.updateInheritedId(orgBean);

						} else {

							String oldInheritedIdString = (String) org.get(OrgBean.INHERITED_ID);

							// 更新本节点内容
							// org.put(Org.PRIMARY_KEY,
							// (Integer)org.get(Org.ORG_ID));
							org.put(OrgBean.ORG_ID, (Integer) org.get(OrgBean.ORG_ID));
							org.put(OrgBean.ADDRESS, orgMap.get(OrgBean.ADDRESS));
							org.put(OrgBean.TEL, orgMap.get(OrgBean.TEL));
							org.put(OrgBean.ORG_NAME, orgMap.get(OrgBean.ORG_NAME));
							if (pOrg != null) {

								// 增加对网点机构的父机构判断
								// 网点机构下不能再新增网点
								orgBean.setOrgId((Integer) pOrg.get(OrgBean.ORG_ID));
								SysOrg orgCheck = orgMapper.selectByPrimaryKey(orgBean.getOrgId());
//								if(0 == orgCheck.getIsNotHall() ){
//									throw new CommMsgException( "机构【"+orgCheck.getOrgCode()+"】为厅堂网点机构，其下不能再新增网点" );
//								}

								org.put(OrgBean.INHERITED_NAME,
										pOrg.get(OrgBean.INHERITED_NAME) + "/" + org.get(OrgBean.ORG_NAME));
								org.put(OrgBean.PARENT_ID, (Integer) pOrg.get(OrgBean.ORG_ID));
								org.put(OrgBean.INHERITED_ID,
										pOrg.get(OrgBean.INHERITED_ID) + "/" + org.get(OrgBean.ORG_ID));
							} else {
								org.put(OrgBean.INHERITED_NAME, org.get(OrgBean.ORG_NAME));
								org.put(OrgBean.PARENT_ID, new Integer(0));
								org.put(OrgBean.INHERITED_ID, org.get(OrgBean.ORG_ID).toString());
							}
//							org.put(OrgBean.ISNOTHALL,orgMap.get(OrgBean.ISNOTHALL));
							org.put(Bean.LAST_UPDATED_BY, currentUserId);
							org.put(Bean.LAST_UPDATED_DATE, now);
							orgMapper.updateOrg4Import(org);
							// update 子节点
							if (!oldInheritedIdString.equals((String) org.get(OrgBean.INHERITED_ID))) {
								List<Map<String, Object>> l = orgMapper
										.selectOrgsByParentId((Integer) org.get(OrgBean.ORG_ID));
								updateChildren(l, org, currentUserId, now);
							}
						}

					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
				throw new CommMsgException( e.getMessage() );
			}
		} else {
//			throw new CommMsgException("file为空" );
			throw new CommMsgException(MapUtil.get(CommonKey.INCOMING_FILE_IS_EMPTY));
		}
	}

	@Override
	public List<SysOrg> getAllHallOrgs() throws Exception {
		return orgMapper.getAllHallOrgs();
	}

//	//查看父机构是否不为网点机构  0-是厅点 1-不是
//	public boolean parentIsNotHall(int orgParentId){
//
//		SysOrg orgCheck = orgMapper.selectByPrimaryKey(orgParentId);
//
//		if(orgCheck == null){
//			return false;
//		}
//		return orgCheck.getIsNotHall()==1;
//	}

	/**
	 * 根据orgId获取机构树 含VIP
	 */
	@Override
	public List<Map<String, Object>> getOrgsAndVip() {
		Integer userId = UserUtil.getCurrentUser().getUserId();
		int orgId = UserUtil.getCurrentUser().getOrgId();
		Map<String, Object> map = orgMapper.selectOrgsAndVipForTree(orgId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(1);
		list.add(map);

		return list;
	}

/*	public static void main(String[] args) {
		String regex="(0\\d{2,3}[-]?\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|15[1089]\\d{8})||(^[1][3-9][0-9]{9}$)";
		Pattern pattern= Pattern.compile(regex); //编译正则表达式
		Matcher matcher=pattern.matcher("02700-11111112"); //创建给定输入模式的匹配器
		if (!matcher.matches()){
			System.out.println("错了");
		}else{
			System.out.println("yes");
		}
	}*/

	@Override
	public SysOrg getOrgById(int orgId) {
		return orgMapper.selectByPrimaryKey(orgId);
	}

	@Override
	public List<Map<String, Object>> queryOrgsByParentId(int parentOrgId) {
		return orgMapper.selectOrgsByParentId(parentOrgId);
	}

	@Override
	public Map<String, Object> selectOrgsForTree(int orgId){
		return orgMapper.selectOrgsForTree(orgId);
	}

	@Override
	public Map<String, Object> getOrgByCode(String code) {
		return orgMapper.selectOrgByOrgCode(code);
	}

	@Override
	public List<SysOrg> queryChildOrgByOrgId(int orgId){
		return orgMapper.getChildOrgByOrgId(orgId);
	}

	@Override
	public List<SysOrg> selectOrgIdAndName(){
		return orgMapper.selectOrgIdAndName();
	}
}
