/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.subject.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.subject.entity.SocialSubject;

/**
 * 话题管理DAO接口
 * @author wmm
 * @version 2017-07-31
 */
@MyBatisDao
public interface SocialSubjectDao extends CrudDao<SocialSubject> {
	
	/**
	 * @Description：保存排序
	 * @Author：王萌萌
	 * @Date：2017年8月11日
	 * @param socialSubject
	 * @return
	 */
	public int updateSortNum(SocialSubject socialSubject);
	
	/**
	 * @Description：是否推荐
	 * @Author：王萌萌
	 * @Date：2017年8月11日
	 * @param socialSubject
	 * @return
	 */
	public int updateRecommend(SocialSubject socialSubject);
	
	/**
	 * @Description：查询所有话题
	 * @Author：王萌萌
	 * @Date：2017年8月11日
	 * @return
	 */
	public List<SocialSubject> findAll();
	
	/**
	 * @Description：根据发言id查询话题
	 * @Author：王萌萌
	 * @Date：2017年8月11日
	 * @param speakId
	 * @return
	 */
	public List<SocialSubject> findSubListBySpeakId(@Param("speakId") String speakId);

	/**
	 * @Description：根据话题名称查询话题
	 * @Author：王萌萌
	 * @Date：2017年8月23日
	 * @param tag
	 * @return
	 */
	public SocialSubject findByTag(@Param("tag") String tag);
	
}