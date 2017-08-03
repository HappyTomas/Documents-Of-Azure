package com.its.modules.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.config.Global;
import com.its.common.utils.StringUtils;
import com.its.common.web.BaseController;
import com.its.modules.app.bean.GroupPurchaseBean;
import com.its.modules.app.common.ValidateUtil;
import com.its.modules.app.entity.BusinessInfo;
import com.its.modules.app.entity.LessonInfo;
import com.its.modules.app.service.BusinessInfoService;
import com.its.modules.app.service.GroupPurchaseService;
import com.its.modules.app.service.LessonInfoService;
import com.its.modules.app.service.MyCollectService;

/**
 * 课程培训Controller
 * 
 * @author sushipeng
 * 
 * @version 2017-07-10
 */
@Controller
@RequestMapping(value = "${appPath}/live")
public class LessonInfoController extends BaseController {

	@Autowired
	private LessonInfoService lessonInfoService;

	@Autowired
	private BusinessInfoService businessInfoService;

	@Autowired
	private GroupPurchaseService groupPurchaseService;

	@Autowired
	private MyCollectService myCollectService;

	/**
	 * 模块商家列表
	 * 
	 * @param userID
	 *            用户ID（可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @param sort
	 *            排序方式：1->默认排序 2->商家销量排序 3->商家发布时间排序
	 */
	@RequestMapping(value = "getCourseList")
	@ResponseBody
	public Map<String, Object> getCourseList(String userID, String buildingID, String sort, HttpServletRequest request) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, buildingID, sort)) {
			return toJson;
		}
		List<BusinessInfo> businessInfos = businessInfoService.getBusinessList(2, buildingID,
				(StringUtils.isNoneBlank(sort) && StringUtils.isNumeric(sort)) ? Integer.parseInt(sort) : 1);
		if (businessInfos == null || businessInfos.size() == 0) {
			toJson.put("code", Global.CODE_SUCCESS);
			toJson.put("message", "暂无数据");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		int count = 0;
		if (businessInfos.size() == 1) {
			// 判断模块下只有一个商家时：自动显示商家6个课程信息（其他规则不变）
			count = 6;
		} else {
			// 默认显示商家推荐的3个课程，无推荐，依次显示最新发布的3个课程
			count = 3;
		}
		for (BusinessInfo businessInfo : businessInfos) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("businessID", businessInfo.getId());
			data.put("businessName", businessInfo.getBusinessName());
			data.put("businessImage", businessInfoService.formatBusinessPic(businessInfo.getBusinessPic(), request));
			data.put("isNormal", businessInfoService.isBusinessNormal(businessInfo));
			data.put("businessLabels", businessInfoService.getBusinessLabelList(businessInfo));

			/* 商家课程项目开始 */
			List<Map<String, Object>> courseItems = new ArrayList<Map<String, Object>>();
			List<LessonInfo> lessonInfos = lessonInfoService.getByBusinessId(businessInfo.getId(), count);
			if (lessonInfos != null && lessonInfos.size() != 0) {
				for (LessonInfo lessonInfo : lessonInfos) {
					Map<String, Object> courseItem = new HashMap<String, Object>();
					courseItem.put("ID", lessonInfo.getId());
					courseItem.put("name", lessonInfoService.getLessonName(lessonInfo));
					courseItem.put("image", lessonInfoService.getFirstLessonImg(lessonInfo, request));
					courseItem.put("price", lessonInfoService.getLessonPrice(lessonInfo));
					courseItem.put("url", null);

					courseItems.add(courseItem);
				}
			}
			data.put("courseItems", courseItems);
			/* 商家课程项目结束 */

			data.put("businessUrl", null);
			datas.add(data);
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 商家课程列表
	 * 
	 * @param userID
	 *            用户ID（可空）
	 * @param buildingID
	 *            楼盘ID(不可空)
	 * @param businessID
	 *            商家ID（不可空）
	 */
	@RequestMapping(value = "getCourseItems")
	@ResponseBody
	public Map<String, Object> getCourseItems(String userID, String buildingID, String businessID, HttpServletRequest request) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, buildingID, businessID)) {
			return toJson;
		}
		BusinessInfo businessInfo = businessInfoService.get(businessID);
		if (businessInfo == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "商家不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("businessID", businessInfo.getId());
		data.put("businessName", businessInfo.getBusinessName());
		data.put("businessImage", businessInfoService.formatBusinessPic(businessInfo.getBusinessPic(), request));
		data.put("isNormal", businessInfoService.isBusinessNormal(businessInfo));
		data.put("isCollection", StringUtils.isNotBlank(userID) ? myCollectService.isCollect(userID, buildingID, businessInfo.getId()) : 0);
		data.put("businessLabels", businessInfoService.getBusinessLabelList(businessInfo));
		data.put("businessHours", businessInfo.getBusinessHours());

		/* 商家课程项目开始 */
		List<Map<String, Object>> courseItems = new ArrayList<Map<String, Object>>();
		List<LessonInfo> lessonInfos = lessonInfoService.getNoLimitList(businessInfo.getId());
		if (lessonInfos != null && lessonInfos.size() != 0) {
			for (LessonInfo lessonInfo : lessonInfos) {
				Map<String, Object> courseItem = new HashMap<String, Object>();
				courseItem.put("ID", lessonInfo.getId());
				courseItem.put("name", lessonInfoService.getLessonName(lessonInfo));
				courseItem.put("image", lessonInfoService.getFirstLessonImg(lessonInfo, request));
				courseItem.put("originalPrice", ValidateUtil.validateDouble(lessonInfo.getBasePrice()));
				courseItem.put("discountedPrice", ValidateUtil.validateDouble(lessonInfo.getBenefitPrice()));
				courseItem.put("url", null);

				courseItems.add(courseItem);
			}
		}
		data.put("courseItems", courseItems);
		/* 商家课程项目结束 */

		data.put("businessUrl", null);
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 课程详情
	 * 
	 * @param userID
	 *            用户ID（可空）
	 * @param buildingID
	 *            楼盘ID(不可空)
	 * @param courseID
	 *            课程ID（不可空）
	 */
	@RequestMapping(value = "getCourseItemDetail")
	@ResponseBody
	public Map<String, Object> getCourseItemDetail(String userID, String buildingID, String courseID, HttpServletRequest request) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, buildingID, courseID)) {
			return toJson;
		}
		LessonInfo lessonInfo = lessonInfoService.get(courseID);
		if (lessonInfo == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "课程不存在");
			return toJson;
		}
		BusinessInfo businessInfo = businessInfoService.get(lessonInfo.getBusinessInfoId());

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("courseID", lessonInfo.getId());
		data.put("courseName", lessonInfoService.getLessonName(lessonInfo));
		data.put("courseImage", lessonInfoService.getImageList(lessonInfo, request));
		data.put("originalPrice", ValidateUtil.validateDouble(lessonInfo.getBasePrice()));
		data.put("discountedPrice", ValidateUtil.validateDouble(lessonInfo.getBenefitPrice()));
		data.put("classNumber", ValidateUtil.validateInteger(lessonInfo.getLessonCount()));
		data.put("limitPeople", ValidateUtil.validateInteger(lessonInfo.getPeopleLimit()));
		data.put("classTime", DateFormatUtils.format(lessonInfo.getStartTime(), "yyyy-MM-dd") + "至" + DateFormatUtils.format(lessonInfo.getEndTime(), "MM-dd"));
		data.put("classLocation", lessonInfo.getAddress());
		data.put("courseDesc", lessonInfo.getContent());
		data.put("stockNumber", ValidateUtil.validateInteger(lessonInfo.getPeopleLimit()) - ValidateUtil.validateInteger(lessonInfo.getSellCount()));
		data.put("businessID", businessInfo.getId());
		data.put("businessName", businessInfo.getBusinessName());
		data.put("isNormal", businessInfoService.isBusinessNormal(businessInfo));
		data.put("isCollection", StringUtils.isNotBlank(userID) ? myCollectService.isCollect(userID, buildingID, businessInfo.getId()) : 0);
		data.put("businessPhone", businessInfo.getPhoneNum());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 商家首页
	 * 
	 * @param userID
	 *            用户ID（可空）
	 * @param buildingID
	 *            楼盘ID(不可空)
	 * @param businessID
	 *            商家ID（不可空）
	 */
	@RequestMapping(value = "getCourseIndex")
	@ResponseBody
	public Map<String, Object> getCourseIndex(String userID, String buildingID, String businessID, HttpServletRequest request) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, buildingID, businessID)) {
			return toJson;
		}
		BusinessInfo businessInfo = businessInfoService.get(businessID);
		if (businessInfo == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "商家不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("businessID", businessInfo.getId());
		data.put("businessName", businessInfo.getBusinessName());
		data.put("businessImage", businessInfoService.formatBusinessPic(businessInfo.getBusinessPic(), request));
		data.put("isNormal", businessInfoService.isBusinessNormal(businessInfo));
		data.put("isCollection", StringUtils.isNotBlank(userID) ? myCollectService.isCollect(userID, buildingID, businessInfo.getId()) : 0);
		data.put("businessAddress", businessInfoService.getAddress(businessInfo));
		data.put("businessPhone", businessInfo.getPhoneNum());

		/* 团购活动开始 */
		List<Map<String, Object>> groupBuys = new ArrayList<Map<String, Object>>();
		List<GroupPurchaseBean> groupPurchaseBeans = groupPurchaseService.getBusinessGroupPurchase(businessID);
		if (groupPurchaseBeans != null && groupPurchaseBeans.size() != 0) {
			int count = 0;
			for (GroupPurchaseBean groupPurchaseBean : groupPurchaseBeans) {
				// 只显示两个团购活动
				if (count == 2) {
					break;
				}

				Map<String, Object> groupBuy = new HashMap<String, Object>();
				groupBuy.put("groupBuyID", groupPurchaseBean.getGroupPurchaseTimeId());
				groupBuy.put("groupBuyName", groupPurchaseBean.getGroupPurcName());
				groupBuy.put("groupBuyImage", groupPurchaseService.formatGroupPurchaseImg(groupPurchaseBean.getGroupPurcPic(), request));
				groupBuy.put("groupBuyPrice", ValidateUtil.validateDouble(groupPurchaseBean.getGroupPurcMoney()));
				groupBuy.put("marketPrice", ValidateUtil.validateDouble(groupPurchaseBean.getMarketMoney()));
				groupBuy.put("soldNum", ValidateUtil.validateInteger(groupPurchaseBean.getSaleNum()));
				groupBuy.put("groupBuyUrl", null);

				groupBuys.add(groupBuy);
				count++;
			}
		}
		data.put("groupBuy", groupBuys);
		/* 团购活动结束 */

		data.put("businessHours", businessInfo.getBusinessHours());
		data.put("businessLabels", businessInfoService.getBusinessLabelList(businessInfo));
		data.put("businessDesc", businessInfo.getBusinessIntroduce());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}
}