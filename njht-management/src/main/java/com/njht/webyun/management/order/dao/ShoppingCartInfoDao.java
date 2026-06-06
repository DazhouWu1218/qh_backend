package com.njht.webyun.management.order.dao;


import com.njht.webyun.management.order.entity.ShoppingcartInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lmd
 */
@Repository
public interface ShoppingCartInfoDao {
	
	public List<ShoppingcartInfo> selectShoppingcart(String userId);

	public void insertCartInfo(@Param("userId") String userId);

	/**
	 * 获取购物车信息
	 * @param dataId
	 * @param cartId
	 * @return
	 */
    String getShoppingInfoByDataId(@Param("dataId")String dataId,@Param("cartId")String cartId);

	/**
	 * 修改购物车中卫星数据相关的数据dateType
	 * @param dataId
	 * @param dataType
	 * @param cartId
	 */
	void updateSatelliteShopCartInfo(@Param("dataId")String dataId, @Param("dataType")String dataType,@Param("cartId")String cartId);
}
