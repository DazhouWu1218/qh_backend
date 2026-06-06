package com.njht.webyun.business.datapush.service.impl;


import com.njht.entity.dataPush.SystemStatusEntity;
import com.njht.webyun.business.common.util.RedisService;
import com.njht.webyun.business.datapush.constant.EsConstant;
import com.njht.webyun.business.datapush.service.DataPushService;
import com.njht.webyun.business.index.service.RegistryService;
import com.njht.webyun.business.index.vo.ServerReqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author daiguojun
 * @date 2022-09-06 15:25
 * 系统运行状态推送 实现类
 */
@Service(EsConstant.SYSTEM_STATUS)
@Slf4j
public class SystemServerPushServiceImpl implements DataPushService {

    @Value("${spring.redis.host}")
    private String ip;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RegistryService registryService;

    @Override
    public List<?> execute() {
        log.info("系统运行状态统计");
        SystemStatusEntity mysqlEntity = new SystemStatusEntity();
        SystemStatusEntity redisEntity = new SystemStatusEntity();
        List<SystemStatusEntity> nodeList = new ArrayList<>();
        try {
            // 获取数据库运行状态 默认数据库正常 数据查询失败则为异常
            mysqlEntity = new SystemStatusEntity(ip,EsConstant.MYSQL,EsConstant.NORMAL);
            // 获取redis运行状态
            redisEntity = new SystemStatusEntity(ip,EsConstant.REDIS,EsConstant.NORMAL);
            // 测试redis 链接是否正常
            try {
                redisService.exists(ip);
            } catch (Exception e) {
                e.printStackTrace();
                // 抛异常之后 将redis 的状态设置为异常
                redisEntity.setServerStatus(EsConstant.ABNORMAL);
            }
            // 获取服务器节点运行状态
            List<ServerReqVo> registryEntityList = registryService.serverList();
            nodeList = Optional.ofNullable(registryEntityList).orElse(new ArrayList<>())
                    .stream()
                    .map(item -> {
                        SystemStatusEntity registryEntity = new SystemStatusEntity(EsConstant.SYSTEM);
                        registryEntity.setServerIp(item.getIp());
                        // 设置节点运行状态
                        setServerStatus(item.getIsRun(), registryEntity);
                        return registryEntity;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            mysqlEntity.setServerStatus(EsConstant.ABNORMAL);
        }
        nodeList.add(mysqlEntity);
        nodeList.add(redisEntity);
        return nodeList;
    }

    /**
     * 返回节点正常 或者异常
     * @param isRun
     */
    private void setServerStatus(Long isRun, SystemStatusEntity registryEntity) {
        if (isRun == 1L) {
            registryEntity.setServerStatus(EsConstant.NORMAL);
        } else {
            registryEntity.setServerStatus(EsConstant.ABNORMAL);
        }

    }
}
