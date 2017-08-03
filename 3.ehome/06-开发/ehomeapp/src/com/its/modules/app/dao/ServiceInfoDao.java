package com.its.modules.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.app.entity.ServiceInfo;

/**
 * 服务管理DAO接口
 * 
 * @author sushipeng
 * 
 * @version 2017-07-05
 */
@MyBatisDao
public interface ServiceInfoDao extends CrudDao<ServiceInfo> {

	/**
	 * 根据商户信息ID获取商家服务项目
	 * 
	 * @param businessInfoId
	 *            商户信息ID
	 * @param showCount
	 *            查询数量
	 * @return List<ServiceInfo>
	 */
	public List<ServiceInfo> getByBusinessId(@Param("businessInfoId") String businessInfoId, @Param("showCount") int showCount);

	/**
	 * 根据商家ID和服务分类ID获取商家服务项目（不包含下架或删除的服务）
	 * 
	 * @param serviceInfo
	 *            服务信息
	 * @return List<ServiceInfo>
	 */
	public List<ServiceInfo> getByBusinessIdAndSortInfoId(ServiceInfo serviceInfo);
}