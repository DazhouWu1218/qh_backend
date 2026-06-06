package com.htht.executor.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.product.dao.ProductDao;
import com.htht.executor.product.entity.ProductEntity;
import com.htht.executor.product.service.ProductService;
import com.htht.executor.statistics.entity.CategoryProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author daiguojun
 * @date 2022-05-10 18:06:35
 */
@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductDao, ProductEntity> implements ProductService {

    @Override
    public List<CategoryProductDto> selectProductBaseCfgInfo(String treeKey) {
        return baseMapper.selectProductBaseCfgInfo(treeKey);
    }

    @Override
    public List<Map<String, Object>> selectCimisInfoFromTable(String txtCols, String tableName, String issue,String cycle) {
        String year = issue.substring(0,4);
        String mon = issue.substring(4,6);
        String day = issue.substring(6,8);
        if ("COAM".equals(cycle)) {
            return baseMapper.selectCimisInfoFromTableMon(txtCols,tableName,Integer.valueOf(year),Integer.valueOf(mon));
        }
        return baseMapper.selectCimisInfoFromTable(txtCols,tableName,Integer.valueOf(year),Integer.valueOf(mon), Integer.valueOf(day));
    }
}