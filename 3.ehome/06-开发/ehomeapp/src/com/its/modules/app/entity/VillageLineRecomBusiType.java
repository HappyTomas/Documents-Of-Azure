/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.its.common.persistence.DataEntity;

/**
 * 楼盘产品线推荐商家模式Entity
 * @author sushipeng
 * @version 2017-07-31
 */
public class VillageLineRecomBusiType extends DataEntity<VillageLineRecomBusiType> {
	
	private static final long serialVersionUID = 1L;
	private String villageLineId;		// 楼盘产品线ID
	private String recomPosition;		// 推荐位置：00 首页推荐  10 社区推荐   20 生活商家推荐2
	private Integer sortNum;		// 排序序号
	private String recomBusinessId;		// 推荐商家ID
	
	public VillageLineRecomBusiType() {
		super();
	}

	public VillageLineRecomBusiType(String id){
		super(id);
	}

	@Length(min=0, max=64, message="楼盘产品线ID长度必须介于 0 和 64 之间")
	public String getVillageLineId() {
		return villageLineId;
	}

	public void setVillageLineId(String villageLineId) {
		this.villageLineId = villageLineId;
	}
	
	@Length(min=0, max=2, message="推荐位置：00 首页推荐  10 社区推荐   20 生活商家推荐2长度必须介于 0 和 2 之间")
	public String getRecomPosition() {
		return recomPosition;
	}

	public void setRecomPosition(String recomPosition) {
		this.recomPosition = recomPosition;
	}
	
	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	
	@Length(min=0, max=64, message="推荐商家ID长度必须介于 0 和 64 之间")
	public String getRecomBusinessId() {
		return recomBusinessId;
	}

	public void setRecomBusinessId(String recomBusinessId) {
		this.recomBusinessId = recomBusinessId;
	}
	
}