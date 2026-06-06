package com.htht.job.admin.dispatch.service.impl;

import com.github.pagehelper.PageInfo;
import com.htht.job.admin.algorithm.service.AlgorithmService;
import com.htht.job.admin.dispatch.core.model.XxlJobGroup;
import com.htht.job.admin.plugin.service.HandlerService;
import com.njht.entity.xxljob.XxlJobRegistry;
import com.htht.job.admin.dispatch.core.util.I18nUtil;
import com.htht.job.admin.dispatch.dao.XxlJobGroupDao;
import com.htht.job.admin.dispatch.dao.XxlJobInfoDao;
import com.htht.job.admin.dispatch.dao.XxlJobRegistryDao;
import com.htht.job.admin.dispatch.service.JobGroupService;
import com.htht.job.admin.dispatch.vo.JobGroupVo;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.enums.RegistryConfig;
import com.njht.webyun.exception.CommonException;
import com.njht.webyun.utils.PageUtil;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 代国军
 * @description: 实现类
 * @date 2022/5/30 14:45
 */
@Service
public class JobGroupServiceImpl implements JobGroupService {

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;

    @Resource
    private XxlJobRegistryDao xxlJobRegistryDao;

    @Resource
    public XxlJobInfoDao xxlJobInfoDao;

    @Autowired
    private HandlerService handlerService;

    @Autowired
    private AlgorithmService algorithmService;

    @Override
    public PageUtils queryPageList(JobGroupVo jobGroupVo) {
        PageUtil.setPageAndSize(jobGroupVo.getPage(),jobGroupVo.getSize(),0,10);
        List<XxlJobGroup> xxlJobGroups = xxlJobGroupDao.selectGroupList(jobGroupVo.getAppname(), jobGroupVo.getTitle(),null);
        return new PageUtils(new PageInfo<>(xxlJobGroups));
    }

    @Override
    public ReturnT<String> insert(XxlJobGroup xxlJobGroup) {

        // valid
        if (xxlJobGroup.getAppname()==null || xxlJobGroup.getAppname().trim().length()==0) {
            return new ReturnT<>(500, (I18nUtil.getString("system_please_input")+"AppName") );
        }
        if (xxlJobGroup.getAppname().length()<4 || xxlJobGroup.getAppname().length()>64) {
            return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_appname_length") );
        }
        if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
            return new ReturnT<>(500, "AppName"+I18nUtil.getString("system_unvalid") );
        }
        if (xxlJobGroup.getTitle()==null || xxlJobGroup.getTitle().trim().length()==0) {
            return new ReturnT<>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
        }
        if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
            return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_title")+I18nUtil.getString("system_unvalid") );
        }
        if (xxlJobGroup.getAddressType()!=0) {
            if (xxlJobGroup.getAddressList()==null || xxlJobGroup.getAddressList().trim().length()==0) {
                return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
            }
            if (xxlJobGroup.getAddressList().contains(">") || xxlJobGroup.getAddressList().contains("<")) {
                return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_registryList")+I18nUtil.getString("system_unvalid") );
            }
            String[] addressList = xxlJobGroup.getAddressList().split(",");
            for (String item: addressList) {
                if (item==null || item.trim().length()==0) {
                    return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
                }
            }
        }
        // process
        xxlJobGroup.setUpdateTime(new Date());
        int ret = xxlJobGroupDao.save(xxlJobGroup);
        return (ret>0)?ReturnT.success():ReturnT.failed();
    }

    @Override
    public ReturnT<String> edit(XxlJobGroup xxlJobGroup) {
        // valid
        if (xxlJobGroup.getAppname()==null || xxlJobGroup.getAppname().trim().length()==0) {
            return new ReturnT<>(500, (I18nUtil.getString("system_please_input")+"AppName") );
        }
        if (xxlJobGroup.getAppname().length()<4 || xxlJobGroup.getAppname().length()>64) {
            return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_appname_length") );
        }
        if (xxlJobGroup.getTitle()==null || xxlJobGroup.getTitle().trim().length()==0) {
            return new ReturnT<>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
        }
        if (xxlJobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = this.findRegistryByAppName(xxlJobGroup.getAppname());
            String addressListStr = null;
            if (registryList!=null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = "";
                for (String item:registryList) {
                    addressListStr += item + ",";
                }
                addressListStr = addressListStr.substring(0, addressListStr.length()-1);
            }
            xxlJobGroup.setAddressList(addressListStr);
        } else {
            // 1=手动录入
            if (xxlJobGroup.getAddressList()==null || xxlJobGroup.getAddressList().trim().length()==0) {
                return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
            }
            String[] addresss = xxlJobGroup.getAddressList().split(",");
            for (String item: addresss) {
                if (item==null || item.trim().length()==0) {
                    return new ReturnT<>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
                }
            }
        }

        // process
        xxlJobGroup.setUpdateTime(new Date());

        int ret = xxlJobGroupDao.update(xxlJobGroup);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @Override
    public ReturnT<String> delete(int id) {
        // valid
        int count = xxlJobInfoDao.pageListCount(0, 10, id, -1,  null, null, null);
        if (count > 0) {
            throw new CommonException(I18nUtil.getString("jobgroup_del_limit_0"));
        }

        // 校验该执行器是否与插件绑定
        Integer handlerCount  = handlerService.queryCountByGroupId(id);
        if (handlerCount > 0){
            throw new CommonException("拒绝删除,执行器与插件绑定");
        }

        // 校验该执行器是否与算法绑定
        Integer algorithmCount  = algorithmService.queryCountByGroupId(id);
        if (algorithmCount > 0){
            throw new CommonException("拒绝删除,执行器与算法节点绑定");
        }

        List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
        if (allList.size() == 1) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1") );
        }

        int ret = xxlJobGroupDao.remove(id);
        return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @Override
    public XxlJobGroup loadById(int id) {
        return  xxlJobGroupDao.load(id);
    }

    @Override
    public Map<String, Object> addressInfo() {
        Map<String,Object> map = new HashMap<>(2);
        // 执行器信息
        List<XxlJobGroup> xxlJobGroups = xxlJobGroupDao.selectGroupList(null,null,null);
        // 所有节点集合
        List<String> addressList = xxlJobGroups.stream()
                .filter(xxlJobGroup -> StringUtils.isNotEmpty(xxlJobGroup.getAddressList()))
                .flatMap(xxlJobGroup -> {
            String[] split = xxlJobGroup.getAddressList().split(",");
            return Arrays.stream(split);
        }).collect(Collectors.toList());
        map.put("address",addressList);
        // 可用节点信息集合
        List<XxlJobRegistry> registryList = xxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        map.put("registry",registryList);
        return map;
    }

    @Override
    public String getNameById(Integer groupId) {
        return xxlJobGroupDao.getNameById(groupId);
    }

    /**
     *  查找改执行器以注册节点信息
     * @param appNameParam
     * @return
     */
    private List<String> findRegistryByAppName(String appNameParam){
        HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
        List<XxlJobRegistry> list = xxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        if (list != null) {
            for (XxlJobRegistry item: list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appname = item.getRegistryKey();
                    List<String> registryList = appAddressMap.get(appname);
                    if (registryList == null) {
                        registryList = new ArrayList<>();
                    }

                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                    appAddressMap.put(appname, registryList);
                }
            }
        }
        return appAddressMap.get(appNameParam);
    }
}
