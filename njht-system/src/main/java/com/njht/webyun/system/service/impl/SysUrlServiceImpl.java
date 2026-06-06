package com.njht.webyun.system.service.impl;


import com.njht.webyun.common.UserUtil;
import com.njht.webyun.exception.CommMsgException;
import com.njht.webyun.utils.MapUtil;
import com.njht.webyun.system.constant.CommonKey;
import com.njht.webyun.system.constant.FunKeys;
import com.njht.webyun.system.constant.UrlKeys;
import com.njht.webyun.system.dao.mapper.SysUrlMapper;
import com.njht.webyun.system.model.base.BeanProperty.Num;
import com.njht.webyun.system.model.sysFun.SysFun;
import com.njht.webyun.system.model.sysUrl.SysUrl;
import com.njht.webyun.system.model.sysUrl.SysUrlQuery;
import com.njht.webyun.system.model.sysUrl.SysUrlResp;
import com.njht.webyun.system.service.inf.SysUrlService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Component
@Service
public class SysUrlServiceImpl implements SysUrlService {

    @Autowired
    SysUrlMapper sysUrlMapper;

    /**
     * 查询所有系统URL，支持条件查询
     *
     * @param urlQuery
     * @return
     * @throws Exception
     */
    @Override
    public Page<SysUrlResp> showSysUrl(SysUrlQuery urlQuery) throws Exception {
        if (urlQuery.getUrlName() != null) {
            urlQuery.setUrlName(StringUtils.trimAllWhitespace(URLDecoder.decode(urlQuery.getUrlName(), "UTF-8")));
        }
        PageHelper.startPage(urlQuery.getPage(), urlQuery.getRows());
        return sysUrlMapper.getUrl(urlQuery);
    }

    /**
     * 新增URL
     * URL 在vue协同时，用作菜单的URL配置
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public void addUrl(SysUrl model) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(4);
        if (!checkUrl(model)) {
//            throw new CommMsgException("URL地址或名称不能为空");
            throw new CommMsgException(MapUtil.get(UrlKeys.URLNAME_OR_ADDRESS_NOTNULL));
        }
        int currentUserId = UserUtil.getCurrentUser().getUserId();

        int count = sysUrlMapper.selectCountName(model);
        if (count > 0) {
//            throw new CommMsgException("URL名存在");
            throw new CommMsgException(MapUtil.get(UrlKeys.URLNAME_EXISTS));
        }

        if("GET".equals(model.getHttpMethod())){
            model.setLoggedDataChanged(0);
        }else{
            model.setLoggedDataChanged(1);
        }

        Date now = new Date();
        model.setCreatedBy(currentUserId);
        model.setCreatedDate(now);
        model.setLastUpdatedBy(currentUserId);
        model.setLastUpdatedDate(now);

        if (Num.ZERO == sysUrlMapper.insertUrl(model)) {
//            throw new CommMsgException("插入0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.ADD_FAIL));
        }
    }

    /**
     * 编辑URL
     * URL 在vue协同时，用作菜单的URL配置
     *
     * @param sysUrl
     * @throws Exception
     */
    @Override
    public void editUrl(SysUrl sysUrl) throws Exception {
        sysUrl.setLastUpdatedBy(UserUtil.getCurrentUser().getUserId());
        sysUrl.setLastUpdatedDate(new Date());
        sysUrl.setPk_urlId(sysUrl.getUrlId());
        int count = sysUrlMapper.selectCountName(sysUrl);
//        if (count > 0) throw new CommMsgException("URL名存在");
        if (count > 0) throw new CommMsgException(MapUtil.get(UrlKeys.URLNAME_EXISTS));

        if("GET".equals(sysUrl.getHttpMethod())){
            sysUrl.setLoggedDataChanged(0);
        }else{
            sysUrl.setLoggedDataChanged(1);
        }
        if (Num.ZERO == sysUrlMapper.updateByPrimaryKeySelective(sysUrl)) {
//            throw new CommMsgException("修改0条数据");
            throw new CommMsgException(MapUtil.get(CommonKey.EDIT_FAIL));
        }

    }

    @Override
    public void deleteUser(List<Integer> list) throws Exception {
        if (StringUtils.isEmpty(list)){
//            throw new CommMsgException("传入list为空");
            throw new CommMsgException(MapUtil.get(CommonKey.NOTNULL));
        }
        int retNum = sysUrlMapper.deleteByPrimaryKey(list);
        if (Num.ZERO == retNum) {
//            throw new CommMsgException("删除时存在无效URLID");
            throw new CommMsgException(MapUtil.get(UrlKeys.INVALID_URLID));
        } else if (retNum != list.size()) {
//            throw new CommMsgException("删除成功的数量不一致");
            throw new CommMsgException(MapUtil.get(UrlKeys.NUMBER_INCONFORMITY));
        }
    }

    @Override
//    @Cacheable(value = Cache.PERMISSION_CACHE, key = "#root.targetClass + #root.methodName")
    public List<SysUrl> selectUrlsAndRole() {
        return sysUrlMapper.selectUrlsAndRole();
    }


    @Override
    public List<SysUrl> getUrlsByFunId(int funId) throws Exception
    {
        if(funId == Num.ZERO){
//            throw new CommMsgException("funId不能为0");
            throw new CommMsgException(MapUtil.get(FunKeys.FUNID_NOT_BE_0));
        }
        return sysUrlMapper.selectUrlsByFunId(funId);
    }

    /**
     * 查询除某些URL外的所有URL
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public Page<SysUrl> getUrlsExceptByPage(SysUrlQuery model) throws Exception
    {
        PageHelper.startPage(model.getPage(), model.getRows());
        return sysUrlMapper.selectUrlsExceptByPage(model);
    }

    @Override
    public void bindUrl(SysFun sysFun) throws Exception{
        sysUrlMapper.deleteFunUrl(sysFun);

        if (null != sysFun.getUrlId()) {
            if(sysUrlMapper.insertFunUrl(sysFun) == 0){
//                throw new CommMsgException("绑定了0条URL信息");
                throw new CommMsgException(MapUtil.get(CommonKey.FAIL));
            }
        }else{
//            throw new CommMsgException("URL为NULL");
            throw new CommMsgException(MapUtil.get(UrlKeys.URL_IS_NULL));
        }
    }
    /**
     * 校验前端传来的URL对象合法性
     *
     * @return
     */
    private boolean checkUrl(SysUrl sysurl) {
        if (StringUtils.isEmpty(sysurl.getHttpMethod()) || StringUtils.isEmpty(sysurl.getUrlName())) {
            return false;
        }

        return true;
    }

}
