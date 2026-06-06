package com.njht.webyun.business.index.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.njht.entity.algorithm.AlgorithmEntity;
import com.njht.entity.xxljob.XxlJobRegistry;
import com.njht.webyun.business.feign.AdminFeignService;
import com.njht.webyun.business.index.service.RegistryService;
import com.njht.webyun.business.index.vo.ServerDetailReqVo;
import com.njht.webyun.business.index.vo.ServerReqVo;
import com.njht.webyun.entity.CommonEntity;
import com.njht.webyun.enums.TreeNodeEnum;
import com.njht.webyun.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 代国军
 * @description: 实现类
 * @date 2022/8/15 11:21
 */
@Service("registryService")
public class RegistryServiceImpl implements RegistryService {


    @Autowired
    private AdminFeignService adminFeignService;


    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<ServerReqVo> serverList() {
        // 获取服务器相关数据信息
        Map<String, Object> map = adminFeignService.registryList().getData();

        // 所有节点信息
        List<String> addressList = (List)map.get("address");

        // 当前可用节
        List<LinkedHashMap<String,Object>> list = (List)map.get("registry");

        // map转对象
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, XxlJobRegistry.class);
        List<XxlJobRegistry> registryInfoList = new ArrayList<>();
        try {
            registryInfoList = objectMapper.readValue(objectMapper.writeValueAsString(list),collectionType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 根据节点拆分数据
        Map<String, List<XxlJobRegistry>> collect = registryInfoList.stream().collect(Collectors.groupingBy(XxlJobRegistry::getRegistryValue));
        // 处理数据并返回
        return Optional.ofNullable(addressList).orElse(new ArrayList<>())
                .stream()
                .map(s -> this.setReqVo(s,collect.get(s)))
                .collect(Collectors.toList());
    }

    /**
     * 设置 节点cpu,内存使用情况信息
     * @param XxlJobRegistryList
     * @return
     */
    private ServerReqVo setReqVo(String address,List<XxlJobRegistry> XxlJobRegistryList) {
        ServerReqVo reqVo = new ServerReqVo();
        reqVo.setIp(address);
        if (XxlJobRegistryList == null || XxlJobRegistryList.isEmpty()) {
            // 当前节点不可用
            reqVo.setIsRun(0L);
        } else {
            // 节点可用,添加返回结果
            reqVo.setIsRun(1L);
            XxlJobRegistry xxlJobRegistry = XxlJobRegistryList.stream().findFirst().orElse(new XxlJobRegistry());
            Long totalSize = xxlJobRegistry.getTotalPhysicalMemorySize();
            Long useSize = totalSize - xxlJobRegistry.getFreePhysicalMemorySize();
            reqVo.setUsePhysicalMemorySize(this.byteToGB(useSize));
            reqVo.setTotalPhysicalMemorySize(this.byteToGB(totalSize));
            reqVo.setSystemCpuLoad(xxlJobRegistry.getSystemCpuLoad());
            reqVo.setMemoryLoad(String.format("%.5f", (double) useSize / (double) totalSize));
        }
        return reqVo;
    }


    /**
     * 转gb
     * @param byteNumber
     * @return
     */
    private String byteToGB(Long byteNumber) {
        final BigDecimal byteBigDecimal = new BigDecimal(byteNumber);
        final BigDecimal gbBigDecimal = byteBigDecimal.divide(new BigDecimal(1024L * 1024L * 1024L), 2, BigDecimal.ROUND_HALF_UP);
        if (gbBigDecimal.equals(new BigDecimal(0))) {
            return "0.00";
        }
        return gbBigDecimal.toString();
    }


    @Override
    public List<ServerDetailReqVo> getServerListByIpOrRunStatus(Long isRun, String ip) {
        // 获取所有可运行 不可运行服务器节点
        List<ServerReqVo> serverReqVos = this.serverList();
        // 过滤所有算法数据节点，并返回节点以及名称对应关系
        Map<String, List<CommonEntity>> map  = this.getAlgorithmMap();
        return Optional.ofNullable(serverReqVos).orElse(new ArrayList<>())
                .stream()
                // 过滤出符合要求的节点信息
                .filter(item -> this.isRun(isRun, item) && this.isContainsIp(ip, item))
                // 将算法结果塞到返回结果中
                .map(item -> {
                    ServerDetailReqVo reqVo = new ServerDetailReqVo();
                    BeanUtils.copyProperties(item, reqVo);
                    List<CommonEntity> commonEntities = map.getOrDefault(item.getIp(), new ArrayList<>());
                    List<String> collect = commonEntities.stream().map(CommonEntity::getLabel).collect(Collectors.toList());
                    reqVo.setAlgorithmList(collect);
                    return reqVo;
                }).collect(Collectors.toList());
    }

    /**
     * 过滤出包含该节点的信息
     * @param ip
     * @param item
     * @return
     */
    private boolean isContainsIp(String ip, ServerReqVo item) {
        return StringUtils.isEmpty(ip) || item.getIp().contains(ip);
    }

    /**
     * 判断当前节点状态并返回
     * @param isRun
     * @param item
     * @return
     */
    private boolean isRun(Long isRun, ServerReqVo item) {
        return isRun == -1 || item.getIsRun().equals(isRun);
    }

    /**
     * 算法 节点，名称对应关系map
     * @return
     */
    private Map<String, List<CommonEntity>> getAlgorithmMap() {
        // 所有算法信息
        List<AlgorithmEntity> algorithmEntityList = adminFeignService.algorithmNodeList().getData();
        return Optional.ofNullable(algorithmEntityList).orElse(new ArrayList<>())
                .stream()
                // 过滤出数据信息且registryValue 不为空
                .filter(item -> TreeNodeEnum.DATA.getCode().equals(item.getType()) && StringUtils.isNotEmpty(item.getRegistryValue()))
                .flatMap(item ->
                        // 返回结果个数与原先list不同
                        this.getValueAndNameList(item).stream()
                ).collect(Collectors.groupingBy(CommonEntity::getValue));
    }

    /**
     * 获取节点名称集合（将多个节点对应一个名称拆分成集合）
     * @param entity
     * @return
     */
    private List<CommonEntity> getValueAndNameList(AlgorithmEntity entity) {
        List<CommonEntity> list = new ArrayList<>();
        Arrays.asList(entity.getRegistryValue().split(","))
                .forEach(s -> {
                    CommonEntity commonEntity = new CommonEntity(s,entity.getName());
                    list.add(commonEntity);
                });

        return list;
    }
}
