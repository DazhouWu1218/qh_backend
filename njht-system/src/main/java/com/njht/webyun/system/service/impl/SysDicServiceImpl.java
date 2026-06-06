package com.njht.webyun.system.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.system.dao.mapper.SysDicMapper;
import com.njht.webyun.system.model.base.BeanProperty;
import com.njht.webyun.system.model.sysDic.SysDic;
import com.njht.webyun.system.model.sysDic.SysDicQuery;
import com.njht.webyun.system.model.sysDic.SysDicValueQuery;
import com.njht.webyun.system.service.inf.SysDicService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author ：David
 * @date ：Created in 2019/12/9 10:54
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Transactional
@DS(DbConstant.MYSQL)
public class SysDicServiceImpl implements SysDicService {

    @Autowired
    SysDicMapper sysDicMapper;

    /**
     * 字典信息查询分页
     *
     * @param sysDicQuery
     * @return
     * @throws Exception
     */
    @Override
    public Page<SysDic> showSysDics(SysDicQuery sysDicQuery) throws Exception {
        PageHelper.startPage(sysDicQuery.getPage(), sysDicQuery.getRows());
        Page<SysDic> sysDic = sysDicMapper.getSysDicByPage(sysDicQuery);
        return sysDic;
    }

    /*
     * 字典值信息查询分页
     * @param sysDicQuery
     * @return
     * @throws Exception
     */
    @Override
    public Page<SysDic> showSysDicValues(SysDicValueQuery dicValueQuery) throws Exception {
        PageHelper.startPage(dicValueQuery.getPage(), dicValueQuery.getRows());
        Page<SysDic> sysDic = sysDicMapper.getSysDicValueByPage(dicValueQuery);
        return sysDic;
    }

    @Override
    public void addSysDic(List<SysDic> sysDicList) throws Exception {


        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();
        if(!"".equals(checkDicParam(sysDicList.get(0)))){
            throw new CommMsgException(checkDicParam(sysDicList.get(0)));
        }
        int countDicName = sysDicMapper.selectDicByNameAndType(sysDicList.get(0));
        //字典名称或字典类型都不能重复
        if (countDicName > 0)
        {
            throw new CommMsgException("字典名或类型已存在");
        }

        for (SysDic sysDic : sysDicList) {
            //检查参数，必输字段不能为空
            if(!"".equals(checkDicParam(sysDic))){
                throw new CommMsgException(checkDicParam(sysDic));
            }

            sysDic.setCreatedBy(currentUserId);
            sysDic.setCreatedDate(now);
            sysDic.setLastUpdatedBy(currentUserId);
            sysDic.setLastUpdatedDate(now);
            if(BeanProperty.Num.ZERO == sysDicMapper.insertDic(sysDic)){
                throw new CommMsgException("插入数据不成功，dicName="+sysDic.getDicName()+" dicKey="+sysDic.getDicKey());
            }

        }

    }

    @Override
    public void editSysDic(List<SysDic> sysDicList) throws Exception {

        int currentUserId = UserUtil.getCurrentUser().getUserId();
        Date now = new Date();
        //字典名称或字典类型都不能重复
        if (sysDicMapper.selectDicByNameAndType(sysDicList.get(0)) > 0)
        {
            throw new CommMsgException("字典名或类型已存在");
        }
        String status = "";
        for (SysDic sysDic : sysDicList) {
            status = sysDic.getStatu();
            sysDic.setCreatedBy(currentUserId);
            sysDic.setCreatedDate(now);
            sysDic.setLastUpdatedBy(currentUserId);
            sysDic.setLastUpdatedDate(now);
            if (status.equals("inserted")) {
                sysDicMapper.insertDic(sysDic);
            } else if (status.equals("deleted")) {
                if (sysDic.getDicId() != null && sysDic.getDicId() > 0) {
                    sysDicMapper.deleteByPrimaryKey(sysDic.getDicId());
                }
            } else if (status.equals("updated")) {
                if (sysDic.getDicId() != null && sysDic.getDicId() > 0) {
                    sysDicMapper.updateByPrimaryKeySelective(sysDic);
                }
            }
        }
    }

    @Override
    public void deleteSysDics(List<SysDic> list) throws Exception {
        sysDicMapper.deleteByPrimaryKeyList(list);
    }

    @Override
    public List<SysDic> getValuesByType(SysDic sysDic) throws Exception {
        return sysDicMapper.selectByType(sysDic);
    }

    private String checkDicParam(SysDic dic){
        if(dic.getDicId()==null || dic.getDicId()==0 ){//新增字典
            if("".equals(dic.getDicName()) || "".equals(dic.getDicType()) || "".equals(dic.getDicValue()) || "".equals(dic.getDicKey())){
                return "请检查参数，不能为空";
            }
        }else{//编辑字典

        }
        return "";
    }
}
