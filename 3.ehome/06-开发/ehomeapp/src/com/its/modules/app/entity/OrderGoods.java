package com.its.modules.app.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.its.common.persistence.DataEntity;

/**
 * 商品订单Entity
 * 
 * @author like
 * 
 * @version 2017-07-13
 */
public class OrderGoods extends DataEntity<OrderGoods> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单号
	private String businessInfoId;		// 商家id
	private String type;		// 终端类型(0 Android 1 IOS 2 商家后台)
	private String moduleManageId;		// 模块管理ID
	private String prodType;		// 产品模式：0商品购买  1服务预约  2课程购买  3场地预约
	private Double sumMoney;		// 总金额
	private String villageInfoId;		// 楼盘ID
	private Double benefitMoney;		// 商家优惠金额
	private String provinceId;		// 省ID
	private Double couponMoney;		// 优惠券优惠金额
	private String cityId;		// 城市ID
	private Double payMoney;		// 实际支付金额
	private String orderState;		// 订单状态:0待受理、1已受理、2配送中、3已完成、4已取消
	private String checkOrderState;		// 支付对账状态:0未对账1正常2异常
	private Date checkTime;		// 结算时间
	private String checkState;		// 结算状态:0未结算1已结算
	private String accountId;		// 买家ID
	private String accountName;		// 买家名称
	private String accountPhoneNumber;		// 买家联系方式
	private String payType;		// 支付方式(0在线支付)
	private String payOrg;		// 支付机构(0微信1支付宝2平台钱包)
	private Date payTime;		// 支付时间
	private String payUserName;		// 支付账号
	private String accountMsg;		// 买家留言
	private String payState;		// 支付状态
	private String addressType;		// 配送方式
	private String address;		// 配送地址
	private String addressState;		// 配送状态0配送中1已送达
	private Double addressMoney;		// 配送费用
	private Double addressBenefit;		// 配送费减免
	private Date overTime;		// 送达时间
	private String isStart;		// 是否立即配送
	private String startTime;		// 预计开始时间
	private String endTime;		// 预计结束时间
	
	public OrderGoods() {
		super();
	}

	public OrderGoods(String id){
		super(id);
	}

	@Length(min=0, max=64, message="订单号长度必须介于 0 和 64 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Length(min=1, max=64, message="商家id长度必须介于 1 和 64 之间")
	public String getBusinessInfoId() {
		return businessInfoId;
	}

	public void setBusinessInfoId(String businessInfoId) {
		this.businessInfoId = businessInfoId;
	}
	
	@Length(min=0, max=1, message="终端类型(0 Android 1 IOS 2 商家后台)长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=64, message="模块管理ID长度必须介于 0 和 64 之间")
	public String getModuleManageId() {
		return moduleManageId;
	}

	public void setModuleManageId(String moduleManageId) {
		this.moduleManageId = moduleManageId;
	}
	
	@Length(min=0, max=64, message="产品模式：0商品购买  1服务预约  2课程购买  3场地预约长度必须介于 0 和 64 之间")
	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	
	public Double getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}
	
	@Length(min=0, max=64, message="楼盘ID长度必须介于 0 和 64 之间")
	public String getVillageInfoId() {
		return villageInfoId;
	}

	public void setVillageInfoId(String villageInfoId) {
		this.villageInfoId = villageInfoId;
	}
	
	public Double getBenefitMoney() {
		return benefitMoney;
	}

	public void setBenefitMoney(Double benefitMoney) {
		this.benefitMoney = benefitMoney;
	}
	
	@Length(min=0, max=64, message="省ID长度必须介于 0 和 64 之间")
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	
	public Double getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(Double couponMoney) {
		this.couponMoney = couponMoney;
	}
	
	@Length(min=0, max=64, message="城市ID长度必须介于 0 和 64 之间")
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	
	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	
	@Length(min=0, max=1, message="订单状态:0待受理、1已受理、2配送中、3已完成、4已取消长度必须介于 0 和 1 之间")
	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	
	@Length(min=0, max=1, message="支付对账状态:0未对账1正常2异常长度必须介于 0 和 1 之间")
	public String getCheckOrderState() {
		return checkOrderState;
	}

	public void setCheckOrderState(String checkOrderState) {
		this.checkOrderState = checkOrderState;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	@Length(min=0, max=1, message="结算状态:0未结算1已结算长度必须介于 0 和 1 之间")
	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	
	@Length(min=1, max=64, message="买家ID长度必须介于 1 和 64 之间")
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	@Length(min=0, max=64, message="买家名称长度必须介于 0 和 64 之间")
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	@Length(min=0, max=64, message="买家联系方式长度必须介于 0 和 64 之间")
	public String getAccountPhoneNumber() {
		return accountPhoneNumber;
	}

	public void setAccountPhoneNumber(String accountPhoneNumber) {
		this.accountPhoneNumber = accountPhoneNumber;
	}
	
	@Length(min=0, max=64, message="支付方式(0在线支付)长度必须介于 0 和 64 之间")
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	@Length(min=0, max=64, message="支付机构(0微信1支付宝2平台钱包)长度必须介于 0 和 64 之间")
	public String getPayOrg() {
		return payOrg;
	}

	public void setPayOrg(String payOrg) {
		this.payOrg = payOrg;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	@Length(min=0, max=255, message="支付账号长度必须介于 0 和 255 之间")
	public String getPayUserName() {
		return payUserName;
	}

	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}
	
	@Length(min=0, max=255, message="买家留言长度必须介于 0 和 255 之间")
	public String getAccountMsg() {
		return accountMsg;
	}

	public void setAccountMsg(String accountMsg) {
		this.accountMsg = accountMsg;
	}
	
	@Length(min=0, max=1, message="支付状态长度必须介于 0 和 1 之间")
	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}
	
	@Length(min=0, max=1, message="配送方式长度必须介于 0 和 1 之间")
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	
	@Length(min=0, max=255, message="配送地址长度必须介于 0 和 255 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=1, message="配送状态0配送中1已送达长度必须介于 0 和 1 之间")
	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	
	public Double getAddressMoney() {
		return addressMoney;
	}

	public void setAddressMoney(Double addressMoney) {
		this.addressMoney = addressMoney;
	}
	
	public Double getAddressBenefit() {
		return addressBenefit;
	}

	public void setAddressBenefit(Double addressBenefit) {
		this.addressBenefit = addressBenefit;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	
	@Length(min=0, max=1, message="是否立即配送长度必须介于 0 和 1 之间")
	public String getIsStart() {
		return isStart;
	}

	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}
	
	@Length(min=0, max=64, message="预计开始时间长度必须介于 0 和 64 之间")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@Length(min=0, max=64, message="预计结束时间长度必须介于 0 和 64 之间")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}