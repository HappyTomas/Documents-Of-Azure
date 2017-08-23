package com.its.modules.app.common;

/**
 * 通用字典表
 */
public class CommonGlobal {
	/** 否 */
	public static final String NO = "0";
	/** 是 */
	public static final String YES = "1";
	/** 男 */
	public static final String MAN = "0";
	/** 女 */
	public static final String WOMAN = "1";

	/* <=============================场地预约状态=============================> */
	/** 场地预约状态：可预约 */
	public static final String FIELD_APPOINTMENT_STATE_WAITING = "0";
	/** 场地预约状态：已预约 */
	public static final String FIELD_APPOINTMENT_STATE_ALREADY = "1";
	/** 场地预约状态：已消费 */
	public static final String FIELD_APPOINTMENT_STATE_PAYED = "2";

	/* <============================钱包明细交易类型============================> */
	/** 钱包明细交易类型：0-充值 */
	public static final String WALLET_TRADE_TYPE_RECHARGE = "0";
	/** 钱包明细交易类型：1-充值赠送 */
	public static final String WALLET_TRADE_TYPE_PRESENT = "1";
	/** 钱包明细交易类型：2-钱包支付(订单消费) */
	public static final String WALLET_TRADE_TYPE_ORDER_PAY = "2";
	/** 钱包明细交易类型：3-手环支付 */
	public static final String WALLET_TRADE_TYPE_BRACELET = "3";
	/** 钱包明细交易类型：4-刷脸支付 */
	public static final String WALLET_TRADE_TYPE_FACE = "4";
	/** 钱包明细交易类型：5-退款(订单取消) */
	public static final String WALLET_TRADE_TYPE_REFUND = "5";

	/* <============================钱包明细终端来源============================> */
	/** 钱包明细终端来源：0-Android */
	public static final String WALLET_TERMINAL_SOURCE_ANDROID = "0";
	/** 钱包明细终端来源：1-IOS */
	public static final String WALLET_TERMINAL_SOURCE_IOS = "1";

	/* <============================钱包明细支付方式============================> */
	/** 钱包明细支付方式：0-余额支付 */
	public static final String WALLET_PAY_TYPE_BALANCE = "0";
	/** 钱包明细支付方式：1-微信支付 */
	public static final String WALLET_PAY_TYPE_WECHAT = "1";
	/** 钱包明细支付方式：2-支付宝支付 */
	public static final String WALLET_PAY_TYPE_ALIPAY = "2";

	/* <============================访客邀请状态描述============================> */
	/** 访客邀请状态描述：正常 */
	public static final String VISITOR_INVITE_STATE_NOMAL = "0";
	/** 访客邀请状态描述：已作废 */
	public static final String VISITOR_INVITE_STATE_ABOLISHED = "1";
	/** 访客邀请状态描述：已过期 */
	public static final String VISITOR_INVITE_STATE_EXPIRED = "2";

	/* <============================访客邀请性别描述============================> */
	/** 访客邀请性别描述：男 */
	public static final String VISITOR_INVITE_SEX_MAN = "先生";
	/** 访客邀请性别描述：女 */
	public static final String VISITOR_INVITE_SEX_WOMAN = "女士";

	/* <============================我的订单筛选模块============================> */
	/** 全部订单：模块ID */
	public static final String ALL_ORDER_MODULEID = "-1";
	/** 全部订单：文字描述 */
	public static final String ALL_ORDER_DESC = "全部订单";
	/** 精品团购 ：模块ID */
	public static final String GROUP_PURCHASE_MODULEID = "-2";
	/** 精品团购：文字描述 */
	public static final String GROUP_PURCHASE_DESC = "精品团购";

	/* <===============================推荐类型===============================> */
	/** 推荐类型：商家 */
	public static final String RECOMMEND_TYPE_BUSINESS = "0";
	/** 推荐类型：模块 */
	public static final String RECOMMEND_TYPE_MODULE = "1";

	/* <===============================推荐位置===============================> */
	/** 推荐位置：首页 */
	public static final String RECOMMEND_MAINT = "00";
	/** 推荐位置：社区 */
	public static final String RECOMMEND_COMMUNITY = "10";
	/** 推荐位置：社区更多 */
	public static final String RECOMMEND_COMMUNITY_MORE = "11";
	/** 推荐位置：生活 */
	public static final String RECOMMEND_LIFE = "20";

	/* <===============================模块类型===============================> */
	/** 模块类型：主导航 */
	public static final String MODULE_TYPE_MAIN = "0";
	/** 模块类型：社区 */
	public static final String MODULE_TYPE_COMMUNITY = "1";
	/** 模块类型：生活 */
	public static final String MODULE_TYPE_LIFE = "2";

	/* <==============================字典表TYPE==============================> */
	/** 字典表TYPE：mainNavigation */
	public static final String MAIN_NAVIGATION = "mainNavigation";

	/* <==============================地址管理类型==============================> */
	/** 地址类型：按楼栋选择地址 */
	public static final String ADDRESS_TYPE_CHOICE = "0";
	/** 地址类型：手动输入地址 */
	public static final String ADDRESS_TYPE_INPUT = "1";

	/* <==============================优惠券使用状态==============================> */
	/** 会员优惠券使用状态：未使用 */
	public static final String DISCOUNT_USE_STATE_UNUSED = "0";
	/** 会员优惠券使用状态：已使用 */
	public static final String DISCOUNT_USE_STATE_USED = "1";
	/** 会员优惠券使用状态：已过期 */
	public static final String DISCOUNT_USE_STATE_EXPIRE = "2";
	/** 会员优惠券使用状态：已冻结 */
	public static final String DISCOUNT_USE_STATE_FROST = "3";

	/* <==============================优惠券领取方式==============================> */
	/** 优惠券领取方式：买家领取 */
	public static final String COUPON_RECEIVE_TYPE_RECEIVE = "0";
	/** 优惠券领取方式：下单赠送 */
	public static final String COUPON_RECEIVE_TYPE_ORDER = "1";
	/** 优惠券领取方式：平台推送 */
	public static final String COUPON_RECEIVE_TYPE_PLATFORM = "2";

	/* <==============================优惠券使用范围==============================> */
	/** 优惠券使用范围：无限制 */
	public static final String COUPON_USE_SCOPE_UNLIMIT = "0";
	/** 优惠券使用范围：服务品类专享 */
	public static final String COUPON_USE_SCOPE_SERVICE = "1";
	/** 优惠券使用范围：商家专享 */
	public static final String COUPON_USE_SCOPE_BUSINESS = "2";

	/* <===============================优惠券类型===============================> */
	/** 优惠券类型：固定金额券 */
	public static final String COUPON_TYPE_FIXED = "0";
	/** 优惠券类型：折扣券 */
	public static final String COUPON_TYPE_DISCOUNT = "1";

	/* <==============================优惠券使用条件==============================> */
	/** 优惠券使用条件：无限制 */
	public static final String COUPON_USE_RULE_UNLIMIT = "0";
	/** 优惠券使用条件：满额使用 */
	public static final String COUPON_USE_RULE_FULL = "1";

	/* <==============================优惠券发放类型==============================> */
	/** 优惠券发放类型：无限制 */
	public static final String COUPON_GRANT_TYPE_UNLIMIT = "0";
	/** 优惠券发放类型：限量发送 */
	public static final String COUPON_GRANT_TYPE_LIMIT = "1";

	/* <==============================优惠券有效期类型==============================> */
	/** 优惠券有效期类型：起止日期 */
	public static final String COUPON_VALIDITY_TYPE_START_END = "0";
	/** 优惠券有效期类型：天 */
	public static final String COUPON_VALIDITY_TYPE_DAYS = "1";

	/* <=============================优惠券下单赠送规则=============================> */
	/** 优惠券下单赠送规则：下单即送 */
	public static final String COUPON_GIVE_RULE_UNLIMIT = "0";
	/** 优惠券下单赠送规则：满额送 */
	public static final String COUPON_GIVE_RULE_LIMITED = "1";

	/* <==============================优惠券领取规则==============================> */
	/** 优惠券领取规则：无限制 */
	public static final String COUPON_RECEIVE_RULE_UNLIMIT = "0";
	/** 优惠券领取规则：每人每日限领1张 */
	public static final String COUPON_RECEIVE_RULE_LIMITONE = "1";
	/** 优惠券领取规则：每人限领1张 */
	public static final String COUPON_RECEIVE_RULE_ONLYONE = "2";

	/* <==============================商家服务时段==============================> */
	/** 商家服务时段类型：上门服务时段 */
	public static final String BUSINESS_TIME_TYPE_VISIT = "0";
	/** 商家服务时段类型：到店服务时段 */
	public static final String BUSINESS_TIME_TYPE_ARRIVAL = "1";
	/** 商家服务时段类型：上门配送时段 */
	public static final String BUSINESS_TIME_TYPE_DELIVERY = "2";

	/* <==============================预约服务方式==============================> */
	/** 预约服务方式：上门 */
	public static final String APPOINT_SERVICE_TYPE_VISIT = "0";
	/** 预约服务方式：到店 */
	public static final String APPOINT_SERVICE_TYPE_ARRIVAL = "1";

	/* <==============================团购活动状态==============================> */
	/** 团购活动状态：待开始 */
	public static final String ACTIVITY_GROUP_PURCHASE_UNSTART = "0";
	/** 团购活动状态：活动中 */
	public static final String ACTIVITY_GROUP_PURCHASE_START = "1";
	/** 团购活动状态：已结束 */
	public static final String ACTIVITY_GROUP_PURCHASE_END = "2";
	/** 团购活动状态：已撤消 */
	public static final String ACTIVITY_GROUP_PURCHASE_REVOKED = "3";

}