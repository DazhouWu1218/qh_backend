package com.njht.webyun.system.service.inf;


import com.njht.webyun.system.model.sysFun.SysFun;
import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.njht.webyun.system.model.sysUrl.SysUrlQuery;
import com.njht.webyun.system.model.sysUrl.SysUrlResp;
import com.github.pagehelper.Page;

import java.util.List;

public interface SysUrlService
{
    public Page<SysUrlResp> showSysUrl(SysUrlQuery urlQuery) throws Exception;

    public void addUrl(SysUrl model) throws Exception;

    public void editUrl(SysUrl user) throws Exception;

    public void deleteUser(List<Integer> list) throws Exception;

    List<SysUrl> selectUrlsAndRole();

    List<SysUrl> getUrlsByFunId(int funId) throws Exception;

    Page<SysUrl> getUrlsExceptByPage(SysUrlQuery model) throws Exception;

    public void bindUrl(SysFun sysFun) throws Exception;
}
