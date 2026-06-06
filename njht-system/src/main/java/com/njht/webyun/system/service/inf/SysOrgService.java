package com.njht.webyun.system.service.inf;


import com.njht.webyun.system.model.sysOrg.OrgExportModel;
import com.njht.webyun.system.model.sysOrg.SysOrg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SysOrgService {
	
	List<Map<String, Object>> getOrgs();

	public void addOrg(SysOrg org) throws Exception;

    public void editOrg(SysOrg org) throws Exception;

    public void removeOrg(int orgId) throws Exception;

//    public int moveOrg(OrgMoveModel orgMoveModel) throws Exception;
    void moveOrg(int orgId, int parentId,int parentLevelNum, Map<String,Object> pMap);

    public List<OrgExportModel> exportOrg(int orgId) throws Exception;

    public void importOrg(MultipartFile file) throws Exception;

    public List<SysOrg> getAllHallOrgs() throws Exception;

	List<Map<String, Object>> getOrgsAndVip();

    SysOrg getOrgById(int orgId);

    List<Map<String, Object>> queryOrgsByParentId(int parentOrgId);

    Map<String, Object> selectOrgsForTree(int orgId);

    Map<String, Object> getOrgByCode(String code);

    List<SysOrg> queryChildOrgByOrgId(int orgId);

    List<SysOrg> selectOrgIdAndName();

    List<Map<String, Object>> getDepartments();
}
