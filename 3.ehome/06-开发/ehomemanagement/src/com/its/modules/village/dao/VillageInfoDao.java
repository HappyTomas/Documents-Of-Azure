/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.village.dao;

import java.util.List;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.village.entity.VillageInfo;

/**
 * 楼盘信息DAO接口
 * 
 * @author zhujiao
 * @version 2017-07-03
 */
@MyBatisDao
public interface VillageInfoDao extends CrudDao<VillageInfo> {
	/**
	 * 查询楼盘全部省份
	 * 
	 * @author zhujiao
	 * @param user
	 * @return
	 */
	public List<VillageInfo> findPro(VillageInfo villageInfo);

	/**
	 * 根据省份获取城市数据
	 * 
	 * @author zhujiao
	 * @param user
	 * @return
	 */
	public List<VillageInfo> findCity(VillageInfo villageInfo);

	/**
	 * 查询城市获取楼盘数据
	 * 
	 * @author zhujiao
	 * @param user
	 * @return
	 */
	public List<VillageInfo> findVillage(VillageInfo villageInfo);
	
	/**
	 * 查询城市获取楼盘数据
	 * 
	 * @author zhujiao
	 * @param user
	 * @return
	 */
	public List<VillageInfo> findUserVillage(VillageInfo villageInfo);
	
	/**
	 * 查询楼盘（查询楼栋信息接口定时任务开始用）
	 * @param villageInfo
	 * @return
	 */
	public List<VillageInfo> getVillageList(VillageInfo villageInfo);
	/**
	 * 更新楼盘状态
	 * @author ChenXiangyu
	 * @param villageInfo
	 * @return
	 */
	public int updateState(VillageInfo villageInfo);
}