package com.njht.webyun.system.service.inf;

import com.njht.webyun.system.model.sysFun.FunMoveModel;
import com.njht.webyun.system.model.sysFun.SysFun;

import java.util.List;
import java.util.Map;

/**
 * @author ：David
 * @date ：Created in 2020/4/20 15:19
 * @description：
 * @modified By：
 * @version: $
 */
public interface SysFunService {

    List<Map<String, Object>> getFunsByParentId(int userId, int parentId) throws Exception;

    void createFun(SysFun model)throws Exception;

    void editSysFun(SysFun model)throws Exception;

    void removeSysFun(int id)throws Exception;

    void moveSysFun(FunMoveModel funMoveModel) throws Exception;

//    List<Map<String, Object>> getRequestMap();

    List<Integer> getFunctionsByRoleId(int roleId);

    Map<String, Boolean> getFunctionsByUserId(int userId);

    List<Map<String, Object>> getFunctionsForTree(int userId, int parentId);

    public List<Map<String, Object>> selectFunContainCheck(int userId, int parentId,int roleId);

//    void createFunUrl(int funId, List<Integer> list);



}
