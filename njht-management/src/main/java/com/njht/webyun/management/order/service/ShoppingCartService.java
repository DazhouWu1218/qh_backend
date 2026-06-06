package com.njht.webyun.management.order.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

/**
 * @author lmd
 */
public interface ShoppingCartService {

    List<Map<String,Object>> queryShoppingCart(String usreId) throws JsonProcessingException;

    boolean addGoods(String userId, String type, List<Map<String, String>> datas) throws Exception;
}
