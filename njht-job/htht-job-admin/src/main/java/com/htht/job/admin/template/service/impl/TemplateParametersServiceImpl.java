package com.htht.job.admin.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htht.job.admin.template.constant.TemplateConstant;
import com.htht.job.admin.template.dao.TemplateParametersDao;
import com.htht.job.admin.template.entity.TemplateEntity;
import com.htht.job.admin.template.entity.TemplateParametersEntity;
import com.htht.job.admin.template.service.TemplateParametersService;
import com.htht.job.admin.template.service.TemplateService;
import com.htht.job.admin.template.vo.ParamInfoVo;
import com.htht.job.admin.template.vo.TemplateParamReqVo;
import com.htht.job.admin.template.vo.TemplateParamVo;
import com.htht.job.admin.template.vo.TemplateReqVo;
import com.htht.job.core.exception.CommonException;
import com.njht.webyun.enums.TreeNodeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板参数表
 * @author daiguojun
 * @date 2022-06-29 09:14:41
 */
@Service("templateParametersService")
public class TemplateParametersServiceImpl extends ServiceImpl<TemplateParametersDao, TemplateParametersEntity> implements TemplateParametersService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateParametersService templateParametersService;

    @Override
    public List<?> getParamListInfo(List<TemplateEntity> templateList) {
        // 获取参数对应的id
        List<String> idList = Optional.ofNullable(templateList).orElse(new ArrayList<>()).stream().map(TemplateEntity::getId).collect(Collectors.toList());
        // 根据id 查询对应的模板参数
        LambdaQueryWrapper<TemplateParametersEntity> qw = new LambdaQueryWrapper<>();
        qw.in(TemplateParametersEntity::getTemplateId,idList);
        qw.orderByAsc(TemplateParametersEntity::getSortKey);
        List<TemplateParametersEntity> paramList = this.list(qw);
        // 根据模板id分组,并设置模板参数返回结果
        Map<String, List<TemplateParamReqVo>> paramMap = Optional.ofNullable(paramList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    TemplateParamReqVo reqVo = new TemplateParamReqVo();
                    BeanUtils.copyProperties(item, reqVo);
//                    this.setControlMapInfo(item.getControlJson(), reqVo);
                    reqVo.setControlStr(item.getControlJson());
                    return reqVo;
                })
                .collect(Collectors.groupingBy(TemplateParamReqVo::getTemplateId));
        // 设置返回结果(将模板参数 放到对应的模板上)
        return Optional.ofNullable(templateList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    TemplateReqVo reqVo = new TemplateReqVo();
                    BeanUtils.copyProperties(item, reqVo);
                    List<TemplateParamReqVo> paramReqVoList = new ArrayList<>();
                    if (paramMap != null && !paramMap.isEmpty()) {
                        paramReqVoList = paramMap.get(item.getId());
                    }
                    reqVo.setParamList(paramReqVoList);
                    return reqVo;
                }).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrEdit(TemplateParamVo paramVo) {
        // 模板新增 或 修改
        TemplateEntity templateEntity = new TemplateEntity();
        BeanUtils.copyProperties(paramVo,templateEntity);
        // 设置为数据节点
        templateEntity.setType(TreeNodeEnum.DATA.getCode());
        templateService.saveOrUpdate(templateEntity);

        String id = templateEntity.getId();
        // 删除该id 对应的模板
        LambdaQueryWrapper<TemplateParametersEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(TemplateParametersEntity::getTemplateId,id);
        this.remove(qw);

        // 模板参数新增或修改
        if (paramVo.getParamList()!= null && !paramVo.getParamList().isEmpty()) {
            this.saveOrUpdateParam(paramVo.getParamList(),templateEntity.getId());
        }
    }

    /**
     * 模板参数新增或修改
     * @param paramList
     * @param templateId
     */
    private void saveOrUpdateParam(List<ParamInfoVo> paramList, String templateId) {
        List<TemplateParametersEntity> collect = Optional.ofNullable(paramList).orElse(new ArrayList<>())
                .stream()
                .map(item -> {
                    TemplateParametersEntity entity = new TemplateParametersEntity();
                    BeanUtils.copyProperties(item, entity);
                    entity.setTemplateId(templateId);
                    entity.setSortKey(item.getNum());
                    entity.setControlJson(item.getControlStr());
                    return entity;
                }).collect(Collectors.toList());
        // 批量新增
        templateParametersService.saveOrUpdateBatch(collect);
    }


    /**
     * map 转json
     * @param map
     * @return
     */
    private String setControlJsonInfo(HashMap<String, Object> map) {
        String json;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new CommonException("json格式转换异常");
        }
        return json;
    }

    @Override
    public List<TemplateParamReqVo> getParamList(String id, int num) {
        List<TemplateParametersEntity> paramList;
        // 算法模板 1 调度模板 0
        if (num == TemplateConstant.ALGORITHM_ZERO) {
            paramList = this.getAlgorithmParamList(id);
        } else {
            paramList = this.getPluginParamList(id);
        }
        return paramList
                .stream()
                .map(item -> {
                    TemplateParamReqVo paramInfoVo = new TemplateParamReqVo();
                    BeanUtils.copyProperties(item,paramInfoVo);
                    paramInfoVo.setControlStr(item.getControlJson());
                    return paramInfoVo;
                }).collect(Collectors.toList());
    }

    @Override
    public void deleteByTemplateId(String id) {
        LambdaQueryWrapper<TemplateParametersEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(TemplateParametersEntity::getTemplateId,id);
        this.remove(qw);
    }

    /**
     * 插件模板
     * @param id
     * @return
     */
    private List<TemplateParametersEntity> getPluginParamList(String id) {
        return baseMapper.selectTemplateParamListByPluginId(id);
    }

    /**
     * 算法模板
     * @param id
     * @return
     */
    private List<TemplateParametersEntity> getAlgorithmParamList(String id) {
        return baseMapper.selectTemplateParamListByAlgorithmId(id);
    }

}