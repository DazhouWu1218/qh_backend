package com.njht.webyun.management.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.njht.webyun.management.order.entity.UpdateShoppingCartInfo;
import com.njht.webyun.management.order.entity.UserUnCommitData;
import com.njht.webyun.management.order.service.ShoppingCartService;
import com.njht.webyun.utils.ReturnT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author lmd
 */
@Api(tags = "购物车")
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService shoppingCartService;

	/**
	 * 更新购物车数据
	 * @param updateShoppingCartInfo
	 * @return
	 */
	@RequestMapping("/updateGoods")
	@ResponseBody
	public ReturnT<Object> queryDataSize(@RequestBody UpdateShoppingCartInfo updateShoppingCartInfo) {
		try {
			if (shoppingCartService.addGoods(updateShoppingCartInfo.getUserId(),updateShoppingCartInfo.getType(),updateShoppingCartInfo.getDatas())) {
				return new ReturnT<>(ReturnT.SUCCESS_CODE,"","success");
			}
		} catch (Exception e) {
			Throwable cause = e.getCause();
			return new ReturnT<>(0,cause.getMessage(),"success");
		}
		return new ReturnT<>(ReturnT.FAIL_CODE,"","Operate Failed");
	}


	/**
	 *showdoc
	 *@catalog 业务管理子系统/数据检索
	 *@title 获取个人购物车清单
	 *@description 个人购物车清单
	 *@method post
	 *@url http://192.168.1.199:8088/uus/shoppingCart/getUserCart
	 *@return_param code int 返回码
	 *@return_param msg string 返回信息
	 *@return_param content Object 返回数据体
	 *@remark
	 *@number 99
	 */
	@ApiOperation(value = "获取用户购物车信息")
	@PostMapping("/getUserCart")
	@ResponseBody
	public ReturnT<List<Map<String,Object>>> queryShoppingCart(@RequestBody UserUnCommitData userUnCommitData) throws JsonProcessingException {
		return new ReturnT<>(ReturnT.SUCCESS_CODE,shoppingCartService.queryShoppingCart(userUnCommitData.getUserId()),"success");
	}

}