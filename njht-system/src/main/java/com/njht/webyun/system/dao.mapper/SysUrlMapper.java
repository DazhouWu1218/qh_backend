package com.njht.webyun.system.dao.mapper;


import com.njht.webyun.system.model.sysFun.SysFun;
import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.njht.webyun.system.model.sysUrl.SysUrlQuery;
import com.njht.webyun.system.model.sysUrl.SysUrlResp;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SysUrlMapper {

    Page<SysUrlResp> getUrl(SysUrlQuery urlQuery);

    int insertUrl(SysUrl record);

    int insertSelective(SysUrl record);

    SysUrl selectByPrimaryKey(Integer urlId);

    int updateByPrimaryKeySelective(SysUrl record);

    int updateByPrimaryKey(SysUrl record);

    int selectCountName(SysUrl record);

    int deleteByPrimaryKey(List<Integer> urlId);

    List<SysUrl> selectUrlsAndRole();

    List<SysUrl> selectUrlsByFunId(int funId);

    Page<SysUrl> selectUrlsExceptByPage(SysUrlQuery query);

    void deleteFunUrl(SysFun sysFun);

    int insertFunUrl(SysFun sysFun);
}
