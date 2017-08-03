package com.its.modules.app.dao;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.app.entity.GroupPurchasetime;

/**
 * 团购管理子表－团购时间DAO接口
 * 
 * @author sushipeng
 * 
 * @version 2017-07-24
 */
@MyBatisDao
public interface GroupPurchasetimeDao extends CrudDao<GroupPurchasetime> {

	/**
	 * 更新团购子表已售数量
	 * 
	 * @param count
	 *            变动的数值
	 * @param groupPurchaseTimeId
	 *            团购子表ID
	 * @return 操作的行数
	 */
	public int updateSaleNum(@Param("count") int count, @Param("groupPurchaseTimeId") String groupPurchaseTimeId);
}