/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.operation.dao;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.operation.entity.GroupPurchasetime;

/**
 * 团购管理DAO接口
 * @author caojing
 * @version 2017-06-28
 */
@MyBatisDao
public interface GroupPurchasetimeDao extends CrudDao<GroupPurchasetime> {
	
	/**
	 * 团购管理：团购活动删除用，取团购时间条数
	 * @param groupPurchase
	 */
	public int countTime(GroupPurchasetime groupPurchasetime);

	/**
	 * 团购管理：删除团购时间信息
	 * @param groupPurchase
	 */
	public void deleteGroupPurchasetime(GroupPurchasetime groupPurchasetime);
	
}