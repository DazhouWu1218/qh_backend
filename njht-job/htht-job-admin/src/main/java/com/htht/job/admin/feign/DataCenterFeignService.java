package com.htht.job.admin.feign;

import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.utils.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 代国军
 * @description: 数据中心远程调度service 获取任务目录
 * @date 2022/7/26 15:08
 */
@FeignClient("njht-data-center")
public interface DataCenterFeignService {

    @PostMapping({"/data-center/category/list"})
    ReturnT<List<DataCategoryEntity>> categoryList(@RequestParam(value = "treeKey",required = false) String treeKey,
                                                   @RequestParam(value = "parentId",required = false) String parentId);

    @GetMapping({"/data-center/category/treeList"})
    ReturnT<List<DataCategoryEntity>> treeList();

    @PostMapping({"/data-center/category/getProductId"})
    ReturnT<String> getProductId(@RequestParam(value = "treeId",required = false) String treeId);
}
