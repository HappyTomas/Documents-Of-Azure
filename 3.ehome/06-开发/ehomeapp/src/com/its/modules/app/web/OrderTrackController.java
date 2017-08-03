/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.its.common.config.Global;
import com.its.common.persistence.Page;
import com.its.common.web.BaseController;
import com.its.common.utils.StringUtils;
import com.its.modules.app.entity.OrderTrack;
import com.its.modules.app.service.OrderTrackService;

/**
 * 订单跟踪Controller
 * @author sushipeng
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/app/orderTrack")
public class OrderTrackController extends BaseController {

	@Autowired
	private OrderTrackService orderTrackService;
	
	@ModelAttribute
	public OrderTrack get(@RequestParam(required=false) String id) {
		OrderTrack entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = orderTrackService.get(id);
		}
		if (entity == null){
			entity = new OrderTrack();
		}
		return entity;
	}
	
	@RequiresPermissions("app:orderTrack:view")
	@RequestMapping(value = {"list", ""})
	public String list(OrderTrack orderTrack, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderTrack> page = orderTrackService.findPage(new Page<OrderTrack>(request, response), orderTrack); 
		model.addAttribute("page", page);
		return "modules/app/orderTrackList";
	}

	@RequiresPermissions("app:orderTrack:view")
	@RequestMapping(value = "form")
	public String form(OrderTrack orderTrack, Model model) {
		model.addAttribute("orderTrack", orderTrack);
		return "modules/app/orderTrackForm";
	}

	@RequiresPermissions("app:orderTrack:edit")
	@RequestMapping(value = "save")
	public String save(OrderTrack orderTrack, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orderTrack)){
			return form(orderTrack, model);
		}
		orderTrackService.save(orderTrack);
		addMessage(redirectAttributes, "保存订单跟踪成功");
		return "redirect:"+Global.getAdminPath()+"/app/orderTrack/?repage";
	}
	
	@RequiresPermissions("app:orderTrack:edit")
	@RequestMapping(value = "delete")
	public String delete(OrderTrack orderTrack, RedirectAttributes redirectAttributes) {
		orderTrackService.delete(orderTrack);
		addMessage(redirectAttributes, "删除订单跟踪成功");
		return "redirect:"+Global.getAdminPath()+"/app/orderTrack/?repage";
	}

}