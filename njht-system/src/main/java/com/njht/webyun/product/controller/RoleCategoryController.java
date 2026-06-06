package com.njht.webyun.product.controller;

import com.njht.webyun.product.service.RoleCategoryService;
import com.njht.webyun.product.vo.CategoryVo;
import com.njht.webyun.product.vo.ProductRoleTree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2022/4/18 18:50
 * @Description: 产品目录权限控制
 */
@RestController
@RequestMapping("/system/role/tree")
@Api(value = "角色-产品管理")
public class RoleCategoryController {

    @Autowired
    private RoleCategoryService roleCategoryService;

    @ApiOperation(value = "分类树结构")
    @ApiImplicitParam(paramType="String", name="roleId", dataType="String", required=true, value="角色id")
    @PostMapping({"/list"})
    @ResponseBody
    public List<ProductRoleTree> getCategoryTree(@RequestParam("roleId") Integer roleId){
        return roleCategoryService.getCategoryTree(roleId);
    }

    /**
     * 修改产品的勾选状态
     * @param categoryVo 修改树结构勾选信息 参数
     * @return
     */
    @PostMapping({"/update"})
    @ResponseBody
    public void updateRoleCategory(@Validated @RequestBody CategoryVo categoryVo){
        roleCategoryService.updateRoleCategory(categoryVo);

    }
}
