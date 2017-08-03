/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.app.entity.OrderGoodsList;
import com.its.modules.app.dao.OrderGoodsListDao;

/**
 * 订单商品明细表Service
 * @author like
 * @version 2017-07-10
 */
@Service
@Transactional(readOnly = true)
public class OrderGoodsListService extends CrudService<OrderGoodsListDao, OrderGoodsList> {

	public OrderGoodsList get(String id) {
		return super.get(id);
	}
	
	public List<OrderGoodsList> findList(OrderGoodsList orderGoodsList) {
		return super.findList(orderGoodsList);
	}
	
	public Page<OrderGoodsList> findPage(Page<OrderGoodsList> page, OrderGoodsList orderGoodsList) {
		return super.findPage(page, orderGoodsList);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderGoodsList orderGoodsList) {
		super.save(orderGoodsList);
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderGoodsList orderGoodsList) {
		super.delete(orderGoodsList);
	}
	
}