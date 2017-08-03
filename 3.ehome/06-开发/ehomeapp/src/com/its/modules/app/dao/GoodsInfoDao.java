/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.app.entity.BusinessInfo;
import com.its.modules.app.entity.GoodsInfo;
import com.its.modules.app.entity.SortInfo;

/**
 * 商品信息DAO接口
 * 
 * @author like
 * @version 2017-07-05
 */
@MyBatisDao
public interface GoodsInfoDao extends CrudDao<GoodsInfo> {
	/**
	 * 获取每个商家推荐的3个商品
	 * 
	 * @param businessInfoList
	 * @return
	 */
	public List<GoodsInfo> getBusinessRecomendGoodsList(List<BusinessInfo> businessInfoList);

	/**
	 * 获取商家商品分类集合
	 * 
	 * @param businessInfoID
	 * @return
	 */
	public List<SortInfo> getBusinessSortInfoList(String businessInfoID);

	/**
	 * 获取某一分类的商品集合
	 * 
	 * @param SortInfoID
	 * @return
	 */
	public List<GoodsInfo> getGoodsInfoBySortList(@Param("sortInfoID") String sortInfoID, @Param("businessInfoID") String businessInfoID);

	/**
	 * 获取商家自定单位名称
	 * 
	 * @param id
	 * @return
	 */
	public String getUnitNameCustom(String id);

	/**
	 * 获取系统单位名称
	 * 
	 * @param id
	 * @return
	 */
	public String getUnitNameSystem(String id);

	/**
	 * 减少商品库存数量
	 * 
	 * @param id
	 * @param reduce
	 * @return 修改的记录数
	 */
	public int reduceGoodsInfoStock(@Param("id") String goodsInfoId, @Param("reduce") int reduce);

	/**
	 * 减少商品规格库存数量
	 * 
	 * @param goodsSkuPriceId
	 * @param reduce
	 * @return 修改的记录数
	 */
	public int reduceGoodsSkuPriceStock(@Param("id") String goodsSkuPriceId, @Param("reduce") int reduce);

	/**
	 * 更新商品库存，已售数量
	 * 
	 * @param count
	 *            操作数量
	 * @param goodsInfoId
	 *            商品ID
	 * @return 操作的行数
	 */
	public int updateStockAndSellCount(@Param("count") int count, @Param("goodsInfoId") String goodsInfoId);
}