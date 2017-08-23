/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.common.utils.AlipayUtils;
import com.its.common.utils.HttpUtils;
import com.its.common.utils.NumberUtil;
import com.its.common.utils.WXUtils;
import com.its.common.utils.WXUtilsConfig;
import com.its.modules.order.entity.Account;
import com.its.modules.order.entity.OrderGoods;
import com.its.modules.order.entity.OrderGoodsList;
import com.its.modules.order.entity.OrderRefundInfo;
import com.its.modules.order.entity.OrderTrack;
import com.its.modules.order.entity.WalletDetail;
import com.its.modules.setup.dao.BusinessInfoDao;
import com.its.modules.setup.entity.BusinessInfo;
import com.its.modules.sys.entity.User;
import com.its.modules.sys.utils.UserUtils;

import com.its.modules.goods.dao.GoodsInfoDao;
import com.its.modules.goods.dao.GoodsSkuPriceDao;
import com.its.modules.goods.entity.GoodsInfo;
import com.its.modules.goods.entity.GoodsSkuPrice;
import com.its.modules.order.dao.AccountDao;
import com.its.modules.order.dao.OrderGoodsDao;
import com.its.modules.order.dao.WalletDetailDao;

/**
 * 订单-商品类Service
 * 
 * @author liuhl
 * @version 2017-07-11
 */
@Service
@Transactional(readOnly = true)
public class OrderGoodsService extends CrudService<OrderGoodsDao, OrderGoods> {

    private static Logger logger = LoggerFactory.getLogger(OrderGoodsService.class);
    
    /** 退款信息明细表Service */
    @Autowired
    private OrderRefundInfoService orderRefundInfoService;

    /** 订单跟踪表Service */
    @Autowired
    private OrderTrackService orderTrackService;

    /** 商品订单明细表SERVICE */
    @Autowired
    private OrderGoodsListService orderGoodsListService;

    /** 商品信息表Dao */
    @Autowired
    private GoodsInfoDao goodsInfoDao;

    /** 钱包明细Dao */
    @Autowired
    private WalletDetailDao walletDetailDao;

    /**
     * 会员信息DAO
     */
    @Autowired
    private AccountDao accountDao;

    /**
     * 商品规格价格Dao
     */
    @Autowired
    private GoodsSkuPriceDao goodsSkuPriceDao;

    /** 商户分类Service */
    @Autowired
    private BusinessInfoDao businessInfoDao;

    public OrderGoods get(String id) {
        return super.get(id);
    }

    public List<OrderGoods> findList(OrderGoods orderGoods) {
        return super.findList(orderGoods);
    }

    public Page<OrderGoods> findPage(Page<OrderGoods> page, OrderGoods orderGoods) {
        page.setOrderBy("a.create_date desc,a.order_no desc");
        return super.findPage(page, orderGoods);
    }

    @Transactional(readOnly = false)
    public void save(OrderGoods orderGoods) {
        super.save(orderGoods);
    }

    @Transactional(readOnly = false)
    public void delete(OrderGoods orderGoods) {
        super.delete(orderGoods);
    }

    /**
     * 订单完成
     * 
     * @param id
     *            订单号
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int complete(String id) {
        OrderGoods orderGoods = super.get(id);
        orderGoods.setOverTime(new Date());
        orderGoods.preUpdate();
        int result = this.dao.complete(orderGoods);
        if (result == 0) {
            return result;
        }
        // 跟踪表要插入信息
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderTrack.setOrderId(orderGoods.getId());
        orderTrack.setOrderType(orderGoods.getProdType());
        orderTrack.setStateMsg("已完成");
        orderTrack.setHandleMsg("完成服务/送达/已自提");
        orderTrack.setStateMsgPhone("已完成");
        orderTrack.setHandleMsgPhone("感谢您的订购");
        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())){
            orderTrack.setCreateName(user.getId());
        }
        orderTrackService.save(orderTrack);
        return result;

    }

    /**
     * 订单接受
     * 
     * @param id
     *            订单号
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int accept(String id) {
        OrderGoods orderGoods = super.get(id);
        orderGoods.preUpdate();
        int result = this.dao.accept(orderGoods);
        if (result == 0) {
            return result;
        }
        // 跟踪表要插入信息
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderTrack.setBusinessInfoId(orderGoods.getBusinessInfoId());
        orderTrack.setOrderId(orderGoods.getId());
        orderTrack.setOrderType(orderGoods.getProdType());
        orderTrack.setStateMsg("已受理");
        orderTrack.setHandleMsg("商家已受理，准备配送");
        orderTrack.setStateMsgPhone("已受理");
        orderTrack.setHandleMsgPhone("商家已受理，等待商家服务");
        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())){
            orderTrack.setCreateName(user.getId());
        }
        orderTrackService.save(orderTrack);
        return result;

    }

    /**
     * 订单取消
     * 
     * @param orderGoodsFromJsp
     *            订单取消信息
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int cancel(OrderGoods orderGoodsFromJsp) {
        orderGoodsFromJsp.preUpdate();
        // 订单状态更新为：已取消
        int result = this.dao.cancel(orderGoodsFromJsp);
        // 影响条数为0，更新失败
        if (0 == result) {
            return result;
        }

        // 数据库中订单数据（参数是画面传递过来的信息）
        OrderGoods orderGoodsInfo = super.get(orderGoodsFromJsp.getId());
        // 如果订单支付状态为1：已支付的话，执行退款处理
        if ("1".equals(orderGoodsInfo.getPayState())) {
            // 如果是支付宝的话
            if ("1".equals(orderGoodsInfo.getPayOrg())) {
                // 更新订单表
                orderGoodsInfo.preUpdate();
                // 将该订单的支付状态改为2：退款中
                orderGoodsInfo.setPayState("2");
                super.save(orderGoodsInfo);

                // 执行支付宝退款
                JSONObject refundResult = AlipayUtils.alipayRefundRequest(orderGoodsInfo.getOrderNo(),
                        String.valueOf(orderGoodsInfo.getPayMoney()), orderGoodsFromJsp.getCancelRemarks());
                if (refundResult != null && "10000".equals(refundResult.get("code"))) {
                    // 更新订单表
                    orderGoodsInfo.preUpdate();
                    // 将该订单的支付状态改为3：已退款
                    orderGoodsInfo.setPayState("3");
                    super.save(orderGoodsInfo);
                    OrderRefundInfo orderRefundInfo = new OrderRefundInfo();
                    orderRefundInfo.setOrderId(orderGoodsInfo.getId());
                    orderRefundInfo.setOrderNo(orderGoodsInfo.getOrderNo());
                    // 微信交易号
                    orderRefundInfo.setTransactionId(orderGoodsInfo.getTransactionID());
                    // 因为是商品订单发生退款，所以固定为0：商品类
                    orderRefundInfo.setOrderType("0");
                    orderRefundInfo.setPayType(orderGoodsInfo.getPayType());
                    orderRefundInfo.setOrderMoney(orderGoodsInfo.getPayMoney());
                    // 终端类型固定为商家后台
                    orderRefundInfo.setType("2");
                    orderRefundInfo.setModuleManageId(orderGoodsInfo.getModuleManageId());
                    // 产品模式固定为0:商品购买
                    orderRefundInfo.setProdType("0");
                    orderRefundInfo.setRefundMoney(orderGoodsInfo.getPayMoney());

                    orderRefundInfo.setRefundNo(orderGoodsInfo.getOrderNo());
                    // 微信退款单号
                    orderRefundInfo.setRefundTransactionId(refundResult.get("out_trade_no").toString());
                    orderRefundInfo.setRefundType(orderGoodsInfo.getPayOrg());

                    // 退款状态：退款成功
                    orderRefundInfo.setRefundState("2");

                    // 退款完成时间
                    orderRefundInfo.setRefundOverTime(new Date());

                    // 退款原因
                    orderRefundInfo.setRefundReason(orderGoodsFromJsp.getCancelRemarks());

                    orderRefundInfoService.save(orderRefundInfo);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
                // 如果是微信的话，直接调用接口进行退款
            } else if ("0".equals(orderGoodsInfo.getPayOrg())) {
                // 更新订单表
                orderGoodsInfo.preUpdate();
                // 将该订单的支付状态改为2：退款中
                orderGoodsInfo.setPayState("2");
                super.save(orderGoodsInfo);
                // 以订单号为退款号
                Map<String, String> refund_result = WXUtils.doRefund(orderGoodsInfo.getOrderNo(),
                        orderGoodsInfo.getOrderNo(),
                        // 微信退款是以分为单位
                        NumberUtil.yuanToFen(orderGoodsInfo.getPayMoney()),
                        // 微信退款是以分为单位
                        NumberUtil.yuanToFen(orderGoodsInfo.getPayMoney()),
                        // 退款原因
                        orderGoodsFromJsp.getCancelRemarks());
                // 订单支付成功的话
                if (StringUtils.isNotBlank(refund_result.get("result_code"))
                        && WXUtilsConfig.SUCCESS.equals(refund_result.get("result_code"))) {
                    // 更新订单表
                    orderGoodsInfo.preUpdate();
                    // 将该订单的支付状态改为3：已退款
                    orderGoodsInfo.setPayState("3");
                    super.save(orderGoodsInfo);
                    OrderRefundInfo orderRefundInfo = new OrderRefundInfo();
                    orderRefundInfo.setOrderId(orderGoodsInfo.getId());
                    orderRefundInfo.setOrderNo(orderGoodsInfo.getOrderNo());
                    // 微信交易号
                    orderRefundInfo.setTransactionId(orderGoodsInfo.getTransactionID());
                    // 因为是商品订单发生退款，所以固定为0：商品类
                    orderRefundInfo.setOrderType("0");
                    orderRefundInfo.setPayType(orderGoodsInfo.getPayType());
                    orderRefundInfo.setOrderMoney(orderGoodsInfo.getPayMoney());
                    // 终端类型固定为商家后台
                    orderRefundInfo.setType("2");
                    orderRefundInfo.setModuleManageId(orderGoodsInfo.getModuleManageId());
                    // 产品模式固定为0:商品购买
                    orderRefundInfo.setProdType("0");
                    orderRefundInfo.setRefundMoney(orderGoodsInfo.getPayMoney());

                    orderRefundInfo.setRefundNo(orderGoodsInfo.getOrderNo());
                    // 微信退款单号
                    orderRefundInfo.setRefundTransactionId(refund_result.get("refund_id"));
                    orderRefundInfo.setRefundType(orderGoodsInfo.getPayOrg());

                    // 退款状态：退款成功
                    orderRefundInfo.setRefundState("2");

                    // 退款完成时间
                    orderRefundInfo.setRefundOverTime(new Date());

                    // 退款原因
                    orderRefundInfo.setRefundReason(orderGoodsFromJsp.getCancelRemarks());

                    orderRefundInfoService.save(orderRefundInfo);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                // 如果是用户钱包支付的话
            } else if ("2".equals(orderGoodsInfo.getPayOrg())) {
                logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "退款开始");
                // 取得用户信息,并施加行级锁
                Account userInfo = accountDao.getForUpdate(orderGoodsInfo.getAccountId());
                if (userInfo == null) {
                    logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "，取得不到用户信息");
                    logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "退款异常结束");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                // 根据订单ID取得支付明细（即本金支付多少，赠送金额支付多少）
                WalletDetail payInfo = walletDetailDao.getByOrderId(orderGoodsInfo.getId());

                // 钱包本金追加
                userInfo.setWalletPrincipal(userInfo.getWalletPrincipal() + payInfo.getWalletPrincipal());
                // 钱包赠送金额追加
                userInfo.setWalletPresent(userInfo.getWalletPresent() + payInfo.getWalletPresent());
                // 钱包余额追加(当前余额 + 本金支付金额 + 赠送支付金额)
                userInfo.setWalletBalance(
                        userInfo.getWalletBalance() + payInfo.getWalletPrincipal() + payInfo.getWalletPresent());

                // 执行更新处理
                userInfo.preUpdate();
                result = accountDao.update(userInfo);
                // 若更新失败则回滚事务
                if (result == 0) {
                    logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "，钱包余额修改失败");
                    logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "退款异常结束");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }

                // 钱包明细追加一条退款记录
                WalletDetail walletDetail = new WalletDetail();
                walletDetail.preInsert();
                // 会员ID
                walletDetail.setAccountId(orderGoodsInfo.getAccountId());
                // 楼盘ID
                walletDetail.setVillageInfoId(orderGoodsInfo.getVillageInfoId());
                // 订单ID
                walletDetail.setOrderId(orderGoodsInfo.getId());
                // 交易类型：5-退款(订单取消)
                walletDetail.setTradeType("5");
                // 本金退款金额
                walletDetail.setWalletPrincipal(payInfo.getWalletPrincipal());
                // 赠送金退款金额
                walletDetail.setWalletPresent(payInfo.getWalletPresent());
                // 退款时间
                walletDetail.setTradeDate(new Date());
                // 支付类型
                walletDetail.setPayType("0");
                // 执行插入操作
                walletDetailDao.insert(walletDetail);
                logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "，退款金额如下：");
                logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "，钱包本金：" + payInfo.getWalletPrincipal());
                logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "，钱包赠送金额：" + payInfo.getWalletPresent());
                logger.warn("用户钱包退款------------>订单号：" + orderGoodsInfo.getOrderNo() + "退款正常结束");
            }
        }
        // 若更新失败则回滚事务
        if (result == 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        // 商品订单明细取得
        OrderGoodsList orderGoodsList = new OrderGoodsList();
        // 根据订单号检索
        orderGoodsList.setOrderNo(orderGoodsInfo.getOrderNo());
        // 取得该订单对应的信息
        List<OrderGoodsList> orderGoodsInfoList = orderGoodsListService.findList(orderGoodsList);

        List<String> goodsId = new ArrayList<String>();
        // 商品数量信息MAP
        Map<String, Integer> goodsStock = new HashMap<String, Integer>();
        for (OrderGoodsList orderGoodsListTemp : orderGoodsInfoList) {
            goodsId.add(orderGoodsListTemp.getGoodsInfoId());

            // 根据规格项ID以及规格名称ID取得规格库存信息
            GoodsSkuPrice goodsSkuPrice = new GoodsSkuPrice();
            goodsSkuPrice.setGoodsInfoId(new GoodsInfo(orderGoodsListTemp.getGoodsInfoId()));
            goodsSkuPrice.setSkuKeyId(orderGoodsListTemp.getSkuKeyId());
            goodsSkuPrice.setSkuValueId(orderGoodsListTemp.getSkuValueId());
            // 取得商品规格表信息并添加单行锁
            List<GoodsSkuPrice> goodsSkuPriceInfo = goodsSkuPriceDao.findGoodsSkuPriceListForUpdate(goodsSkuPrice);
            // 商品规格表库存回退
            for (GoodsSkuPrice goodsSkuPriceTemp : goodsSkuPriceInfo) {
                goodsSkuPriceTemp.setStock(
                        nullToZero(goodsSkuPriceTemp.getStock()) + nullToZero(orderGoodsListTemp.getGoodsSum()));
                goodsSkuPriceTemp.preUpdate();
                result = goodsSkuPriceDao.update(goodsSkuPriceTemp);
                // 若更新失败则回滚事务
                if (0 == result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
            }

            // 算出该订单每种商品一共多少库存
            int stock = nullToZero(orderGoodsListTemp.getGoodsSum())
                    + nullToZero(goodsStock.get(orderGoodsListTemp.getGoodsInfoId()));
            goodsStock.put(orderGoodsListTemp.getGoodsInfoId(), stock);
        }
        // 虽然逻辑上不会有商品为空的订单，为了程序的健壮性依然增加了该判断
        if (goodsId != null & goodsId.size() > 0) {
            // 对商品信息添加行级锁
            List<GoodsInfo> goodsInfoList = goodsInfoDao.findGoodsInfoListForUpdate(goodsId);

            // 库存回退
            for (GoodsInfo goodsInfo : goodsInfoList) {
                goodsInfo.preUpdate();
                goodsInfo.setStock(nullToZero(goodsInfo.getStock()) + nullToZero(goodsStock.get(goodsInfo.getId())));
                goodsInfo.preUpdate();
                result = goodsInfoDao.update(goodsInfo);
                // 若更新失败则回滚事务
                if (0 == result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
            }
        }

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderId(orderGoodsInfo.getId());
        orderTrack.setOrderNo(orderGoodsInfo.getOrderNo());
        orderTrack.setOrderType(orderGoodsInfo.getProdType());
        orderTrack.setStateMsg("已取消");
        orderTrack.setHandleMsg("商家取消订单（自动退款）");
        orderTrack.setStateMsgPhone("已取消");
        orderTrack.setHandleMsgPhone("订单已成功取消");
        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())){
            orderTrack.setCreateName(user.getId());
        }
        orderTrack.setRemarks(orderGoodsFromJsp.getCancelRemarks());
        // 保存订单跟踪信息
        orderTrackService.save(orderTrack);

        // 向用户推送订单取消信息
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("businessId", user.getBusinessinfoId());
        BusinessInfo businessInfo = businessInfoDao.get(user.getBusinessinfoId());
        paramMap.put("businessName", (businessInfo != null) ? businessInfo.getBusinessName() : "");
        paramMap.put("cancelReason", orderGoodsFromJsp.getCancelRemarks());
        paramMap.put("orderId", orderGoodsInfo.getId());
        paramMap.put("toUserId", orderGoodsInfo.getAccountId());
        paramMap.put("sendType", "2.2");

        JSONObject msg_result = HttpUtils.sendPost("/rongCloudMsg/cancelOrderMsg", paramMap);
        // 若信息发送失败则回滚
        if (!"1000".equals(String.valueOf(msg_result.get("code")))) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }

        return result;
    }

    /**
     * 订单配送
     * 
     * @param id
     *            订单号
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int dispatching(String id) {
        OrderGoods orderGoods = super.get(id);
        orderGoods.preUpdate();
        int result = this.dao.dispatching(orderGoods);
        if (result == 0) {
            return result;
        }
        // 订单跟踪信息添加
        // 跟踪表要插入信息
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderTrack.setBusinessInfoId(orderGoods.getBusinessInfoId());
        orderTrack.setOrderId(orderGoods.getId());
        orderTrack.setOrderType(orderGoods.getProdType());
        orderTrack.setStateMsg("配送中");
        orderTrack.setHandleMsg("上门/配送/等待自提中");
        orderTrack.setStateMsgPhone("配送中");
        orderTrack.setHandleMsgPhone("商家开始配送，等待送达");
        
        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())){
            orderTrack.setCreateName(user.getId());
        }
        // 保存订单跟踪信息
        orderTrackService.save(orderTrack);

        // 向用户推送商品配送信息
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("businessId", user.getBusinessinfoId());
        BusinessInfo businessInfo = businessInfoDao.get(user.getBusinessinfoId());
        paramMap.put("businessName", (businessInfo != null) ? businessInfo.getBusinessName() : "");
        paramMap.put("orderId", orderGoods.getId());
        paramMap.put("toUserId", orderGoods.getAccountId());
        paramMap.put("sendType", "2.1");

        JSONObject msg_result = HttpUtils.sendPost("/rongCloudMsg/sendGoodsMsg", paramMap);
        // 若信息发送失败则回滚
        if (!"1000".equals(String.valueOf(msg_result.get("code")))) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        return result;

    }

    /**
     * 更新前的CHECK
     * 
     * @param id
     *            订单号
     * @param updateDate
     *            更新日时
     * @return
     */
    public boolean check(String id, String updateDate) {
        Map<String, String> paramer = new HashMap<String, String>();
        paramer.put("id", id);
        paramer.put("updateDate", updateDate);
        int result = this.dao.check(paramer);
        // 更新日时已经发生变化
        if (0 == result) {
            return false;
        }
        return true;
    }

    /**
     * 如果为NULL则变为0
     * 
     * @param number
     *            待转换数字
     * @return
     */
    private Integer nullToZero(Integer number) {
        if (number == null) {
            return NumberUtils.INTEGER_ZERO;
        }
        return number;
    }

    /**
     * 本周商品订单金额
     * 
     * @return
     */
    public Double findAllListMoney() {
        return this.dao.findAllListMoney();
    }

    /**
     * 本周商品订单数量
     *
     * @return
     */
    public Integer findAllListCount() {
        return this.dao.findAllListCount();
    }
}