package com.its.modules.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.app.entity.LessonInfo;

/**
 * 课程培训DAO接口
 * 
 * @author sushipeng
 * 
 * @version 2017-07-10
 */
@MyBatisDao
public interface LessonInfoDao extends CrudDao<LessonInfo> {

	/**
	 * 根据商户信息ID获取商家课程项目
	 * 
	 * @param businessInfoId
	 *            商户信息ID
	 * @param showCount
	 *            查询数量
	 * @return List<LessonInfo>
	 */
	public List<LessonInfo> getByBusinessId(@Param("businessInfoId") String businessInfoId, @Param("showCount") Integer showCount);

	/**
	 * 根据商户信息ID获取商家课程项目
	 * 
	 * @param businessInfoId
	 *            商户信息ID
	 * @return List<LessonInfo>
	 */
	public List<LessonInfo> getNoLimitList(@Param("businessInfoId") String businessInfoId, @Param("pageIndex") int pageIndex, @Param("numPerPage") int numPerPage);

	/**
	 * 更新课程库存、已购数量
	 * 
	 * @param lessonInfoId
	 *            课程ID
	 * @return 操作的行数
	 */
	public int updateSellCount(String lessonInfoId);
	
	/**
	 * 根据课程id获取非下架的课程
	 * @param id
	 * 			课程id
	 * @return	
	 * 			课程信息
	 */
	public LessonInfo getValidateById(String id);
}