/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.social.bean.SocialCommentBean;
import com.its.modules.social.entity.SocialComment;

/**
 * 评论DAO接口
 * @author wmm
 * @version 2017-08-04
 */
@MyBatisDao
public interface SocialCommentDao extends CrudDao<SocialComment> {
	
	/**
	 * @Description：根据发言ID获取转发bean集合
	 * @Author：刘浩浩
	 * @Date：2017年8月4日
	 * @param userId 用户id
	 * @param socialComment 查询参数
	 * @param pageIndex 分页页码(从0开始为起始页)
	 * @param pageSize
	 * @return
	 */
	public List<SocialCommentBean> findCommentBeanList(@Param("userId") String userId, @Param("socialComment") SocialComment socialComment, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
	
	/**
	 * @Description：根据发言id获取一级评论个数
	 * @Author：刘浩浩
	 * @Date：2017年8月4日
	 * @param speakId
	 * @return
	 */
	public int getSecCmtCount(@Param("commentId") String commentId);
	
	/**
	 * @Description 根据评论ID获取评论详情
	 * @Author：王萌萌
	 * @Date：2017年8月7日
	 * @param socialComment 查询参数
	 * @return
	 */
	public SocialCommentBean findCommentBean(@Param("socialComment") SocialComment socialComment);
	
	/**
	 * @Description：根据评论ID获取字评论列表
	 * @Author：王萌萌
	 * @Date：2017年8月7日
	 * @param userId 用户id
	 * @param socialComment 查询参数
	 * @param pageIndex 分页页码（从0开始为起始页）
	 * @param pageSize
	 * @return
	 */
	public List<SocialCommentBean> findSecCommentBeanList(@Param("userId") String userId, @Param("socialComment") SocialComment socialComment, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 
	 * @Description：根据发言id得到评论数量
	 * @Author：邵德才
	 * @Date：2017年8月7日
	 * @param speakid
	 * @return
	 */
	public int commentCount(@Param("speakid") String speakid);

	/**
	 * 
	 * @Description：根据userId找到所有数据
	 * @Author：邵德才
	 * @Date：2017年8月9日
	 * @param socialComment
	 * @return
	 */
	public List<SocialComment> findListByUserId(@Param("userId")String userId, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 
	 * @Description：根据id删除当前评论（逻辑删除）
	 * @Author：邵德才
	 * @Date：2017年8月10日
	 * @param id
	 */
	public void deleteComment(@Param("id")String id);
}