package com.njht.webyun.system.service.impl;

import com.njht.webyun.common.UserUtil;
import com.njht.webyun.enums.NumberEnum;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.dao.mapper.SysFunMapper;
import com.njht.webyun.system.dao.mapper.SysUrlMapper;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.constant.FunKeys;
import com.njht.webyun.system.model.base.BeanProperty.Num;
import com.njht.webyun.system.model.base.BeanProperty.Function;
import com.njht.webyun.system.model.sysFun.FunMoveModel;
import com.njht.webyun.system.model.sysFun.SysFun;
import com.njht.webyun.system.service.inf.SysFunService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ：David
 * @date ：Created in 2020/4/20 15:20
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Transactional
public class SysFunServiceImpl implements SysFunService {
    @Autowired
    protected SysFunMapper functionMapper;

    protected SysUrlMapper urlMapper;

    /**
     * 根据父ID获取功能树
     */
    @Override
    public List<Map<String, Object>> getFunsByParentId(int userId, int parentId) throws Exception
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("userId", userId);
        map.put("parentId", parentId);
        List<Map<String, Object>> list = functionMapper.selectFunsByParentId(map);
        return list;
    }

    /**
     * 新增功能
     */
    @Override
    public void createFun(SysFun model) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>(NumberEnum.NUMBER_8.getNum());
        validateFun(model, map);

        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();

        model.setCreatedBy(currentUserId);
        model.setCreatedDate(now);
        model.setLastUpdatedBy(currentUserId);
        model.setLastUpdatedDate(now);
        model.setDeleted(0);
        if(functionMapper.insertFun(model) == Num.ZERO){
//            throw new CommMsgException("功能表新增了0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
    }

    /**
     * 〈一句话功能简述〉
     *
     * @author David
     * @param model FunModel
     * @return String
     * @exception/throws [异常类型] [异常说明]（可选）
     * @see [类、类#方法、类#成员]（可选）
     * @since [起始版本]（可选）
     */
    @Override
    public void editSysFun(SysFun model) throws Exception
    {
     /*   Map<String, Object> map = new HashMap<String, Object>(NumberEnum.NUMBER_8.getNum());
        validateFun(model, map);*/

        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();
        model.setLastUpdatedDate(now);
        model.setLastUpdatedBy(currentUserId);
        model.setPk_funId(model.getFunId());
        if(functionMapper.updateFun(model) == Num.ZERO){
//            throw new CommMsgException("修改0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }

    }

    /**
     * 删除功能节点
     */
    @Override
    public void removeSysFun(int id) throws Exception
    {
        functionMapper.deleteFun(id);
    }

    /**
     * 更新子节点 更新子节点的层级关系
     */
    @Override
    public void moveSysFun(FunMoveModel model) throws Exception
    {
        int userId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();

        // 1.更新排序号 更新其父节点的所有儿子的排序号 由前端传过来的顺序，重新赋sortNum值
        List<SysFun> sysFunList = model.getSysfun();
        int i = 1;
        for (SysFun sysFun : sysFunList)
        {
            sysFun.setPk_funId(sysFun.getFunId());
            sysFun.setSortNum(i);
            sysFun.setLastUpdatedBy(userId);
            sysFun.setLastUpdatedDate(now);
            functionMapper.updateFunSortNum(sysFun);
            i++;
        }

        //2.更新功能本身的信息
        SysFun fun = new SysFun();
        fun.setPk_funId(model.getId());
        fun.setLastUpdatedBy(userId);
        fun.setLastUpdatedDate(now);
        fun.setParentId(model.getParentId());
        fun.setLevelNum(model.getParentLevelNum()+1);
        fun.setFunId(model.getId());
        functionMapper.updateSysFunSelf(fun);

        //3.更新本菜单原始的子菜单信息
        List<SysFun> mList = functionMapper.getFunIdByUserId(model.getId());
        updateChildren(mList, model.getParentLevelNum()+1, userId, now);


//        updateChildren(l, levelNum, currentUserId, now);
    }

/*    @Override
    @Transactional
    @CacheEvict(value = { Cache.PERMISSION_CACHE }, allEntries = true)
    public void createFunUrl(int funId, List<Integer> list)
    {
        functionMapper.deleteFunUrl(funId);

        if (null != list && list.size() > 0)
        {
            Map<String, Object> map = new HashMap<String, Object>(2);

            for (int urlId : list)
            {
                map.put(Function.FUN_ID, funId);
                map.put(Url.URL_ID, urlId);

                functionMapper.insertFunUrl(map);
            }
        }
    }*/


    /**
     * 〈一句话功能简述〉获取所有功能树，可用于角色授权
     */
    @Override
    public List<Map<String, Object>> getFunctionsForTree(int userId, int parentId)
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("userId", userId);
        map.put("parentId", parentId);
        List<Map<String, Object>> list = functionMapper.selectFunctionsForTree(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectFunContainCheck(int userId, int parentId,int roleId)
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("userId", userId);
        map.put("parentId", parentId);
        map.put("roleId", roleId);
        List<Map<String, Object>> list = functionMapper.selectFunContainCheck(map);
        return list;
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author David
     * @param list List<Map<String, Object>>
     * @param parentLevelNum int
     * @param currentUserId int
     * @param now Date
     */
    @SuppressWarnings("unchecked")
    private void updateChildren(List<SysFun> list, int parentLevelNum, int currentUserId, Date now)
    {
        int levelNum = parentLevelNum + 1;
        int funId = 0;
        List<SysFun> children = null;

        for (SysFun m : list)
        {
            if (null != m.getChildren())
            {
                children = m.getChildren();
            }
            m.setLevelNum(levelNum);
            m.setLastUpdatedBy(currentUserId);
            m.setLastUpdatedDate(now);

            // 更新所有子节点本身
            functionMapper.updateFunLevel(m);

            if (null != children && children.size() > 0)
            {
                updateChildren(children, levelNum, currentUserId, now);
            }
        }
    }

    @Override
    public List<Integer> getFunctionsByRoleId(int roleId)
    {
        return functionMapper.selectFunctionsByRoleId(roleId);
    }

  /*  @Override
    @Cacheable(value = Cache.PERMISSION_CACHE, key = "#root.targetClass + #root.methodName")
    public List<Map<String, Object>> getRequestMap()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> getUrls = urlMapper.selectUrlsByMethod("GET");
        List<Map<String, Object>> postUrls = urlMapper.selectUrlsByMethod("POST");
        List<Map<String, Object>> putUrls = urlMapper.selectUrlsByMethod("PUT");
        List<Map<String, Object>> deleteUrls = urlMapper.selectUrlsByMethod("DELETE");

        for (Map<String, Object> url : getUrls)
        {
            addUrlItem(url, list);
        }

        for (Map<String, Object> url : postUrls)
        {
            addUrlItem(url, list);
        }

        for (Map<String, Object> url : putUrls)
        {
            addUrlItem(url, list);
        }

        for (Map<String, Object> url : deleteUrls)
        {
            addUrlItem(url, list);
        }

        return list;
    }*/

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     */
 /*   private void addUrlItem(Map<String, Object> url, List<Map<String, Object>> list)
    {
        String u = (String) url.get(Url.URL_PATTERN);
        String m = (String) url.get(Url.HTTP_METHOD);
        String r = url.get("roleId").toString();
        String ro = "";

        for (Map<String, Object> map : list)
        {
            if (u.equals(map.get(Url.URL_PATTERN)) && m.equals(map.get(Url.HTTP_METHOD)))
            {
                ro = (String) map.get("roles") + ",";

                if (!StringUtils.contains(ro, r + ","))
                {
                    ro += r + ",";
                }

                ro = StringUtils.removeEnd(ro, ",");
                map.put("roles", ro);
                return;
            }
        }

        url.put("roles", r);
        list.add(url);
    }*/

    @Override
    public Map<String, Boolean> getFunctionsByUserId(int userId)
    {
        List<Map<String, Object>> list = functionMapper.selectFunctionsByUserId(userId);
        Map<String, Boolean> map = new HashMap<String, Boolean>(NumberEnum.NUMBER_32.getNum());

        for (Map<String, Object> m : list)
        {
            map.put(String.valueOf(m.get(Function.FUN_ID)), (Boolean) m.get(Function.ENABLED));
        }

        return map;
    }

    /**
     * 校验传入的功能参数
     */
    private void validateFun(SysFun model, Map<String, Object> map)
    {
        map.put(Function.FUN_NAME, StringUtils.trim(model.getFunName()));
        map.put(Function.PARENT_ID, model.getParentId());
        map.put(Function.SORT_NUM, model.getSortNum());
        map.put(Function.FUN_ID, model.getFunId());
/*        int count = functionMapper.selectCountByFunId(map);
        if (count > 0)
        {
            throw new CommMsgException("功能id已经存在，请重新输入");
        }*/
    /*    int count = functionMapper.selectCountByNameAndParentId(map);
        if (count > 0)
        {
            throw new CommMsgException("功能名已经存在，请重新输入");
        }*/

        int funIdCount = functionMapper.selectCountByFunId(map);
        if (funIdCount > 0)
        {
//            throw new CommMsgException("功能id已存在，不能重复绑定");
            throw new CommMsgException(MapUtil.get(FunKeys.FUNID_EXISTS));

        }

    }
}
