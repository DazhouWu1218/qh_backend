package com.njht.webyun.business.feign;

import com.njht.entity.category.DataCategoryEntity;
import com.njht.entity.product.ProductEntity;
import com.njht.webyun.utils.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 代国军
 * @description: 数据中心远程调度service
 * @date 2022/7/26 15:08
 */
@FeignClient("njht-data-center")
public interface DataCenterFeignService {

    /**
     * 产品发布（支持批量发布产品）
     */
    @PostMapping("/data-center/product/info/editProductStatus")
    ReturnT update(@RequestParam("ids") List<String> ids);

    /**
     * 产品删除（删除某一期产品信息）
     */
    @PostMapping("/data-center/product/info/deleteProductById")
    ReturnT deleteProductById(@RequestParam("id") String id);


    @GetMapping({"/data-center/product/fileInfo/count"})
    ReturnT<Integer> count();

    /**
     * 产品信息集合
     * @return
     */
    @GetMapping({"/data-center/product/base/list"})
    ReturnT<List<ProductEntity>> productList();

    @PostMapping({"/data-center/category/list"})
    ReturnT<List<DataCategoryEntity>> categoryList(@RequestParam(value = "treeKey",required = false) String treeKey,
                                                   @RequestParam(value = "parentId",required = false) String parentId);
}
