package com.njht.webyun.system.dao.mapper;


import com.njht.webyun.system.model.sysDic.SysDic;
import com.njht.webyun.system.model.sysDic.SysDicQuery;
import com.njht.webyun.system.model.sysDic.SysDicValueQuery;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SysDicMapper {

    int deleteByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(SysDic record);

    Page<SysDic> getSysDicByPage(SysDicQuery query);
    Page<SysDic> getSysDicValueByPage(SysDicValueQuery dicValueQuery);
    int selectDicByNameAndType(SysDic record);
    int insertDic(SysDic record);
    int deleteByPrimaryKeyList(List<SysDic> list);
    List<SysDic> selectByType(SysDic record);



}