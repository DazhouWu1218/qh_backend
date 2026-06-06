package com.htht.data.category.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njht.entity.category.DataCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2021-11-11 10:22:52
 */
@Mapper
public interface DataCategoryDao extends BaseMapper<DataCategoryEntity> {

    /**
     * 通过角色id查询树结构中的分类信息
     * @param idList
     * @return
     */
    List<DataCategoryEntity> selectTreeListByRoleId(@Param("idList") List<String> idList,@Param("identify")String identify,
                                                    @Param("parentId") String parentId);

    /**
     * 通过产品id 获取父级名称信息
     * @param idList
     * @return
     */
    List<DataCategoryEntity> getParentNameByProductIds(@Param("idList") List<String> idList);
}
