package com.htht.job.admin.algorithm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.njht.entity.algorithm.AlgorithmEntity;
import com.htht.job.admin.algorithm.vo.AlgorithmVo;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.utils.PageUtils;

import java.util.List;

/**
 * 算法管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:06:10
 */
public interface AlgorithmService extends IService<AlgorithmEntity> {

    PageUtils queryPage(AlgorithmVo algorithmVo);

    /**
     * 根据执行器id 和 插件id 查询算法相关信息
     *
     * @param groupId
     * @param handlerId
     * @return
     */
    List<Tree> queryAlgorithmList(String groupId, String handlerId);


    /**
     * 根据任务id 查询对应的算法执行节点
     * @param id
     * @return
     */
    String queryAddressListByJobId(int id);

    /**
     * 节点列表
     * @param handlerId
     * @return
     */
    List<String> getRegisterList(String handlerId);

    /**
     * 查询算法每一级 id信息
     * @param algorithmId
     * @return
     */
    List<String> queryAlgorithmIdList(String algorithmId);

    /**
     * 新增或修改
     * @param algorithm
     * @return
     */
    boolean addOrEdit(AlgorithmEntity algorithm);

    /**
     * 根据模板id 查询算法
     * @param id
     * @return
     */
    List<AlgorithmEntity> queryByTemplateId(String id);

    /**
     * 删除算法
     * @param ids
     * @return
     */
    boolean delete(List<String> ids);

    /**
     * 与该执行器绑定的算法总数
     * @param id
     * @return
     */
    Integer queryCountByGroupId(int id);
}