/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.recharge.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.its.common.config.Global;
import com.its.common.persistence.Page;
import com.its.common.utils.StringUtils;
import com.its.common.web.BaseController;
import com.its.modules.recharge.entity.RechargeManage;
import com.its.modules.recharge.entity.RechargePlanDTO;
import com.its.modules.recharge.service.RechargeManageService;
import com.its.modules.village.entity.VillageInfo;
import com.its.modules.village.service.VillageInfoService;

/**
 * 充值管理Controller
 * 
 * @author ChenXiangyu
 * @version 2017-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/recharge/rechargeManage")
public class RechargeManageController extends BaseController {

	@Autowired
	private RechargeManageService rechargeManageService;
	@Autowired
	private VillageInfoService villageInfoService;
	/** 保存操作：添加 */
	private static final String OPTION_ADD = "add";
	/** 保存操作：编辑 */
	private static final String OPTION_EDIT = "edit";

	@ModelAttribute
	public RechargeManage get(@RequestParam(required = false) String id) {
		RechargeManage entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = rechargeManageService.get(id);
		}
		if (entity == null) {
			entity = new RechargeManage();
		}
		return entity;
	}

	@RequiresPermissions("recharge:rechargeManage:view")
	@RequestMapping(value = { "list", "" })
	public String list(RechargeManage rechargeManage, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<RechargePlanDTO> page = new Page<RechargePlanDTO>(request, response);
		List<RechargePlanDTO> rechargePlanList = rechargeManageService.findPageForUser(page, rechargeManage);
		page.setList(rechargePlanList);
		model.addAttribute("page", page);
		if(rechargeManage.getVillageInfo() != null){
			// 所选省
			model.addAttribute("selecttedAddrPro", rechargeManage.getVillageInfo().getAddrPro());
			// 所选市
			model.addAttribute("selecttedAddrCity", rechargeManage.getVillageInfo().getAddrCity());
			// 所选楼盘
			model.addAttribute("selecttedVillageInfoId", rechargeManage.getVillageInfo().getId());
		}
		return "modules/recharge/rechargeManageList";
	}

	@RequiresPermissions("recharge:rechargeManage:view")
	@RequestMapping(value = "form")
	public String form(RechargeManage rechargeManage, Model model, @RequestParam String villageInfoId,
			@RequestParam String option) {
		if (rechargeManage.getVillageInfo() == null) {
			rechargeManage.setVillageInfo(new VillageInfo());
		}
		if (option != null && !option.isEmpty()) {
			if (OPTION_ADD.equals(option)) {
				// 添加操作
				rechargeManage.setVillageInfo(villageInfoService.get(villageInfoId));
				model.addAttribute("rechargeManage", rechargeManage);
			} else if (OPTION_EDIT.equals(option)) {
				// 编辑操作
				rechargeManage.getVillageInfo().setId(villageInfoId);
				List<RechargePlanDTO> rechargePlanList = rechargeManageService.findListForUser(rechargeManage);
				if (rechargePlanList != null && !rechargePlanList.isEmpty()) {
					model.addAttribute("rechargeManage", rechargePlanList.get(0));
				}
			}
		}
		model.addAttribute("option", option);
		return "modules/recharge/rechargeManageForm";
	}

	@SuppressWarnings("rawtypes")
	@RequiresPermissions("recharge:rechargeManage:edit")
	@RequestMapping(value = "save")
	public String save(RechargePlanDTO rechargePlan, Model model, RedirectAttributes redirectAttributes,
			@RequestParam String option) {
		JSONObject json = new JSONObject(rechargePlan.getRechargeMoney());
		// 充值面额Map，key为充值面额，value为对应的赠送金额
		Map<Double, Double> rechargeMoneyMap = new HashMap<Double, Double>();
		Iterator iterator = json.keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			rechargeMoneyMap.put(Double.parseDouble(key), Double.parseDouble((String) json.get(key)));
		}

		if (OPTION_ADD.equals(option)) {
			// 添加操作
			rechargeManageService.add(rechargePlan.getVillageInfo(), rechargeMoneyMap);
		} else if (OPTION_EDIT.equals(option)) {
			// 修改操作
			rechargeManageService.edit(rechargePlan.getVillageInfo(), rechargeMoneyMap);
		}
		addMessage(redirectAttributes, "保存充值计划成功");
		return "redirect:" + Global.getAdminPath() + "/recharge/rechargeManage/?repage";
	}

	@RequiresPermissions("recharge:rechargeManage:edit")
	@RequestMapping(value = "delete")
	public String delete(RechargeManage rechargeManage, RedirectAttributes redirectAttributes) {
		rechargeManageService.delete(rechargeManage, RechargeManageService.option_Mode_delete);
		addMessage(redirectAttributes, "删除充值计划成功");
		return "redirect:" + Global.getAdminPath() + "/recharge/rechargeManage/?repage";
	}

	/**
	 * 验证选择的楼盘是否可用
	 * 
	 * @param villageId
	 *            选择的楼盘ID
	 * @return 是否可用
	 */
	@ResponseBody
	@RequiresPermissions("recharge:rechargeManage:view")
	@RequestMapping(value = "checkVillageInfoId")
	public String checkVillageInfoId(String villageId) {
		if (villageId != null && !villageId.isEmpty()) {
			if (rechargeManageService.getRechargeCountByVillage(villageId) <= 0) {
				return Boolean.TRUE.toString();
			}
		}
		return Boolean.FALSE.toString();
	}
}