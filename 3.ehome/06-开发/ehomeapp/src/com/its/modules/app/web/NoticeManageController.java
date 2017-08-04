/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.app.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.config.Global;
import com.its.common.web.BaseController;
import com.its.modules.app.common.AppUtils;
import com.its.modules.app.common.ValidateUtil;
import com.its.modules.app.entity.NoticeManage;
import com.its.modules.app.service.NoticeManageService;

/**
 * 公告管理Controller
 * 
 * @author like
 * @version 2017-08-03
 */
@Controller
@RequestMapping(value = { "${appPath}/home", "${appPath}/community" })
public class NoticeManageController extends BaseController {

	@Autowired
	private NoticeManageService noticeManageService;

	/**
	 * 顶部社区公告
	 * 
	 * @param userID
	 *            用户ID（可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return
	 */
	@RequestMapping(value = "getCommunityBulletin")
	@ResponseBody
	public Map<String, Object> getCommunityBulletin(String userID, String buildingID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, buildingID)) {
			return toJson;
		}
		NoticeManage notice = noticeManageService.getLatestNotice(buildingID);
		Map<String, Object> data = new HashMap<String, Object>();
		if (notice != null) {
			data.put("bulletinID", notice.getId());
			data.put("isNew", AppUtils.dateSpan(notice.getCreateDate(), new Date()) < 7 ? 1 : 0);
			data.put("bulletinTitle", notice.getNoticeTitle());
			data.put("bulletinUrl", "");
		}
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}
	
	/**
	 * 公告列表
	 * 
	 * @param userID
	 *            用户ID（可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return
	 */
	@RequestMapping(value = "getBulletinList")
	@ResponseBody
	public Map<String, Object> getBulletinList(String userID, String buildingID) {
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, buildingID)) {
			return toJson;
		}
		List<NoticeManage> list = noticeManageService.getNoticeList(buildingID);
		List<Map<String, Object>> data = new ArrayList<>();
		for (NoticeManage notice : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("bulletinID", notice.getId());
			map.put("bulletinTitle", notice.getNoticeTitle());
			map.put("publishDate", DateFormatUtils.format(notice.getCreateDate(), "yyyy-MM-dd"));
			data.add(map);
		}
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 公告详细
	 * 
	 * @param userID
	 *            用户ID
	 * @param bulletinID
	 *            公告ID
	 * @return
	 */
	@RequestMapping(value = "getBulletinDetail")
	@ResponseBody
	public Map<String, Object> getBulletinDetail(String userID, String bulletinID) {
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, bulletinID)) {
			return toJson;
		}
		NoticeManage notice = noticeManageService.get(bulletinID);
		if (notice == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "公告不存在");
			return toJson;
		}
		Map<String, Object> data = new HashMap<>();
		data.put("bulletinTitle", notice.getNoticeTitle());
		data.put("bulletinContent", notice.getNoticeContent());
		data.put("publishDate", DateFormatUtils.format(notice.getCreateDate(), "yyyy-MM-dd"));
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}
}