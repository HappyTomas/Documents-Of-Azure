/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.app.dao;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.app.entity.AdverPosition;

/**
 * 广告位置管理DAO接口
 * @author like
 * @version 2017-07-28
 */
@MyBatisDao
public interface AdverPositionDao extends CrudDao<AdverPosition> {
	
}