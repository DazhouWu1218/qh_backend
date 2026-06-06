package com.njht.webyun.management.order.dao;


import com.njht.webyun.management.order.entity.ShoppingCartList;
import com.njht.webyun.management.satellite.vo.SatelliteThematicDataInfoDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lmd
 */
@Repository
public interface ShoppingCartListDao {
	
	void updateShoppingCart(@Param("cartId") String cartId,@Param("dataId") String dataId,@Param("identifier") String identifier);

	List<ShoppingCartList> selectList(@Param("cartId") String cartId);

	void deleteDatas(@Param("cartId") String cartId, @Param("dataId") String dataId);

    List<ShoppingCartList> queryCartDataIdList(@Param("id") String id);

	String queryDataTableName(@Param("identifier") String identifier);

	Map<String, Object> queryBusinessDataList(@Param("dataId") String dataId, @Param("tableName") String tableName);

	Map<String, Object> querySateDataList(@Param("dataId") String dataId, @Param("tableName") String tableName);

	String selectBasicDataList(@Param("dataId") String dataId, @Param("tableName") String tableName);

	void updateShoppingCartList(@Param("datas") List<Map<String, String>> datas);

	void deleteDatas2(@Param("cartId") String cartId,@Param("dataIdList") List<String> dataIdList);

	/**
	 * 更新专题数据购物车数据
	 * @param datas
	 */
    void updateThematicShoppingCartList(@Param("datas")List<Map<String, String>> datas);

	/**
	 * 查询专题file_info表数据
	 * @param dataId
	 * @return
	 */
	Map<String, Object> selectThematicDataList(@Param("dataId") String dataId);

	/**
	 * 查询影像数
	 * @param dataId
	 * @return
	 */
	List<Map<String, Object>> selectImageData(String dataId);

	/**
	 * 获取购物车下的卫星数据的专题数据
	 * @param dataId
	 * @param tableName
	 * @return
	 */
	SatelliteThematicDataInfoDTO querySatelliteThematicdDataList(@Param(value = "dataId") String dataId, @Param(value = "tableName")String tableName);

	/**
	 * 查询产品父名称
	 * @param dataId
	 * @return
	 */
	String getBusinessProductParentName(String dataId);
}
