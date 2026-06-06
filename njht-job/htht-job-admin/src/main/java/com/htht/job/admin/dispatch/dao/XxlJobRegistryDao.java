package com.htht.job.admin.dispatch.dao;

import com.njht.entity.xxljob.XxlJobRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by piesat on 16/9/30.
 */
@Mapper
public interface XxlJobRegistryDao {

    public List<Integer> findDead(@Param("timeout") int timeout,
                                  @Param("nowTime") Date nowTime);

    public int removeDead(@Param("ids") List<Integer> ids);

    public List<XxlJobRegistry> findAll(@Param("timeout") int timeout,
                                        @Param("nowTime") Date nowTime);

    public int registryUpdate(XxlJobRegistry xxlJobRegistry);

    public int registrySave(XxlJobRegistry xxlJobRegistry);

    public int registryDelete(@Param("registryGroup") String registryGroup,
                          @Param("registryKey") String registryKey,
                          @Param("registryValue") String registryValue);

    /**
     * 查询该执行器对应的可用节点
     * @param timeout
     * @param nowTime
     * @param appName
     * @return
     */
    List<String> findRegistryValueList(@Param("timeout") int timeout,
                                       @Param("nowTime") Date nowTime,@Param("appName") String appName);
}
