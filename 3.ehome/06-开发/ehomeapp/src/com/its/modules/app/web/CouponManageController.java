package com.its.modules.app.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.config.Global;
import com.its.common.web.BaseController;
import com.its.modules.app.bean.CouponManageBean;
import com.its.modules.app.common.CommonGlobal;
import com.its.modules.app.common.ValidateUtil;
import com.its.modules.app.entity.Account;
import com.its.modules.app.entity.CouponManage;
import com.its.modules.app.entity.MemberDiscount;
import com.its.modules.app.service.AccountService;
import com.its.modules.app.service.CouponManageService;
import com.its.modules.app.service.MemberDiscountService;

/**
 * 优惠券管理Controller
 * 
 * @author sushipeng
 * 
 * @version 2017-07-03
 */
@Controller
@RequestMapping(value = { "${appPath}/home", "${appPath}/my" })
public class CouponManageController extends BaseController {

	@Autowired
	private CouponManageService couponManageService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private MemberDiscountService memberDiscountService;

	/**
	 * 我的有效优惠劵
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getValidCoupon", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getValidCoupon(String userID, String buildingID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID)) {
			return toJson;
		}
		Account account = accountService.get(userID);
		List<CouponManageBean> couponManageBeans = couponManageService.getValidCoupons(buildingID, userID);
		if (couponManageBeans == null || couponManageBeans.size() == 0) {
			toJson.put("code", Global.CODE_SUCCESS);
			toJson.put("message", "暂无数据");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (CouponManageBean couponManageBean : couponManageBeans) {
			Map<String, Object> data = new HashMap<String, Object>();
			// 传递会员的优惠券ID（不传递优惠券ID）
			data.put("couponID", couponManageBean.getMemberDiscountId());
			data.put("couponMoney", couponManageService.getCouponMoney(couponManageBean));
			data.put("couponName", couponManageService.getCouponName(couponManageBean));
			data.put("couponCondition", ("0".equals(couponManageBean.getUseRule()) ? "无限制" : "满" + couponManageBean.getFullUseMoney() + "元可用"));
			data.put("limitUser", account.getPhoneNum());
			data.put("couponCap", couponManageBean.getUpperLimitMoney());

			datas.add(data);
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 我的无效优惠劵
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getInvalidCoupon", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getInvalidCoupon(String userID, String buildingID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID)) {
			return toJson;
		}
		Account account = accountService.get(userID);
		List<CouponManageBean> couponManageBeans = couponManageService.getInvalidCoupons(buildingID, userID);
		if (couponManageBeans == null || couponManageBeans.size() == 0) {
			toJson.put("code", Global.CODE_SUCCESS);
			toJson.put("message", "暂无数据");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (CouponManageBean couponManageBean : couponManageBeans) {
			Map<String, Object> data = new HashMap<String, Object>();
			// 传递会员的优惠券ID（不传递优惠券ID）
			data.put("couponID", couponManageBean.getMemberDiscountId());
			data.put("couponMoney", couponManageService.getCouponMoney(couponManageBean));
			data.put("couponName", couponManageService.getCouponName(couponManageBean));
			data.put("couponCondition", ("0".equals(couponManageBean.getUseRule()) ? "无限制" : "满" + couponManageBean.getFullUseMoney() + "元可用"));
			data.put("limitUser", account.getPhoneNum());
			data.put("couponCap", couponManageBean.getUpperLimitMoney());
			data.put("couponStatus", couponManageService.getMemberDiscountStatus(couponManageBean));

			datas.add(data);
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 获取优惠券信息（领券中心）
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getCoupon")
	@ResponseBody
	public Map<String, Object> getCoupon(String userID, String buildingID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID)) {
			return toJson;
		}
		List<CouponManage> couponManages = couponManageService.getCanReceiveCoupons(buildingID);
		if (couponManages == null || couponManages.size() == 0) {
			toJson.put("code", Global.CODE_SUCCESS);
			toJson.put("message", "暂无数据");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (CouponManage couponManage : couponManages) {
			Map<String, Object> data = new HashMap<String, Object>();
			// 传递优惠券ID（不传递会员的优惠券ID）
			data.put("couponID", couponManage.getId());
			data.put("couponMoney", couponManageService.getCouponMoney(couponManage));
			data.put("couponName", couponManageService.getCouponName(couponManage));
			data.put("couponCondition", ("0".equals(couponManage.getUseRule()) ? "无限制" : "满" + couponManage.getFullUseMoney() + "元可用"));
			int receiveNum = 0;
			// 买家领取规则：0无限制 1每人每日限领1张 2每人限领1张
			if ("1".equals(couponManage.getReceiveRule())) {
				receiveNum = memberDiscountService.getTodayReceiveCount(buildingID, userID, couponManage.getId());
			} else if ("2".equals(couponManage.getReceiveRule())) {
				receiveNum = memberDiscountService.getReceiveCount(buildingID, userID, couponManage.getId());
			}
			data.put("couponStatus", couponManageService.getCouponStatus(couponManage, receiveNum));

			datas.add(data);
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 领取优惠券（买家领取）
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param conponID
	 *            优惠劵ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "receiveCoupon", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> receiveCoupon(String userID, String conponID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, conponID)) {
			return toJson;
		}
		CouponManage couponManage = couponManageService.get(conponID);
		if (couponManage == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "优惠券不存在");
			return toJson;
		}

		MemberDiscount memberDiscount = new MemberDiscount();
		memberDiscount.setVillageInfoId(couponManage.getVillageInfoId());
		memberDiscount.setDiscountId(couponManage.getId());
		// 优惠券号
		memberDiscount.setDiscountNum(null);
		memberDiscount.setAccountId(userID);
		memberDiscount.setObtainDate(new Date());
		// 计算优惠券有效时间
		Date validStart = null;
		Date validEnd = null;
		if ("0".equals(couponManage.getValidityType())) {
			validStart = couponManage.getValidityStartTime();
			validEnd = couponManage.getValidityEndTime();
		} else {
			validStart = new Date();
		}
		memberDiscount.setValidStart(validStart);
		memberDiscount.setValidEnd(validEnd);
		memberDiscount.setUseState(CommonGlobal.DISCOUNT_USE_STATE_UNUSED);
		memberDiscount.setReceiveType(CommonGlobal.COUPON_RECEIVE_TYPE_RECEIVE);
		// 新增会员的优惠券
		memberDiscountService.save(memberDiscount);
		// 修改优惠券领取总量
		couponManageService.updateReceiveNumById(couponManage.getReceiveNum() + 1, couponManage.getId());

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("couponMoney", couponManageService.getCouponMoney(couponManage));
		data.put("couponName", couponManageService.getCouponName(couponManage));
		data.put("couponCondition", ("0".equals(couponManage.getUseRule()) ? "无限制" : "满" + couponManage.getFullUseMoney() + "元可用"));
		data.put("couponValidDate", memberDiscount.getValidStart() + "至" + memberDiscount.getValidEnd());

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "领取成功");
		return toJson;
	}
}