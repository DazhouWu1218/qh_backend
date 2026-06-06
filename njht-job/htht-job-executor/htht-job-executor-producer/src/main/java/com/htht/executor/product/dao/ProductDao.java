package com.htht.executor.product.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htht.executor.product.entity.ProductEntity;
import com.htht.executor.statistics.entity.CategoryProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author daiguojun
 * @email daiguojun@piesat.cn
 * @date 2022-05-10 18:06:35
 */
@DS("mysql")
@Mapper
@Repository
public interface ProductDao extends BaseMapper<ProductEntity> {

    /**
     * 查询产品信心
     * @param treeKey
     * @return
     */
    List<CategoryProductDto> selectProductBaseCfgInfo(@Param("treeKey") String treeKey);

    /**
     * 查询cimiss 日数据
     * @param txtCols
     * @param tableName
     * @param year
     * @param mon
     * @param day
     * @return
     */
    List<Map<String, Object>> selectCimisInfoFromTable(@Param("txtCols") String txtCols,
                                                       @Param("tableName") String tableName,
                                                       @Param("year") Integer year,
                                                       @Param("mon") Integer mon,
                                                       @Param("day") Integer day);

    List<Map<String, Object>> selectCimisInfoFromTableMon(@Param("txtCols") String txtCols,
                                                          @Param("tableName") String tableName,
                                                          @Param("year") Integer year,
                                                          @Param("mon") Integer mon);
}
