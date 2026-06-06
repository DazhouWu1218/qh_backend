package com.njht.webyun.system.service.impl;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.constant.RoleKeys;
import com.njht.webyun.system.dao.mapper.SysRoleMapper;
import com.njht.webyun.system.dao.mapper.SysUserMapper;
import com.njht.webyun.system.model.sysRole.RoleMenuFun;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.model.sysFun.FunRole;
import com.njht.webyun.system.model.sysRole.UserRoleQuery;
import com.njht.webyun.system.model.base.BeanProperty.Num;
import com.njht.webyun.system.model.sysRole.RoleQuery;
import com.njht.webyun.system.model.sysRole.SysRole;
import com.njht.webyun.system.service.inf.SysRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/*
*
 * @author ：David
 * @date ：Created in 2019/11/25 19:54
 * @description：
 * @modified By：
 * @version: $
*/


@Service
@Component
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    SysRoleMapper roleMapper;

    @Autowired
    SysUserMapper userMapper;

    @Override
    public Page<SysRole> showRoles(RoleQuery query) throws Exception {
        PageHelper.startPage(query.getPage(), query.getRows());
        Page<SysRole> list = roleMapper.getRoles(query);
        return list;
    }

    @Override
    public void addRole(SysRole role) throws Exception {

        //1.检查参数
        if (!checkRole(role, Num.ONE)) {
//            throw new CommMsgException("角色名或机构id不能为空");
            throw new CommMsgException(MapUtil.get(RoleKeys.ROLENAME_ORGID_EXISTS));
        }
        // 2.查看是否有此角色名存在
        List<SysRole> rolesList = roleMapper.selectRoleByName(role);
        if (rolesList!=null && rolesList.size()>0) {
            throw new CommMsgException("角色名存在");
        }
        //3.获取创建者信息
        Date now = new Date();
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        //4.封装其他信息
        role.setCreatedBy(currentUserId);
        role.setCreatedDate(now);
        role.setLastUpdatedBy(currentUserId);
        role.setLastUpdatedDate(now);
        role.setDeleted(0);
        if(Num.ZERO == roleMapper.addRole(role)){
//            throw new CommMsgException("插入0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }
    }

    @Override
    public void editRole(SysRole role) throws Exception {
        //1.检查参数
        if (!checkRole(role, Num.TWO)) {
//            throw new CommMsgException("角色名或角色id不能为空");
            throw new CommMsgException(MapUtil.get(RoleKeys.ROLENAME_ROLEID_EXISTS));
        }
        // 2.查看是否有此角色名存在
        int count = roleMapper.selectCountByNameAndOrgId(role);
        if (count > 0) {
//            throw new CommMsgException("角色名和角色中文名已存在");
            throw new CommMsgException(MapUtil.get(RoleKeys.ROLENAME_ROLECHINESENAME_EXISTS));
        }
        //3.获取创建者信息
        Date now = new Date();
        int currentUserId = UserUtil.getCurrentUser().getUserId();
        //4.封装其他信息
        role.setLastUpdatedBy(currentUserId);
        role.setLastUpdatedDate(now);
        role.setPk_roleId(role.getRoleId());
        if(Num.ZERO == roleMapper.updateByPrimaryKeySelective(role)){
//            throw new CommMsgException("修改0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }
    }

    @Override
    public void deleteRoles(List<Integer> list) throws Exception{

        for (int roleId : list)
        {
            roleMapper.deleteRoleFun(roleId);
            roleMapper.deleteRoleUser(roleId);
            roleMapper.deleteRole(roleId);
        }
    }

    /*
         * 给角色设置功能
         * @param menuRole
         * @throws Exception
    */

    @Override
    public void addRoleMenu(FunRole funRole) throws Exception{
        //先删除
        roleMapper.deleteRoleFun(funRole.getRoleId());

        if (null != funRole.getFunIdList() &&  funRole.getFunIdList().size() > 0)
        {
            for (int funId : funRole.getFunIdList())
            {
                funRole.setFunId(funId);
                roleMapper.insertRoleMenu(funRole);
            }
        }
    }

    @Override
    public void createRoleUser(int roleId, List<Integer> userIdlist) throws Exception{
        roleMapper.deleteRoleUser(roleId);

        if (null != userIdlist && userIdlist.size() > 0) {
            roleMapper.insertRoleUser(roleId,userIdlist);
        }
    }

    @Override
    public List<Map<String, Object>> getUsersByRoleId(int roleId) throws Exception{
        return userMapper.selectUsersByRoleId(roleId);
    }

    @Override
    public Page<List<Map<String, Object>>> getUsersExceptByPage(UserRoleQuery model) {
        PageHelper.startPage(model.getPage(), model.getRows());

        Map<String, Object> map = new HashMap<String, Object>(8);
        String excep = model.getNotIn();
        List<Integer> excepteds = new ArrayList<Integer>();
        if (excep != null) {
            List<String> lis = Arrays.asList(excep.split(","));

            for (int i = 0; i < lis.size(); i++) {
                excepteds.add(Integer.parseInt(lis.get(i)));
            }
        }

        map.put("excepteds", excepteds);
        map.put("username", StringUtils.trimAllWhitespace(model.getUsername()));
        map.put("orgId", model.getOrgId());
        map.put("containSub", model.isContainSub());
        return userMapper.selectUsersExceptByPage(map);
    }

    private Boolean checkRole(SysRole role, int type) {
        if (Num.ONE == type) {
            if (StringUtils.isEmpty(role.getRoleName()) || StringUtils.isEmpty(role.getOrgId())) {
                return false;
            }
        }else if(Num.TWO == type){
            if (StringUtils.isEmpty(role.getRoleName()) || StringUtils.isEmpty(role.getRoleId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> queryRolesByUserId(int userId)
    {
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<SysRole> selectRoleByOrgId(int orgId){
        List<SysRole> sysRoles = new ArrayList<>();
        SysRole role = new SysRole();
        role.setRoleId(-1);
        role.setRoleName("全部");
        sysRoles.add(role);
        sysRoles.addAll(roleMapper.selectRoleByOrgId(orgId));
        return sysRoles;
    }

    @Override
    public void setMenuFun(RoleMenuFun roleMenuFun) {
        //先删除
        roleMapper.deleteRoleMenu(roleMenuFun.getRoleId());
        roleMapper.deleteRoleFun(roleMenuFun.getRoleId());

        if (null != roleMenuFun.getMenuList() &&  roleMenuFun.getMenuList().size() > 0)
        {
            roleMapper.addRoleMenu(roleMenuFun);
        }
        if (null != roleMenuFun.getFunList() &&  roleMenuFun.getFunList().size() > 0)
        {
            roleMapper.addRoleFun(roleMenuFun);
        }
    }

}
