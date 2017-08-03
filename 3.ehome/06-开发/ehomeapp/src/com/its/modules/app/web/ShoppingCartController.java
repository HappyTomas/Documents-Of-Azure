/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.config.Global;
import com.its.common.utils.StringUtils;
import com.its.common.web.BaseController;
import com.its.modules.app.bean.GoodsInfoBean;
import com.its.modules.app.bean.ShoppingCartBean;
import com.its.modules.app.entity.GoodsInfo;
import com.its.modules.app.entity.ShoppingCart;
import com.its.modules.app.service.GoodsInfoService;
import com.its.modules.app.service.ShoppingCartService;

import net.sf.json.JSONObject;

/**
 * 购物车Controller
 * 
 * @author like
 * @version 2017-07-06
 */
@Controller
@RequestMapping(value = "${appPath}/live")
public class ShoppingCartController extends BaseController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private GoodsInfoService goodsInfoService;

	/**
	 * 加入购物车
	 * 
	 * @param userID
	 *            用户ID
	 * @param buildingID
	 *            楼盘ID
	 * @param businessID
	 *            商家ID
	 * @param commodityID
	 *            商品ID
	 * @param specificationID
	 *            规格价格表ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addShoppingCart")
	public String addShoppingCart(String userID, String buildingID, String businessID, String commodityID, String specificationID) {
		try {
			if (StringUtils.isBlank(userID) || StringUtils.isBlank(buildingID) || StringUtils.isBlank(businessID) || StringUtils.isBlank(commodityID)) {
				return "{\"code\":" + Global.CODE_PROMOT + ",\"message\":\"参数有误\"}";
			}
			if (StringUtils.isBlank(specificationID)) {
				specificationID = null;
			}
			// 判断商品库存
			GoodsInfo goods = goodsInfoService.get(commodityID);
			if (goods.getStock() <= 0) {
				return "{\"code\":" + Global.CODE_PROMOT + ",\"message\":\"库存不足\"}";
			}
			// 判断限购数量
			if ("1".equals(goods.getQuota())) {
				ShoppingCart sc = shoppingCartService.getGoodsOfShoopingCart(userID, buildingID, businessID, commodityID, specificationID);
				if (sc.getNumber() >= goods.getQuotaNum()) {
					return "{\"code\":" + Global.CODE_PROMOT + ",\"message\":\"已达限购数量\"}";
				}
			}
			// 加入购物车
			shoppingCartService.addShoppingCart(userID, buildingID, businessID, commodityID, specificationID);
			ShoppingCartBean bean = shoppingCartService.getShoppingCartTotal(userID, buildingID, businessID);
			Map<String, Object> data = new HashMap<String, Object>();
			if (bean != null) {
				data.put("totalMoney", bean.getTotalMoney());
				data.put("commodityNumber", bean.getSumNumber());
			}
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("code", Global.CODE_SUCCESS);
			json.put("data", data);
			json.put("message", "成功");
			return JSONObject.fromObject(json).toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (Global.isDebug()) {
				return "{\"code\":" + Global.CODE_ERROR + ",\"message\":\"" + e.getMessage() + "\"}";
			}
			return "{\"code\":" + Global.CODE_ERROR + ",\"message\":\"系统错误\"}";
		}
	}

	/**
	 * 减少购物车商品的数量
	 * 
	 * @param userID
	 *            用户ID
	 * @param buildingID
	 *            楼盘ID
	 * @param businessID
	 *            商家ID
	 * @param commodityID
	 *            商品ID
	 * @param specificationID
	 *            规格价格表ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/reduceShoppingCart")
	public String reduceShoppingCart(String userID, String buildingID, String businessID, String commodityID, String specificationID) {
		if (StringUtils.isBlank(userID) || StringUtils.isBlank(buildingID) || StringUtils.isBlank(businessID) || StringUtils.isBlank(commodityID)) {
			return "{\"code\":" + Global.CODE_PROMOT + ",\"message\":\"参数有误\"}";
		}
		if (StringUtils.isBlank(specificationID)) {
			specificationID = null;
		}
		try {
			shoppingCartService.reduceShoppingCart(userID, buildingID, businessID, commodityID, specificationID);
			ShoppingCartBean bean = shoppingCartService.getShoppingCartTotal(userID, buildingID, businessID);
			Map<String, Object> data = new HashMap<String, Object>();
			if (bean != null) {
				data.put("totalMoney", bean.getTotalMoney());
				data.put("commodityNumber", bean.getSumNumber());
			}
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("code", Global.CODE_SUCCESS);
			json.put("data", data);
			json.put("message", "成功");
			return JSONObject.fromObject(json).toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (Global.isDebug()) {
				return "{\"code\":" + Global.CODE_ERROR + ",\"message\":\"" + e.getMessage() + "\"}";
			}
			return "{\"code\":" + Global.CODE_ERROR + ",\"message\":\"系统错误\"}";
		}
	}

	/**
	 * 清空购物车
	 * 
	 * @param userID
	 * @param buildingID
	 * @param businessID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/emptiedShoppingCart")
	public String emptiedShoppingCart(String userID, String buildingID, String businessID) {
		if (StringUtils.isBlank(userID) || StringUtils.isBlank(buildingID) || StringUtils.isBlank(businessID)) {
			return "{\"code\":" + Global.CODE_PROMOT + ",\"message\":\"参数有误\"}";
		}
		try {
			shoppingCartService.emptyShoppingCart(userID, buildingID, businessID);
			return "{\"code\":" + Global.CODE_SUCCESS + ",\"message\":\"成功\"}";
		} catch (Exception e) {
			e.printStackTrace();
			if (Global.isDebug()) {
				return "{\"code\":" + Global.CODE_ERROR + ",\"message\":\"" + e.getMessage() + "\"}";
			}
			return "{\"code\":" + Global.CODE_ERROR + ",\"message\":\"系统错误\"}";
		}
	}

	/**
	 * 获取购物车商品信息
	 * 
	 * @param userID
	 * @param buildingID
	 * @param businessID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getShoppingCart")
	public String getShoppingCart(String userID, String buildingID, String businessID, HttpServletRequest request) {
		if (StringUtils.isBlank(userID) || StringUtils.isBlank(buildingID) || StringUtils.isBlank(businessID)) {
			return "{\"code\":" + Global.CODE_PROMOT + ",\"message\":\"参数有误\"}";
		}
		try {
			List<GoodsInfoBean> list = shoppingCartService.getShoppingCartList(userID, buildingID, businessID);
			List<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
			double totalMoney = 0;
			int totalNumber = 0;
			for (GoodsInfoBean bean : list) {
				Map<String, Object> goods = new HashMap<String, Object>();
				goods.put("commodityID", bean.getId());
				goods.put("commodityName", bean.getName());
				goods.put("commodityImage", goodsInfoService.getGoodsPicUrl(bean, request));
				goods.put("specificationID", StringUtils.isNotBlank(bean.getGoodsSkuPriceID()) ? bean.getGoodsSkuPriceID() : "");
				if (StringUtils.isNotBlank(bean.getGoodsSkuPriceID())) {
					goods.put("originalPrice", bean.getSkuBasePrice() != null ? bean.getSkuBasePrice() : 0);
					goods.put("discountedPrice", bean.getSkuBenefitPrice() != null ? bean.getSkuBenefitPrice() : 0);
					double price = bean.getSkuBasePrice();
					if (bean.getSkuBenefitPrice() != null && bean.getSkuBenefitPrice() != 0) {
						price = bean.getSkuBenefitPrice();
					}
					totalMoney += price * bean.getCartNumber();
				} else {
					goods.put("originalPrice", bean.getBasePrice() != null ? bean.getBasePrice() : 0);
					goods.put("discountedPrice", bean.getBenefitPrice() != null ? bean.getBenefitPrice() : 0);
					double price = bean.getBasePrice();
					if (bean.getBenefitPrice() != null && bean.getBenefitPrice() != 0) {
						price = bean.getBenefitPrice();
					}
					totalMoney += price * bean.getCartNumber();
				}
				goods.put("commodityNumber", bean.getCartNumber());
				totalNumber += bean.getCartNumber();
				listJson.add(goods);
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("totalMoney", totalMoney);
			data.put("totalNumber", totalNumber);
			data.put("commodities", listJson);
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("code", Global.CODE_SUCCESS);
			json.put("data", data);
			json.put("message", "成功");
			return JSONObject.fromObject(json).toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (Global.isDebug()) {
				return "{\"code\":" + Global.CODE_ERROR + ",\"message:\"" + e.getMessage() + "\"}";
			}
			return "{\"code\":" + Global.CODE_ERROR + ",\"message\":\"系统错误\"}";
		}
	}
}