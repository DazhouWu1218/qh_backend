package com.njht.webyun.system.service.inf;



import com.njht.webyun.system.model.sysDic.SysDic;
import com.njht.webyun.system.model.sysDic.SysDicQuery;
import com.njht.webyun.system.model.sysDic.SysDicValueQuery;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author ：David
 * @date ：Created in 2019/12/9 10:44
 * @description：
 * @modified By：
 * @version: $
 */
public interface SysDicService {

    Page<SysDic> showSysDics(SysDicQuery sysDicQuery) throws Exception;

    Page<SysDic> showSysDicValues(SysDicValueQuery dicValueQuery) throws Exception;

    void addSysDic(List<SysDic> sysDic) throws Exception;

    void editSysDic(List<SysDic> SysDic) throws Exception;

    void deleteSysDics(List<SysDic> list) throws Exception;

    List<SysDic> getValuesByType(SysDic sysDic) throws Exception;


}
