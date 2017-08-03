package com.its.modules.app.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;

import com.its.modules.app.bean.CouponManageBean;
import com.its.modules.app.common.CommonGlobal;
import com.its.modules.app.common.ValidateUtil;
import com.its.modules.app.dao.CouponManageDao;
import com.its.modules.app.entity.CouponManage;

/**
 * 优惠券管理Service
 * 
 * @author sushipeng
 * 
 * @version 2017-07-03
 */
@Service
@Transactional(readOnly = true)
public class CouponManageService extends CrudService<CouponManageDao, CouponManage> {

	@Autowired
	private BusinessInfoService businessInfoService;

	@Autowired
	private ModuleManageService moduleManageService;

	public CouponManage get(String id) {
		return super.get(id);
	}

	public List<CouponManage> findList(CouponManage couponManage) {
		return super.findList(couponManage);
	}

	public Page<CouponManage> findPage(Page<CouponManage> page, CouponManage couponManage) {
		return super.findPage(page, couponManage);
	}

	@Transactional(readOnly = false)
	public void save(CouponManage couponManage) {
		super.save(couponManage);
	}

	/**
	 * 获取某用户某楼盘下的有效优惠券
	 * 
	 * @param villageInfoId
	 *            楼盘ID
	 * @param accountId
	 *            用户ID
	 * @return List<CouponManageBean>
	 */
	public List<CouponManageBean> getValidCoupons(String villageInfoId, String accountId) {
		return dao.getValidCoupons(villageInfoId, accountId);
	}

	/**
	 * 获取某用户某楼盘下的无效优惠券（已使用、已过期、已冻结）
	 * 
	 * @param villageInfoId
	 *            楼盘ID
	 * @param accountId
	 *            用户ID
	 * @return List<CouponManageBean>
	 */
	public List<CouponManageBean> getInvalidCoupons(String villageInfoId, String accountId) {
		return dao.getInvalidCoupons(villageInfoId, accountId);
	}

	/**
	 * 获取某用户某楼盘下的无效优惠券的使用状态（已使用、已过期、已冻结）
	 */
	public String getMemberDiscountStatus(CouponManageBean couponManageBean) {
		if ("0".equals(couponManageBean.getUseState())) {
			return "已过期";
		} else if ("1".equals(couponManageBean.getUseState())) {
			return "已使用";
		} else {
			return "已冻结";
		}
	}

	/**
	 * 获取某楼盘下买家可领取的优惠券
	 * 
	 * @param villageInfoId
	 *            楼盘ID
	 * @return List<CouponManage>
	 */
	public List<CouponManage> getCanReceiveCoupons(String villageInfoId) {
		return dao.getCanReceiveCoupons(villageInfoId);
	}

	/**
	 * 获取某楼盘下买家可领取的优惠券的领取状态（已抢光、已领取、立即领取）
	 */
	public String getCouponStatus(CouponManage couponManage, int receiveNum) {
		// 发放总量：0无限制 1限量发送
		if (CommonGlobal.COUPON_GRANT_TYPE_LIMIT.equals(couponManage.getGrantType())) {
			if (ValidateUtil.validateInteger(couponManage.getLimitedNum()) - ValidateUtil.validateInteger(couponManage.getReceiveNum()) == 0) {
				return "已抢光";
			}
		}
		// 买家领取规则：0无限制 1每人每日限领1张 2每人限领1张
		if (CommonGlobal.COUPON_RECEIVE_RULE_UNLIMIT.equals(couponManage.getReceiveRule())) {
			return "立即领取";
		} else {
			if (receiveNum == 0) {
				return "立即领取";
			} else {
				return "已领取";
			}
		}
	}

	/**
	 * 根据优惠券ID修改优惠券领取总量
	 * 
	 * @param receiveNum
	 *            领取总量
	 * @param couponId
	 *            优惠券ID
	 */
	public void updateReceiveNumById(@Param("receiveNum") Integer receiveNum, @Param("couponId") String couponId) {
		dao.updateReceiveNumById(receiveNum, couponId);
	}

	/**
	 * 获取可用的优惠券
	 * 
	 * @param villageInfoId
	 *            楼盘ID
	 * @param accountId
	 *            用户ID
	 * @param prodType
	 *            产品模式：0商品购买 1服务预约 2课程购买 3场地预约
	 * @param businessInfoId
	 *            商户ID
	 * @param shareFlag
	 *            是否与其它优惠同享：0否 1是 传参规则：（0），（1），（0,1）
	 * @param totalMoney
	 *            订单总金额
	 * @return List<CouponManageBean>
	 */
	public List<CouponManageBean> getEnableCoupons(String villageInfoId, String accountId, int prodType, String businessInfoId, String shareFlag, double totalMoney) {
		return dao.getEnableCoupons(villageInfoId, accountId, prodType, businessInfoId, shareFlag, totalMoney);
	}

	/**
	 * 判断优惠券是否可用
	 * 
	 * @param villageInfoId
	 *            楼盘ID
	 * @param accountId
	 *            用户ID
	 * @param memberDiscountId
	 *            会员的优惠券ID
	 * @return 不可用返回NULL，可用返回优惠券信息
	 */
	public CouponManageBean judgeCoupon(String villageInfoId, String accountId, String memberDiscountId) {
		return dao.judgeCoupon(villageInfoId, accountId, memberDiscountId);
	}

	/**
	 * 计算优惠券优惠金额
	 * 
	 * @param couponManage
	 *            优惠券信息
	 * @param totalMoney
	 *            总金额
	 * @return 优惠券优惠金额
	 */
	public double calCouponMoney(CouponManage couponManage, double totalMoney) {
		double couponMoneyNative = ValidateUtil.validateDouble(couponManage.getCouponMoney());
		double couponMoney = 0;
		// 优惠券类型：0固定金额券 1折扣券
		if (CommonGlobal.COUPON_TYPE_FIXED.equals(couponManage.getCouponType())) {
			couponMoney = couponMoneyNative;
		} else {
			couponMoney = ((100 - couponMoneyNative) / 100) * totalMoney;
			// 折扣券可设置优惠上限，判断折扣券是否存在优惠上限
			if (couponManage.getUpperLimitMoney() != null && couponManage.getUpperLimitMoney() != 0) {
				double upperLimitMoney = ValidateUtil.validateDouble(couponManage.getUpperLimitMoney());
				couponMoney = couponMoney > upperLimitMoney ? upperLimitMoney : couponMoney;
			}
		}
		return couponMoney;
	}

	/**
	 * 获取优惠券名称
	 * 
	 * @param couponManage
	 *            优惠券信息
	 * @return 优惠券名称
	 */
	public String getCouponName(CouponManage couponManage) {
		// 使用范围：0无限制 1服务品类专享 2商家专享
		if (CommonGlobal.COUPON_USE_SCOPE_UNLIMIT.equals(couponManage.getUseScope())) {
			return "通用券";
		} else if (CommonGlobal.COUPON_USE_SCOPE_SERVICE.equals(couponManage.getUseScope())) {
			return moduleManageService.get(couponManage.getUseObject()).getModuleName() + "专享券";
		} else {
			return businessInfoService.get(couponManage.getUseObject()).getBusinessName() + "专享券";
		}
	}

	/**
	 * 获取优惠券金额
	 * 
	 * @param couponManage
	 *            优惠券信息
	 * @return 优惠券金额
	 */
	public String getCouponMoney(CouponManage couponManage) {
		// 优惠券类型：0固定金额券 1折扣券
		double couponMoney = ValidateUtil.validateDouble(couponManage.getCouponMoney());
		if (CommonGlobal.COUPON_TYPE_FIXED.equals(couponManage.getCouponType())) {
			return String.valueOf(couponMoney);
		} else {
			int discount = (int) couponMoney;
			// 个位数
			int prefix = discount / 10;
			// 十分位
			int suffix = discount % 10;
			if (suffix == 0) {
				return prefix + "折";
			} else {
				return prefix + "." + suffix + "折";
			}
		}
	}

	/**
	 * 获取优惠券使用条件
	 * 
	 * @param couponManage
	 *            优惠券信息
	 * @return 优惠券使用条件
	 */
	public String getCouponCondition(CouponManage couponManage) {
		// 使用条件：0无限制 1满额使用
		if (CommonGlobal.COUPON_USE_RULE_UNLIMIT.equals(couponManage.getUseRule())) {
			return "无限制";
		} else {
			return "满" + ValidateUtil.validateDouble(couponManage.getFullUseMoney()) + "元可用";
		}
	}

	/**
	 * 修改会员优惠券的使用状态
	 * 
	 * @param id
	 *            会员优惠券ID
	 * @param newState
	 *            使用状态：0未使用；1已使用；2已过期；3已冻结
	 */
	public void updateUserState(String id, String newState) {
		dao.updateUserState(id, newState);
	}
}