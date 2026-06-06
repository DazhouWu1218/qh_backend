package com.njht.webyun.business.index.service;

import com.njht.webyun.business.index.vo.ServerDetailReqVo;
import com.njht.webyun.business.index.vo.ServerReqVo;

import java.util.List;

/**
 * @author 代国军
 * @description: 服务器监控服务
 * @date 2022/8/15 11:21
 */
public interface RegistryService {

    /**
     * 服务器列表
     * @return
     */
    List<ServerReqVo> serverList();


    /**
     * 服务器节点详情
     * @param isRun
     * @param ip
     * @return
     */
    List<ServerDetailReqVo> getServerListByIpOrRunStatus(Long isRun, String ip);
}
