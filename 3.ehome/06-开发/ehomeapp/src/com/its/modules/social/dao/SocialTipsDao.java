/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.social.entity.SocialTips;

/**
 * 消息提醒DAO接口
 * @author 刘浩浩
 * @version 2017-08-07
 */
@MyBatisDao
public interface SocialTipsDao extends CrudDao<SocialTips> {
	
	/**
	 * 
	 * @Description：根据用户id和提醒类型查询所有数据
	 * @Author：邵德才
	 * @Date：2017年8月9日
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<SocialTips> getListByUserIdAndType(@Param("userId")String userId, @Param("type")int type, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
}