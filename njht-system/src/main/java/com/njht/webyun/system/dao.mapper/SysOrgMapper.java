package com.njht.webyun.system.dao.mapper;

import com.njht.webyun.system.model.sysOrg.OrgExportModel;
import com.njht.webyun.system.model.sysOrg.SysOrg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SysOrgMapper {

	Map<String, Object> selectOrgsForTree(int orgId);

    int insertOrg(SysOrg org);

    int updateByPrimaryKeySelective(SysOrg org);

    /**
     * 验证同一机构下机构名称的唯一性
     * @return
     */
    int selectCountByOrgNameAndParentId(SysOrg org);

    /**
     * 验证机构编码的唯一性
     * @return
     */
    int selectCountByOrgCode(SysOrg org);

    int deleteOrg(Map<String, Object> map);

    /**
     * 移动机构树
     * @param map
     * @return
     */
    int updateOrgParent(Map<String, Object> map);

    List<OrgExportModel> selectOrgsForTree4Export(int orgId);

    /**
     * 根据orgId修改InheritedId
     * @return
     */
    int updateInheritedId(SysOrg org);

    /**
     * 拖拽机构时修改机构sortnum
     */
    void updateOrgSortNum(Map<String, Object> map);

    List<Map<String, Object>> selectOrgsByParentId(int orgId);

    void updateOrgLevel(Map<String, Object> m);

    /**
     * 查询机构根节点信息
     * @return
     */
    Map<String, Object> selectRootOrg();
    Map<String, Object> selectOrgByOrgCode(String string);
    Integer selectMaxSortNumByParentId(int orgId);
    void updateOrg4Import(Map<String, Object> org);
    SysOrg selectByPrimaryKey(int orgId);

    /**
     * 获取所有的网点型机构，包含经纬度
     * @return
     */
    List<SysOrg> getAllHallOrgs();

	Map<String, Object> selectOrgsAndVipForTree(int orgId);

    List<SysOrg> getChildOrgByOrgId(int orgId);

    List<SysOrg> selectOrgIdAndName();

    Map<String, Object> selectDepartmentsForTree();
}
