package com.its.modules.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.app.bean.GroupPurchaseBean;
import com.its.modules.app.bean.OrderGroupPurcBean;
import com.its.modules.app.bean.OrderGroupPurcRCBean;
import com.its.modules.app.common.OrderGlobal;
import com.its.modules.app.common.ValidateUtil;
import com.its.modules.app.dao.OrderGroupPurcDao;
import com.its.modules.app.entity.Account;
import com.its.modules.app.entity.BusinessInfo;
import com.its.modules.app.entity.GroupPurchase;
import com.its.modules.app.entity.OrderGroupPurc;
import com.its.modules.app.entity.OrderGroupPurcList;
import com.its.modules.app.entity.VillageInfo;
import com.its.modules.sys.service.SysCodeMaxService;

/**
 * 订单-团购类Service
 * 
 * @author sushipeng
 * 
 * @version 2017-07-13
 */
@Service
@Transactional(readOnly = true)
public class OrderGroupPurcService extends CrudService<OrderGroupPurcDao, OrderGroupPurc> {

	@Autowired
	private BusinessInfoService businessInfoService;

	@Autowired
	private VillageInfoService villageInfoService;

	@Autowired
	private SysCodeMaxService sysCodeMaxService;

	@Autowired
	private OrderGroupPurcListService orderGroupPurcListService;

	@Autowired
	private OrderTrackService orderTrackService;

	@Autowired
	private GroupPurchaseService groupPurchaseService;

	@Autowired
	private GroupPurchasetimeService groupPurchasetimeService;

	@Autowired
	private ModuleManageService moduleManageService;

	public OrderGroupPurc get(String id) {
		return super.get(id);
	}

	public List<OrderGroupPurc> findList(OrderGroupPurc orderGroupPurc) {
		return super.findList(orderGroupPurc);
	}

	public Page<OrderGroupPurc> findPage(Page<OrderGroupPurc> page, OrderGroupPurc orderGroupPurc) {
		return super.findPage(page, orderGroupPurc);
	}

	@Transactional(readOnly = false)
	public void save(OrderGroupPurc orderGroupPurc) {
		super.save(orderGroupPurc);
	}

	@Transactional(readOnly = false)
	public int update(OrderGroupPurc orderGroupPurc) {
		return dao.update(orderGroupPurc);
	}

	@Transactional(readOnly = false)
	public void delete(OrderGroupPurc orderGroupPurc) {
		super.delete(orderGroupPurc);
	}

	/**
	 * 获取某精品团购用户已购数量
	 * 
	 * @param accountId
	 *            用户ID
	 * @param groupPurchaseId
	 *            团购ID
	 * @return 某精品团购用户已购数量
	 */
	public int getCountByGroupPurcIdAndAccountId(String accountId, String groupPurchaseId) {
		return dao.getCountByGroupPurcIdAndAccountId(accountId, groupPurchaseId);
	}

	/**
	 * 根据订单号获取团购券订单
	 * 
	 * @param orderNo
	 *            订单号
	 * @return OrderGroupPurc
	 */
	public OrderGroupPurc getByOrderNo(String orderNo) {
		return dao.getByOrderNo(orderNo);
	}

	/**
	 * 保存团购订单和团购清单
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String createOrderGroupPurc(Account account, String contactPhone, GroupPurchaseBean groupPurchaseBean, int payNumber, String leaveMessage) {
		BusinessInfo businessInfo = businessInfoService.get(groupPurchaseBean.getBusinessinfoId());
		VillageInfo villageInfo = villageInfoService.get(account.getVillageInfoId());
		double totalMoney = payNumber * ValidateUtil.validateDouble(groupPurchaseBean.getGroupPurcMoney());

		/* 生成订单开始 */
		OrderGroupPurc orderGroupPurc = new OrderGroupPurc();
		String moduleManageId = groupPurchaseBean.getModuleId();
		String orderNo = sysCodeMaxService.getOrdNo(villageInfo.getId(), moduleManageId);
		orderGroupPurc.setBusinessInfoId(businessInfo.getId());
		orderGroupPurc.setOrderNo(orderNo);
		orderGroupPurc.setModuleManageId(moduleManageId);
		// 产品模式：0商品购买 1服务预约 2课程购买 3场地预约
		orderGroupPurc.setProdType(moduleManageService.getProdType(moduleManageId));
		// 终端类型(0 Android 1 IOS 2 商家后台)
		orderGroupPurc.setType(null);
		orderGroupPurc.setVillageInfoId(villageInfo.getId());
		orderGroupPurc.setProvinceId(villageInfo.getAddrPro());
		orderGroupPurc.setCityId(villageInfo.getAddrCity());
		// 团购订单主表存团购主表ID
		orderGroupPurc.setGroupPurchaseId(groupPurchaseBean.getId());
		orderGroupPurc.setName(groupPurchaseBean.getGroupPurcName());
		orderGroupPurc.setBasePrice(groupPurchaseBean.getMarketMoney());
		orderGroupPurc.setGroupPurcPrice(groupPurchaseBean.getGroupPurcMoney());
		orderGroupPurc.setPayNumber(payNumber);
		orderGroupPurc.setSumMoney(totalMoney);
		orderGroupPurc.setPayMoney(totalMoney);
		// 订单状态:0未消费、1已消费、2退款处理中、3已退款
		orderGroupPurc.setOrderState(OrderGlobal.ORDER_GROUP_PURHCASE_UNCONSUME);
		// 支付对账状态:0未对账1正常2异常
		orderGroupPurc.setCheckOrderState(OrderGlobal.ORDER_CHECK_STATE_UNDO);
		// 结算状态:0未结算1已结算
		orderGroupPurc.setCheckState(OrderGlobal.ORDER_SETTLE_STATE_UNDO);
		orderGroupPurc.setAccountId(account.getId());
		orderGroupPurc.setAccountName(account.getNickname());
		orderGroupPurc.setAccountPhoneNumber(contactPhone);
		orderGroupPurc.setAccountMsg(leaveMessage);
		// 支付状态:0未支付1已支付2退款中3已退款
		orderGroupPurc.setPayState(OrderGlobal.ORDER_PAY_STATE_UNPAY);
		this.save(orderGroupPurc);

		String orderGroupPurcId = this.getByOrderNo(orderNo).getId();
		// 每张团购券生成一条子表数据
		for (int i = 0; i < payNumber; i++) {
			OrderGroupPurcList orderGroupPurcList = new OrderGroupPurcList();
			orderGroupPurcList.setBusinessInfoId(businessInfo.getId());
			// 团购订单子表存团购子表ID
			orderGroupPurcList.setGroupPurchaseId(groupPurchaseBean.getGroupPurchaseTimeId());
			orderGroupPurcList.setOrderGroupPurcId(orderGroupPurcId);
			orderGroupPurcList.setOrderNo(orderNo);
			orderGroupPurcList.setName(groupPurchaseBean.getGroupPurcName());
			orderGroupPurcList.setImgs(groupPurchaseBean.getGroupPurcPic());
			orderGroupPurcList.setBasePrice(groupPurchaseBean.getMarketMoney());
			orderGroupPurcList.setGroupPurcPrice(groupPurchaseBean.getGroupPurcMoney());
			orderGroupPurcList.setStartTime(groupPurchaseBean.getValidityStartTime());
			orderGroupPurcList.setEndTime(groupPurchaseBean.getValidityEndTime());
			orderGroupPurcList.setContent(groupPurchaseBean.getGroupPurcDetail());
			orderGroupPurcList.setUseTime(groupPurchaseBean.getUseTime());
			orderGroupPurcList.setUseContent(groupPurchaseBean.getUseRule());
			orderGroupPurcList.setPaySumMoney(groupPurchaseBean.getGroupPurcMoney());
			orderGroupPurcList.setGroupPurcNumber(this.getGroupPurcNumber());
			orderGroupPurcList.setGroupPurcState(OrderGlobal.GROUP_PURHCASE_STATE_UNCONSUME);
			orderGroupPurcListService.save(orderGroupPurcList);
		}
		/* 生成订单结束 */

		// 修改团购已购数量
		groupPurchasetimeService.reduceStockNumAddSaleNum(payNumber, groupPurchaseBean.getGroupPurchaseTimeId());
		// 插入订单追踪
		orderTrackService.createTrackSubmit(OrderGlobal.ORDER_GROUP_PURCHASE, orderGroupPurcId, orderNo);
		return orderGroupPurcId;
	}

	/**
	 * 生成12位数字随机数（团购券号）
	 * 
	 * @return 团购券号
	 */
	public String getGroupPurcNumber() {
		return String.valueOf(((long) (Math.random() * 900000000000L) + 100000000000L));
	}

	/**
	 * 根据订单ID和用户ID获取订单信息
	 * 
	 * @param orderId
	 *            订单ID
	 * @param accountId
	 *            用户ID
	 * @return OrderGroupPurcBean
	 */
	public OrderGroupPurcBean getOrderGroupPurcByOrderIdAndAccountId(String orderId, String accountId) {
		return dao.getOrderGroupPurcByOrderIdAndAccountId(orderId, accountId);
	}

	/**
	 * 判断某用户是否可以取消某订单
	 * 
	 * @param orderId
	 *            订单ID
	 * @param accountId
	 *            用户ID
	 * @return OrderGroupPurc
	 */
	public OrderGroupPurc judgeOrderGroupPurcCancelAble(String orderId, String accountId) {
		return dao.judgeOrderGroupPurcCancelAble(orderId, accountId);
	}

	/**
	 * 取消团购订单
	 * 
	 * @param orderId
	 *            订单ID
	 * @param accountId
	 *            用户ID
	 * @param cancelType
	 *            取消类型：0超时取消 1用户取消
	 * @return 取消成功返回true，失败返回false
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean cancelOrder(String orderId, String accountId, String cancelType) {
		// 判断订单是否可取消
		OrderGroupPurc orderGroupPurc = this.judgeOrderGroupPurcCancelAble(orderId, accountId);
		if (orderGroupPurc == null) {
			return false;
		}

		// 订单状态"待支付"——取消订单——订单状态"已取消"
		orderGroupPurc.setOrderState(OrderGlobal.ORDER_GROUP_PURHCASE_CANCELED);
		List<OrderGroupPurcList> orderGroupPurcLists = orderGroupPurcListService.getOrderGroupPurcLists(orderGroupPurc.getId());
		if (orderGroupPurcLists == null || orderGroupPurcLists.size() == 0) {
			// 事务回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 团购子表增加库存，减少已售数量
		int row = groupPurchasetimeService.addStockNumReduceSaleNum(orderGroupPurcLists.size(), orderGroupPurcLists.get(0).getGroupPurchaseId());
		if (row == 0) {
			// 事务回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 更新订单状态
		this.update(orderGroupPurc);
		// 插入订单追踪
		orderTrackService.createTrackCanceled(OrderGlobal.ORDER_GROUP_PURCHASE, orderGroupPurc.getId(), orderGroupPurc.getOrderNo(), cancelType);
		return true;
	}

	/**
	 * 获取团购订单状态
	 * 
	 * @param OrderGroupPurc
	 *            团购订单
	 * @return 团购订单状态
	 */
	public String getOrderGroupPurStatus(OrderGroupPurc orderGroupPurc) {
		List<OrderGroupPurcList> orderGroupPurcLists = orderGroupPurcListService.getOrderGroupPurcLists(orderGroupPurc.getId());
		GroupPurchase groupPurchase = groupPurchaseService.get(orderGroupPurc.getGroupPurchaseId());
		if (orderGroupPurcLists == null || orderGroupPurcLists.size() == 0 || groupPurchase == null) {
			return "";
		}
		if (OrderGlobal.ORDER_PAY_STATE_UNPAY.equals(orderGroupPurc.getPayState())) {
			return "未支付";
		}
		if (OrderGlobal.ORDER_GROUP_PURHCASE_CANCELED.equals(orderGroupPurc.getOrderState())) {
			return "已取消";
		}
		int[] supportType = groupPurchaseService.getSupportType(groupPurchase.getSupportType());

		// 未消费
		boolean unconsumeFlag = false;
		// 已消费
		boolean consumedFlag = false;
		// 退款中
		boolean refundingFlag = false;
		// 已退款
		boolean refundedFlag = false;

		for (OrderGroupPurcList orderGroupPurcList : orderGroupPurcLists) {
			if (OrderGlobal.GROUP_PURHCASE_STATE_UNCONSUME.equals(orderGroupPurcList.getGroupPurcState())) {
				unconsumeFlag = true;
			}
			if (OrderGlobal.GROUP_PURHCASE_STATE_CONSUMED.equals(orderGroupPurcList.getGroupPurcState())) {
				consumedFlag = true;
			}
			if (OrderGlobal.GROUP_PURHCASE_STATE_REFUNDING.equals(orderGroupPurcList.getGroupPurcState())) {
				refundingFlag = true;
			}
			if (OrderGlobal.GROUP_PURHCASE_STATE_REFUNDED.equals(orderGroupPurcList.getGroupPurcState())) {
				refundedFlag = true;
			}
		}

		// 团购券未消费并且团购券有效期已过且不支持过期退
		if (unconsumeFlag && groupPurchase.getValidityEndTime().getTime() <= new Date().getTime() && supportType[1] == 0) {
			return "已过期";
		}
		if (groupPurchase.getValidityEndTime().getTime() > new Date().getTime()) {
			if (unconsumeFlag) {
				return "未消费";
			}
			if (!unconsumeFlag && consumedFlag) {
				return "已消费";
			}
			if (!unconsumeFlag && !consumedFlag && refundingFlag) {
				return "退款处理中";
			}
			if (!unconsumeFlag && !consumedFlag && !refundingFlag && refundedFlag) {
				return "已退款";
			}
		}
		return "";
	}

	/**
	 * 获取团购券的临近期
	 * 
	 * @param numDays
	 *            临近天数
	 * @return List<OrderGroupPurcRCBean>
	 */
	public List<OrderGroupPurcRCBean> findTicketExpireMsg(String numDays) {
		List<OrderGroupPurcRCBean> rtn = dao.findTicketExpireMsg(numDays);
		return rtn;
	}
}