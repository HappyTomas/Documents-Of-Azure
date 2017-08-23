/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.sys.dao;

import java.util.List;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author Jetty
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);
	/**
	 * 根据登录名称查询商家用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName2(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 查找当前系统下的用户
	 * @param user
	 * @return
	 */
	public List<User> findHomeUserList(User user);
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 依据角色，删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteRoleUser(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/**
	 * 更新用户登录标记 
	 * @author zhujiao
	 * @since 2017年6月20日上午10:49:07
	 * @param user
	 * @return
	 */
	public int updateLoginFlag(User user);
	/**
	 * 通过商家ID获取所有用户信息---商家信息
	 * @param businessId
	 * @return
	 * @return List<User>
	 * @author zhujiao   
	 * @date 2017年8月19日 下午12:39:09
	 */
	public List<User> getUserListByBusiness(String businessId);
}
