package com.htht.job.admin.plugin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htht.job.admin.plugin.entity.HandlerEntity;
import com.htht.job.admin.plugin.vo.HandlerSearchVo;
import com.htht.job.admin.plugin.vo.HandlerVo;
import com.htht.job.admin.plugin.vo.PluginTaskReqVo;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.utils.PageUtils;

import java.util.List;

/**
 * 插件管理表
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-30 13:12:01
 */
public interface HandlerService extends IService<HandlerEntity> {


    /**
     * 查询插件数据列表
     * @param handlerSearchVo
     * @return
     */
    PageUtils queryPage(HandlerSearchVo handlerSearchVo);

    /**
     * 通过树结构查询目录以及对应数据信息
     * @param type
     * @return
     */
    List<Tree> queryTreeByType(String type);

    /**
     * 新增
     * @param handler
     */
    void saveHandler(HandlerVo handler);

    /**
     * 修改
     * @param handler
     */
    void updateHandlerById(HandlerVo handler);


    /**
     * 根据执行器id和handlerId 查询 插件列表
     * @param groupId
     * @param handlerId
     * @return
     */
    List<PluginTaskReqVo> queryPluginListByGroupId(String groupId, String handlerId);

    /**
     * 通过插件id  获取插件标识
     * @param handlerId
     * @return
     */
    HandlerEntity getHandlerById(String handlerId);

    /**
     * 查询插件绑定的执行器注册节点
     * @param handlerId
     * @return
     */
    List<String> queryRegisterListByHandlerId(String handlerId);

    /**
     * 删除数据节点
     * @param id
     * @return
     */
    Boolean deleteNode(String id);

    /**
     * 获取插件父id
     * @param handlerId
     * @return
     */
    List<String> getHandlerParentIdArr(String handlerId);

    /**
     * 根据模板id 查询插件
     * @param id
     * @return
     */
    List<HandlerEntity> queryByTemplateId(String id);

    /**
     * 删除 数据节点
     * @param ids
     * @return
     */
    boolean deleteData(List<String> ids);

    /**
     * 统计与该执行器相关的插件总数
     * @param id
     * @return
     */
    Integer queryCountByGroupId(int id);
}

