/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.operation.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.operation.dao.MemberDiscountDao;
import com.its.modules.operation.entity.MemberDiscount;

/**
 * 会员的优惠券Service
 * 
 * @author liuqi
 * @version 2017-07-05
 */
@Service
@Transactional(readOnly = true)
public class MemberDiscountService extends CrudService<MemberDiscountDao, MemberDiscount> {

	public MemberDiscount get(String id) {
		return super.get(id);
	}

	public List<MemberDiscount> findList(MemberDiscount memberDiscount) {
		return super.findList(memberDiscount);
	}

	public Page<MemberDiscount> findPage(Page<MemberDiscount> page, MemberDiscount memberDiscount) {
		return super.findPage(page, memberDiscount);
	}

	@Transactional(readOnly = false)
	public void save(MemberDiscount memberDiscount) {
		super.save(memberDiscount);
	}

	@Transactional(readOnly = false)
	public void delete(MemberDiscount memberDiscount) {
		super.delete(memberDiscount);
	}

	/**
	 * 根据当前时间及数据库中优惠券号流水生成新的优惠券号
	 * 
	 * @return 下一个优惠券号
	 */
	public String getNextDiscountId() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String dateStr = sdf.format(c.getTime());
		System.out.println(dateStr);
		Long maxDiscountId = dao.getNextDiscountId();
		String nextDiscountId = new String();
		if(maxDiscountId == null){ // 系统中还没有优惠券号
			nextDiscountId = dateStr+"000001";
		} else {
			maxDiscountId++;
			nextDiscountId = dateStr + maxDiscountId.toString().substring(6);
		}

		return nextDiscountId;
	}

	/*public static void main(String[] args) {
		MemberDiscountService m = new MemberDiscountService();
		m.getNextDiscountId();
	}*/
}