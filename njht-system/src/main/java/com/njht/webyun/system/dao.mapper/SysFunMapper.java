package com.njht.webyun.system.dao.mapper;

import com.njht.webyun.system.model.sysFun.SysFun;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：David
 * @date ：Created in 2020/4/20 15:31
 * @description：
 * @modified By：
 * @version: $
 */
@Component
@Mapper
public interface SysFunMapper {
    void insertFunUrl(Map<String, Object> map);

    void deleteFunUrl(int funId);

    List<Integer> selectFunctions();

    List<Integer> selectFunctionsByRoleId(int roleId);

    List<Map<String, Object>> selectFunctionsByUserId(int userId);

    int selectCountByFunId(Map<String, Object> map);

    int selectCountByNameAndParentId(Map<String, Object> map);

    int insertFun(SysFun sysFun);

    int updateFun(SysFun sysFun);

    void updateSysFunSelf(SysFun sysFun);

    int deleteFun(int map);

    List<Map<String, Object>> selectFunsByParentId(int parentId);

    void updateFunLevel(SysFun map);

    void updateFunSortNum(SysFun sysFun);

    List<Map<String, Object>> selectFunctionsForTree(HashMap<String, Integer> map);

    List<Map<String, Object>> selectFunContainCheck(HashMap<String, Integer> map);

    List<Map<String, Object>> selectFunsByParentId(HashMap<String, Integer> map);

    List<SysFun> getFunIdByUserId(int parentId);
}
